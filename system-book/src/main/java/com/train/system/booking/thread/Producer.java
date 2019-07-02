package com.train.system.booking.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.commons.jedis.SingleJedisClient;
import com.train.system.booking.SpringContext;
import com.train.system.booking.em.OrderStatus;
import com.train.system.booking.entity.Account;
import com.train.system.booking.entity.Order;
import com.train.system.booking.entity.Passenger;
import com.train.system.booking.mq.producer.BookingRequestProducer;
import com.train.system.booking.service.LogService;
import com.train.system.booking.service.OrderService;
import com.train.system.booking.service.PassengerService;
import com.train.system.booking.util.HttpUtil;
import com.train.system.booking.util.PropertyUtil;
import org.apache.rocketmq.client.producer.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

/**
 * Producer 生产者
 *
 * @author taokai3
 * @date 2018/6/17
 */
public class Producer implements Runnable {

    private Logger logger = LoggerFactory.getLogger(Producer.class);

    private int producerId;

    private static final String PRODUCER_LOCK_KEY_PREFIX = "BOOKING_PRODUCER_LOCK_";
    private static final String ORDER_QUEUE_LOCK_KEY_PREFIX = "BOOKING_ORDER_QUEUE_LOCK_";
    private static final String LOCK_VALUE = UUID.randomUUID().toString();
    private static final int PRODUCER_LOCK_TIME = 60;
    private static final int ORDER_LOCK_TIME = 60 * 3;

    //    private static final String PUSH = "Push";
    //    private static final String PULL = "Pull";

    private HttpUtil httpUtil = new HttpUtil();

    private SingleJedisClient jedisClient = SpringContext.getBean("jedisClient", SingleJedisClient.class);
    private OrderService orderService = SpringContext.getBean("orderService", OrderService.class);
    private PassengerService passengerService = SpringContext.getBean("passengerService", PassengerService.class);
    private LogService logService = SpringContext.getBean("logService", LogService.class);
    private BookingRequestProducer bookingRequestProducer = SpringContext
            .getBean("bookingRequestProducer", BookingRequestProducer.class);

