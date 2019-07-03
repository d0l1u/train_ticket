package com.train.system.booking.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.train.system.booking.em.OrderStatus;
import com.train.system.booking.entity.Order;
import com.train.system.booking.entity.Passenger;
import com.train.system.booking.service.LogService;
import com.train.system.booking.service.OrderService;
import com.train.system.booking.service.PassengerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

/**
 * BookingCallbackConsumer
 *
 * @author taokai3
 * @date 2018/6/19
 */
public class BookingCallbackConsumer {

    private Logger logger = LoggerFactory.getLogger(BookingCallbackConsumer.class);

    private DefaultMQPushConsumer defaultMQPushConsumer;

    private static final String TOPIC = "TRAIN_BOOKING";
    private static final String TAG = "BOOKING_CALLBACK";

    @Resource
    private OrderService orderService;

    @Resource
    private LogService logService;

    @Resource
    private PassengerService passengerService;

    /**
     * 消费方法
     *
     * @throws MQClientException
     * @author: taoka
     * @date: 2018年3月27日 下午2:20:46
     */
    public void consumer() throws MQClientException {
        defaultMQPushConsumer.subscribe(TOPIC, TAG);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                int resendNumber = 0;
                try{
                    MessageQueue messageQueue = context.getMessageQueue();
                    String brokerName = messageQueue.getBrokerName();
                    String queueTopic = messageQueue.getTopic();
                    int queueId = messageQueue.getQueueId();
                    logger.info("MessageQueue brokerName:{}, topic:{}, queueId:{}", brokerName, queueTopic, queueId);

                    MessageExt messageExt = msgs.get(0);
                    long queueOffset = messageExt.getQueueOffset();
                    String propertyMaxOffset = messageExt.getProperty(MessageConst.PROPERTY_MAX_OFFSET);
                    long maxOffset = Long.parseLong(StringUtils.isBlank(propertyMaxOffset) ? "0" : propertyMaxOffset);
                    logger.info("queueOffset:{}, maxOffset:{}", queueOffset, maxOffset);

                    long diff = maxOffset - queueOffset;
                    if (diff > 1000000000) {
                        // 消息堆积情况的特殊处理，不太重要的信息，默认成功，不处理。
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    String topic = messageExt.getTopic();
                    String tags = messageExt.getTags();
                    String msgId = messageExt.getMsgId();
                    String keys = messageExt.getKeys();
                    resendNumber = messageExt.getReconsumeTimes();
                    logger.info("MessageExt topic:{}, tags:{}, msgId:{}, keys:{}, resendNumber:{}", topic, tags, msgId,
                            keys, resendNumber);

                    String body = new String(messageExt.getBody(), "UTF-8");;
                    logger.info("MessageExt body:{}", body);

                /*
                解析body
                {"orderId":"GTGJ180112153746431","status":"0000","message":"success","startMillis":1529387255936,"endMillis":1529387275555,"body":{"sequence":"EG98988377","trainCode":"G7010","departureDate":"2018-06-28","departureTime":"2018-06-28 11:27","arrivalDate":"2018-06-28","arrivalTime":"2018-06-28 12:39","fromStationName":"苏州","fromStationCode":"SZH","toStationName":"南京","toStationCode":"NJH","passengerCount":6,"totalPrice":"99.5","passengers":[{"name":"章海龙","subSequence":"EG98988377102002A","cardType":"1","cardNo":"342723197207160752","ticketType":"1","seatType":"O","boxNo":"02","boxName":"02","seatNo":"002A","seatName":"02A号","price":99.5,"loseTime":"2018-06-19 14:17:49"}]}}
                {"orderId":"20150619236454251","status":"3005","message":"成人票查询日期不在预售日期范围内2015-06-24[2018-06-19,2018-07-18]","startMillis":1529377126579,"endMillis":1529377128306,"body":{"trainCode":"1718","departureDate":"2015-06-24","fromStationName":"包头","fromStationCode":"BTC","toStationName":"包头东","toStationCode":"BDC","passengers":[{"passengerNo":"20150619236454252","name":"张玉朋","cardType":"1","cardNo":"370724198704210751","ticketType":"1","seatType":"1"}]}}
                 */
                    JSONObject responseJson = JSONObject.parseObject(body);
                    String orderId = responseJson.getString("orderId");
                    String status = responseJson.getString("status");
                    String message = responseJson.getString("message");
                    String data = responseJson.getString("body");

                    Order responseOrder = JSONObject.parseObject(data, Order.class);
                    responseOrder.setOrderId(orderId);

                    //根据orderId 查询订单的信息
                    Order order = orderService.selectById(orderId);
                    String orderStatus = order.getOrderStatus();
                    responseOrder.setOrderStatus(orderStatus);
                    logger.info("order Status:{}", orderStatus);

                    //判断 是否申请取消
                    if (OrderStatus.CANCEL_INIT.equals(orderStatus)) {
                        logger.info("订单申请取消，转取消流程");
                        logService.insertBookingLog(orderId, "订单申请取消", "system-booking");
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    //判断一人多单 TODO


                    //占位成功单独处理，主要判断
                    // 1. 无座票是否失败
                    // 2. 是否直接支付，如果已经支付重置状态为等待支付，否则为占位完成
                    if ("0000".equals(status)) { //占位成功
                        //0#9,0
                        //8#无
                        String extraSeatType = order.getExtraSeatType();
                        logger.info("备选坐席:{}", extraSeatType);
                        extraSeatType = StringUtils.isBlank(extraSeatType) ? "" : extraSeatType.split("#")[1];
                        String extra = extraSeatType.split(",")[0];

                        // seatName = "无座"
                        if (data.contains("无座") && !"9".equals(extra) && !"无".equals(extra)) {
                            logger.info("订到无票坐席，并且备选坐席没有选择‘无座’，转入取消流程");
                            logService.insertBookingLog(orderId, "订到无票坐席，并且备选坐席没有选择‘无座’，转入取消流程", "system-booking");
                            responseOrder.setOrderStatus(OrderStatus.CANCEL_INIT);
                            responseOrder.setErrorCode("1");
                            orderService.updateOrder(responseOrder);
                            //TODO
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }

                        String isPay = order.getIsPay();
                        //00:占位支付， 11:先占位后支付
                        logger.info("{} IsPay:{} [00:占位支付, 11:先占位后支付]", orderId, isPay);
                        String channel = order.getChannel();
                        BigDecimal payMoney = order.getPayMoney(); //分销商支付价格
                        BigDecimal realMoney = responseOrder.getTotalPrice(); //机器人支付价格
                        logger.info("channel:{}, 分销商支付价格:{}, 机器人支付价格:{}", channel, payMoney, realMoney);

                        if ("00".equals(isPay) && realMoney.compareTo(payMoney) > 0) {
                            // error_info = 3
                            logger.info("出票失败，票价不符，转取消流程");
                            logService.insertBookingLog(orderId, "出票失败，票价不符，转取消流程", "system-booking");
                            responseOrder.setOrderStatus(OrderStatus.CANCEL_INIT);
                            responseOrder.setErrorCode("3");
                            orderService.updateOrder(responseOrder);
                            //TODO
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }

                        //成功
                        logger.info("占位成功，补填占位信息...");
                        if("00".equals(isPay)){
                            responseOrder.setOrderStatus(OrderStatus.BEGIN_WAIT); //开始支付
                        }else{
                            responseOrder.setOrderStatus(OrderStatus.PAY_WAIT); //等待支付
                        }
                        responseOrder.setErrorCode("6");
                        orderService.updateOrder(responseOrder);
                        //TODO  释放账号信息
                        // TODO 更新白名单信息
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    //非成功，切换账号的情况。
                /*
                    1101:登录名不存在
                    1102:登录失败，用户名或密码为空
                    1103:密码输入错误。如果输错次数超过4次，用户将被锁定
                    1104:码输入错误。您的用户已经被锁定,锁定时间为20分钟,请稍后再试
                    1201:账号:您的用户信息被他人冒用，请重新在网上注册新的账户
                    1208:账号所属人核验未通过
                    1209:账号绑定手机核验未通过
                    2001:您的常用联系人数量已超过上限
                    5002:对不起，由于您取消次数过多，今日将不能继续受理您的订票请求
                    2005:对不起，由于您操作乘车人过于频繁，请明日再试！
                    4002:账号内存在多个未完成订单,请人工核实
                    4003:未完成订单信息不一致,请人工核实
                 */
                    String accountFromWay = order.getAccountFromWay();
                    logger.info("账号来源:{} [0:公司账号, 1:乘客自有]", accountFromWay);

                    //以下，主要针对途牛
                    String fialReson = "";
                    boolean changeAccountFlag = false;
                    if ("1101".equals(status)) {
                        fialReson = "26";
                        changeAccountFlag = true;
                    } else if ("1102,1103,1104".contains(status)) {
                        fialReson = "27";
                        changeAccountFlag = true;
                    } else if ("1201,1208".contains(status)) {
                        fialReson = "26";
                        changeAccountFlag = true;
                    } else if ("1209".equals(status)) {
                        fialReson = "21";
                        changeAccountFlag = true;
                    } else if ("2001".equals(status)) {
                        fialReson = "22";
                        changeAccountFlag = true;
                    } else if ("5002,2005".contains(status)) {
                        fialReson = "24";
                        changeAccountFlag = true;
                    } else if ("4002,4003".contains(status)) {
                        fialReson = "23";
                    }

                    if ("1".equals(accountFromWay) && StringUtils.isNotBlank(fialReson)) {
                        //自动失败处理
                        logger.info("乘客账号，自动失败处理");
                        responseOrder.setOrderStatus(OrderStatus.ORDER_FAIL);
                        responseOrder.setErrorCode(fialReson);
                        orderService.updateOrder(responseOrder);
                        //TODO
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    if (changeAccountFlag) {
                        //非乘客账号，切换账号，进行重发
                        logger.info("非乘客账号，切换账号，进行重发");
                        responseOrder.setAccountId(0);
                        responseOrder.setResendNum(order.getResendNum() + 1);
                        responseOrder.setOrderStatus(OrderStatus.BOOKING_RESEND);
                        orderService.updateOrder(responseOrder);
                        //TODO
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }


                    //直接重发的情况
                /*
                    0003:请求超时
                    0004:执行超出次数
                    0005:网络可能存在问题，请您重试一下！：
                    0006:的操作频率过快:
                    1204:个人信息页面解析xpath出错,请开发确定页面是否变更：
                    1301:用户未登陆：
                    2006:添加联系人操作失败:
                    3001:查询车次结果为空:
                    3006:获取车次查询路径失败：
                    3007:查询车次失败：
                    3008:查询车次返回html页面，可能服务器查询车次结果为空，请尝试更换服务器：
                    4004:未完成订单排队时间过长:
                    4005:查询未完成订单失败:
                    4006:排队订单取消失败:
                    5001:申请下单失败：
                    5009:占座失败:
                    5010:申请下单校验不通过：
                    5011:确认下单必要信息为空:
                    5012:确认下单失败:
                 */
                    if ("0003,0004,0005,0006,1204,1301,2006,3001,3006,3007,3008,4004,4005,4006,5001,5009,5010,5011,5012"
                            .contains(status)) {
                        //判断重发次数，如果 > 3 次，则转为人工

                        Integer resendNum = order.getResendNum();
                        if (resendNum % 3 == 0) {
                            logger.info("重发次数>3次，转为人工");
                            responseOrder.setResendNum(0);
                            responseOrder.setOrderStatus(OrderStatus.BOOKING_MANUAL);
                        } else {
                            logger.info("第 {} 次重发", resendNum);
                            responseOrder.setResendNum(resendNum + 1);
                            responseOrder.setOrderStatus(OrderStatus.BOOKING_RESEND);
                        }
                        orderService.updateOrder(responseOrder);
                        //TODO
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }


                    //直接失败的情况
                /*
                    2002:身份信息涉嫌被他人冒用:
                    2003:您的身份信息正在进行网上核验
                    2004:证件号码输入有误:
                    3003:该车次不可预订：
                    3005:选择的查询日期不在预售日期范围内:
                    5005:没有足够的票!:
                    5006:互联网售票实行实名制:
                    5007:与本次购票行程冲突，请将已购车票办理改签，或办理退票后重新购票:
                    5008:王**已被法院依法限制高消费，限制乘坐G字头列车。:
                 */
                    if ("2002,2003,2004,3003,3005,5005,5006,5007,5008".contains(status)) {
                        //直接失败
                        logger.info("直接失败");
                        responseOrder.setOrderStatus(OrderStatus.ORDER_FAIL);
                        responseOrder.setErrorCode("tk");
                        orderService.updateOrder(responseOrder);
                        //TODO  需要匹配失败原因
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }


                    //其余，一律人工处理
                    logger.info("其余，一律人工处理");
                    responseOrder.setOrderStatus(OrderStatus.BOOKING_MANUAL);
                    orderService.updateOrder(responseOrder);
                    //TODO
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception e){
                    if (resendNumber > 3) {
                        logger.info("消费者重发三次，均为异常。邮件通知开发处理", e);
                        //TODO
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    } else {
                        logger.info("消费异常。进行重新消费", e);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
            }
        });
        defaultMQPushConsumer.start();
    }

    public DefaultMQPushConsumer getDefaultMQPushConsumer() {
        return defaultMQPushConsumer;
    }

    public void setDefaultMQPushConsumer(DefaultMQPushConsumer defaultMQPushConsumer) {
        this.defaultMQPushConsumer = defaultMQPushConsumer;
    }
}