    @Override
    public void run() {
        String producerLockKey = PRODUCER_LOCK_KEY_PREFIX + producerId;
        logger.info("ProducerID:{} 启动成功...", producerId);
        logger.info("Producer lockKey:{}, lockValue:{}", producerLockKey, LOCK_VALUE);
        while (!Thread.interrupted()) {
            try {
                //设置分布式锁
                String lockResult = jedisClient.set(producerLockKey, LOCK_VALUE, "NX", "EX", PRODUCER_LOCK_TIME);
                logger.info("{} Producer Lock Result:{}", producerLockKey, lockResult);
                if (!"OK".equals(lockResult)) {
                    continue;
                }

                //查询待处理的数据，默认查询 10 条数据
                //00:开始占位，初始状态；01:占位重发；02:队列中，并且操作时间+10<now()；11:正在占位，并且操作时间+5<now()
                List<Order> orderList = orderService.queryWaitBookingOrder(producerId);
                if (orderList == null || orderList.isEmpty()) {
                    continue;
                }
                logger.info("本次查询数据总计 {} 条", orderList.size());

                //循环处理数据
                for (Order order : orderList) {
                    //分布式锁，以order_id + booking 为key
                    String orderId = order.getOrderId();
                    String orderLockKey = ORDER_QUEUE_LOCK_KEY_PREFIX + orderId;
                    lockResult = jedisClient.set(orderLockKey, LOCK_VALUE, "NX", "EX", ORDER_LOCK_TIME);
                    logger.info("{} Order Lock Result:{}", orderLockKey, lockResult);
                    if (!"OK".equals(lockResult)) {
                        continue;
                    }
                    logService.insertBookingLog(orderId, "system-booking开始占位", "system-booking");


                    //根据order_id获取乘客信息
                    List<Passenger> passengerList = passengerService.queryListByOrderId(orderId);
                    System.err.println("###" + JSONObject.toJSONString(passengerList));
                    Integer accountId = order.getAccountId();
                    String accountFromWay = order.getAccountFromWay();
                    logger.info("Account ID:{}, Account From Way:{} [0:公司账号, 1:乘客自有]", accountId, accountFromWay);

                    //判断订单账号来源，如果是乘客自有账号，则直接根据账号ID查询账号信息 0:公司账号, 1:乘客自有
                    Account account = null;
                    String httpResult = "";
                    if ((accountId != null && !accountId.equals(0)) ||
                            ((accountId == null || accountId.equals(0)) && "0".equals(accountFromWay))) {

                        String url = "";
                        if (accountId != null && !accountId.equals(0)) {
                            //存在accountId,直接通过accountId查询账号
                            //{"success":true,"msg":"操作成功","data":{"id":2002,"username":"hcp687nto","password":"123456a","status":"22","channel":"19e","stopReason":"11","contact":0,"bookNum":0}}
                            //{"success":false,"msg":"账号获取失败"}
                            url = PropertyUtil.getValue("getAccountById") + "?id=" + accountId;
                        } else {
                            //匹配白名单获取账号
                            StringBuffer cardList = new StringBuffer();
                            for (int i = 0; passengerList != null && i < passengerList.size(); i++) {
                                Passenger passenger = passengerList.get(i);
                                String cardNo = passenger.getCardNo();
                                cardNo = cardNo.toUpperCase().replace("\\s", "");
                                cardList.append(cardNo).append(",");
                            }
                            logger.info("白名单匹配乘客证件列表:{}", cardList);
                            //{"success":true,"msg":"操作成功","data":{"id":1036187,"username":"ylVFws4E53","password":"hGfqffv7D1","status":"00","channel":"19e","stopReason":"3","contact":15,"bookNum":0}}
                            //{"success":false,"msg":"系统异常"}
                            url = PropertyUtil.getValue("getAccountByWhiteList") + "?passportNo=" + cardList;
                        }

                        //发送请求
                        httpResult = httpUtil.doHttpGet(url, 1000 * 5);
                        logger.info("Http Result:{}", httpResult);
                        if (!HttpUtil.EXCEPTION.equals(httpResult)) {
                            JSONObject httpJson = JSONObject.parseObject(httpResult);
                            Boolean success = httpJson.getBoolean("success");
                            if (success) {
                                JSONObject dataJson = httpJson.getJSONObject("data");
                                account = new Account();
                                account.setAccountId(dataJson.getInteger("id"));
                                account.setUsername(dataJson.getString("username"));
                                account.setPassword(dataJson.getString("password"));
                                account.setStatus(dataJson.getString("status"));
                            }
                        }
                    }

                    if (account == null) {
                        //                        //获取账号为空，修改订单状态为人工:44，
                        //                        int updateResult = orderService
                        //                                .updateOrderStatus(orderId, order.getOrderStatus(), OrderStatus.BOOKING_MANUAL);
                        //                        logger.info("订单状态修改结果:{}", updateResult);
                        //
                        //                        //插入日志
                        //                        logService
                        //                                .insertBookingLog(orderId, HttpUtil.EXCEPTION.equals(httpResult) ? "账号获取异常" : "账号获取为空",
                        //                                        "system-booking");
                        //                        //释放订单锁
                        //                        release(orderLockKey, LOCK_VALUE);
                        //                        continue;


                        account = new Account();
                        account.setUsername("taodada777");
                        account.setPassword("taokai1017");
                    }

                    StringBuffer logMessage = new StringBuffer();
                    logMessage.append("分配账号AccountID:").append(account.getAccountId()).append(",账号:")
                            .append(account.getUsername()).append(",密码:").append(account.getPassword()).append(",来源:")
                            .append(accountFromWay).append("(0:公司,1:乘客)");
                    logger.info(logMessage.toString());
                    logService.insertBookingLog(orderId, logMessage.toString(), "system-booking");


                    //根据前置状态，修改订单状态为队列中:02
                    int updateResult = orderService
                            .updateOrderStatus(orderId, order.getOrderStatus(), OrderStatus.BOOKING_QUEUE);
                    logger.info("队列中状态更新结果:{}", updateResult);

                    //String category = new WeightUtil().randomWeight(new WeightCategory("", 2), new WeightCategory("", 2));
                    //计算拉去和推送权重，如果是拉去，则将订单添加进 ，有机器人从MQ中获取处理。否则将订单添加进
                    //统一拉去模式

                    logger.info("Push Order To MQ");
                    SendStatus sendStatus = null;
                    try {
                        JSONObject requestJson = new JSONObject(true);
                        requestJson.put("orderId", orderId);
                        requestJson.put("publicIp", "127.0.0.1");

                        JSONObject accountJson = new JSONObject(true);
                        accountJson.put("username", account.getUsername());
                        accountJson.put("password", account.getPassword());

                        JSONObject dataJson = new JSONObject(true);
                        dataJson.put("trainCode", order.getTrainCode());
                        dataJson.put("departureDate",
                                new SimpleDateFormat("yyyy-MM-dd").format(order.getDepartureDate()));
                        dataJson.put("fromStationName", order.getFromStationName());
                        dataJson.put("fromStationCode", order.getFromStationCode());
                        dataJson.put("toStationName", order.getToStationName());
                        dataJson.put("toStationCode", order.getToStationCode());

                        JSONArray passengerArray = new JSONArray();
                        for (Passenger passenger : passengerList) {
                            JSONObject passengerJson = new JSONObject(true);
                            passengerJson.put("passengerNo", passenger.getPassengerNo());
                            passengerJson.put("name", passenger.getName());
                            passengerJson.put("cardType", passenger.getCardType());
                            passengerJson.put("cardNo", passenger.getCardNo());
                            passengerJson.put("ticketType", passenger.getTicketType());
                            passengerJson.put("seatType", passenger.getSeatType());
                            //学生信息
                            passengerJson.put("provinceName", passenger.getProvinceName());
                            passengerJson.put("provinceCode", passenger.getProvinceCode());
                            passengerJson.put("schoolName", passenger.getSchoolName());
                            passengerJson.put("schoolCode", passenger.getSchoolName());
                            passengerJson.put("studentNo", passenger.getStudentNo());
                            passengerJson.put("system", passenger.getSystem());
                            passengerJson.put("enterYear", passenger.getEnterYear());
                            passengerJson.put("limitBeginName", passenger.getLimitBeginName());
                            passengerJson.put("limitBeginCode", passenger.getLimitBeginCode());
                            passengerJson.put("limitEndName", passenger.getLimitEndName());
                            passengerJson.put("limitEndCode", passenger.getLimitEndCode());
                            passengerArray.add(passengerJson);
                        }
                        dataJson.put("passengers", passengerArray);

                        requestJson.put("account", accountJson);
                        requestJson.put("data", dataJson);

                        sendStatus = bookingRequestProducer.send(requestJson.toJSONString());

                    } catch (Exception e) {
                        logger.info("Push Order Exception", e);
                    }
                    logger.info("{} Push Result:{}", orderId, sendStatus);
                    if (!SendStatus.SEND_OK.equals(sendStatus)) {
                        //推送失败，重置订单状态为
                        logger.info("Push Order Fail, Update Order Status To Manual");
                        updateResult = orderService
                                .updateOrderStatus(orderId, order.getOrderStatus(), OrderStatus.BOOKING_MANUAL);
                        logger.info("占位人工状态更新结果:{}", updateResult);
                    }
                    //释放订单锁
                    release(orderLockKey, LOCK_VALUE);
                }
            } catch (Exception e) {
                logger.info("Producer Thread Exception", e);
            } finally {
                //释放生产分布式锁
                try {
                    release(producerLockKey, LOCK_VALUE);
                    Thread.sleep(3 * 1000);
                } catch (Exception e) {
                }
            }
        }
        logger.info("ProducerID:{} 结束...", producerId);
    }

    public int getProducerId() {
        return producerId;
    }

    public void setProducerId(int producerId) {
        this.producerId = producerId;
    }


    private Object release(String key, String value) {
        Object returnValue = null;
        try {
            returnValue = jedisClient
                    .eval("if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end",
                            1, key, value);
            logger.info("释放分布式锁 {} 结果:{}", key, returnValue);
        } catch (Exception e) {
            logger.info("释放分布式锁 {} 异常", key, e);
        }
        return returnValue;
    }
}
