package com.kuyou.train.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.status.ChangeNotifyStatus;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.status.RefundStatus;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.*;
import com.kuyou.train.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * CallBackServiceImpl
 *
 * @author taokai3
 * @date 2018/11/8
 */
@Slf4j
@Service
public class CallBackServiceImpl implements CallBackService {

    @Resource
    private HistoryService historyService;

    @Resource
    private TicketEntranceService ticketEntranceService;

    @Resource
    private ChangeService changeService;

    @Resource
    private NotifyService notifyService;

    @Resource
    private JedisClient orderJedisClient;

    @Resource
    private OrderService orderService;

    @Resource
    private RefundService refundService;

    @Resource
    private ChangeCpService changeCpService;

    @Override
    public void bookOrder(String dataJsonStr) {
        JSONObject resultJson = JSON.parseObject(dataJsonStr);
        String myOrderId = resultJson.getString("orderid");
        String supplierOrderId = resultJson.getString("transactionid");
        OrderPo orderPo = orderService.selectByMyOrderId(myOrderId);
        String orderId = orderPo.getOrderId();
        String orderStatus = orderPo.getOrderStatus();
        log.info("orderId:{}, myOrderId:{}, supplierOrderId:{},当前订单状态:{}", orderId, myOrderId, supplierOrderId,
                orderStatus);

        //前置状态应该为:人工，预订中
        if (!OrderStatus.BOOK_MANUAL.equals(orderStatus) && !OrderStatus.BOOK_ING.equals(orderStatus)) {
            throw new RuntimeException("预订占位状态不允许更新数据");
        }

        Boolean success = resultJson.getBoolean("success");
        Boolean orderSuccess = resultJson.getBoolean("ordersuccess");
        Integer code = resultJson.getInteger("code");
        String msg = resultJson.getString("msg");
        if (!success || !orderSuccess || !code.equals(100)) {
            log.info("占位失败,Msg:{}", msg);
            if (Integer.valueOf(orderStatus) <= Integer.valueOf(OrderStatus.BOOK_MANUAL)) {
                orderService.updateStatus(orderId, OrderStatus.BOOK_MANUAL);
                historyService.insertBookLog(orderId, String.format("占位失败,Msg:%s", msg));
            } else {
                log.info("占位失败，但是状态为支付流程状态，不做处理,Msg:{}", msg);
            }
            return;
        }

        //将参数转成系统结果 TODO

        //推送到redis队里中
        Long lpush = orderJedisClient.lpush(KeyConstant.BOOK_RESP, "");
        log.info("预订占位结果lpush结果:{}", lpush);
    }

    @Override
    public void bookPay(String dataStr) {
        JSONObject resultJson = JSON.parseObject(dataStr);
        String myOrderId = resultJson.getString("orderid");
        String supplierOrderId = resultJson.getString("transactionid");
        OrderPo order = orderService.selectByMyOrderId(myOrderId);
        String orderId = order.getOrderId();
        log.info("分销商ID:{}, 我方ID:{},供货商ID:{}", orderId, myOrderId, supplierOrderId);

        String preStatus = order.getOrderStatus();
        log.info("当前订单状态:{}", preStatus);
        //前置状态应该为:人工，支付中
        if (!OrderStatus.PAY_MANUAL.equals(preStatus) && !OrderStatus.PAY_ING.equals(preStatus)) {
            throw new RuntimeException("预订支付状态不允许更新数据");
        }

        String isSuccess = resultJson.get("isSuccess").toString();
        if (!"Y".equals(isSuccess)) {
            log.info("预订支付回调‘N’,支付失败,转人工支付");
            orderService.updateStatus(orderId, OrderStatus.PAY_MANUAL);
            historyService.insertBookLog(orderId, "支付失败,转人工支付");
            return;
        }
        order.setSupplierOrderId(supplierOrderId);
        order.setPayTime(new Date());
        order.setOrderStatus(OrderStatus.ORDER_SUCCESS);

        //检票口
        String ticketEntrance = resultJson.getString("ticketEntrances");
        if (StringUtils.isNotBlank(ticketEntrance)) {
            log.info("检票口信息:{}", ticketEntrance);
            ticketEntrance = ticketEntrance.replaceAll("\\\"", "\"");
            JSONObject json = JSON.parseArray(ticketEntrance).getJSONObject(0);
            ticketEntranceService
                    .insertBook(orderId, order.getTrainNo(), order.getFromCity(), json.getString("entrance"));
        }

        //更新数据
        orderService.updateByOrderId(order, orderId, preStatus);
        //更新通知
        notifyService.updateBookPay(orderId);
        historyService.insertBookLog(orderId, "支付成功");
    }

    @Override
    public void refund(String dataJsonStr) {
        JSONObject resultJson = JSON.parseObject(dataJsonStr);

        String myOrderId = resultJson.getString("apiorderid");
        String sequence = resultJson.getString("trainorderid");
        OrderPo order = orderService.selectByMyOrderId(myOrderId);
        String orderId = order.getOrderId();
        log.info("我方ID:{},12306订单号:{}", myOrderId, sequence);

        String returnType = resultJson.getString("returntype");
        log.info("returnType:{} [0:线下退款, 1:线上退款, 2:线下改签退款]", returnType);
        JSONArray ticketArray = resultJson.getJSONArray("returntickets");

        if (!"1".equals(returnType)) {
            //线下退票，肯定成功的
            log.info("线下退款，记录即可");
            List<UnderRefundPo> list = Lists.newArrayList();
            for (int i = 0; i < ticketArray.size(); i++) {
                JSONObject json = ticketArray.getJSONObject(i);
                UnderRefundPo refund = new UnderRefundPo();
                refund.setName(json.getString("passengername"));
                refund.setCardNo(json.getString("passportseno"));
                refund.setMyOrderId(myOrderId);
                refund.setOrderId(orderId);
                refund.setRefundMoney(json.getBigDecimal("returnmoney"));
                refund.setSubSequence(json.getString("ticket_no"));
                refund.setSupplierType("01");
                refund.setStatus("00");
                refund.setRefundTime(json.getDate("returntime"));
                list.add(refund);
            }
            refundService.insertUnderRefund(list);
            return;
        }

        //线上退票回调，判断退票结果
        //我方调用，是一个一个退，这里也只会有一个
        JSONObject json = ticketArray.getJSONObject(0);
        String subSequence = json.getString("ticket_no");

        //根据orderId 和 subSequence 查询对应的退票订单
        RefundPo refundPo = refundService.selectRefundByMyOrderIdAndSeq(orderId, subSequence);
        String orderStatus = refundPo.getOrderStatus();
        String cpId = refundPo.getCpId();
        log.info("cpId:{} 状态为:{}", cpId, orderStatus);

        //前置状态应该为:人工，退票中
        if (!RefundStatus.REFUND_MANUAL.equals(orderStatus) && !RefundStatus.REFUND_ING.equals(orderStatus)) {
            log.info("退票状态不允许更新数据");
            refundService.updateStatus(orderId, cpId, "退票状态不允许更新数据");
            throw new RuntimeException("退票状态不允许更新数据");
        }

        //处理航天华有结果
        String message = json.getString("returnfailmsg");
        BigDecimal price = json.getBigDecimal("returnmoney");
        Boolean returnSuccess = json.getBoolean("returnsuccess");

        //最外层返回false， 内层返回true，这个一个异常的情况
        if (returnSuccess) {
            log.info("cpId:{}退票失败:{},转人工退票", cpId, message);
            historyService.insertRefundLog(orderId, cpId, String.format("退票失败,转人工退票Msg:%s", message));
            refundService.updateStatus(orderId, cpId, RefundStatus.REFUND_MANUAL);
        } else {
            log.info("退票成功，退票金额:{}", price);
            historyService.insertRefundLog(orderId, cpId, String.format("退票成功，退票金额:%s", price));

            refundPo.setRefund12306Money(price);
            refundPo.setRefundMoney(price);
            refundPo.setOrderStatus(RefundStatus.REFUND_AUTO_OVER);
            refundService.updateRefund(refundPo, orderId, cpId);

            //更新退票通知
            notifyService.updateRefund(orderId, cpId);
        }
    }

    @Override
    public void changeOrder(String backJsonStr) {
        JSONObject resultJson = JSON.parseObject(backJsonStr);
        String myOrderId = resultJson.getString("orderid");
        Integer changeId = Integer.valueOf(resultJson.getString("reqtoken"));
        Boolean success = resultJson.getBoolean("success");
        String msg = resultJson.getString("msg");

        //根据changeId 获取数据
        ChangePo changePo = changeService.selectByChangeId(changeId);
        String orderId = changePo.getOrderId();
        String preStatus = changePo.getChangeStatus();

        log.info("我方ID:{}, changeId:{}", myOrderId, changeId);
        log.info("当前订单改签状态:{}", preStatus);

        //前置状态应该为:人工，退票中
        if (!ChangeStatus.CHANGE_MANAUL.equals(preStatus) && !ChangeStatus.CHANGE_ING.equals(preStatus)) {
            log.info("改签状态不允许更新数据");
            historyService.insertChangeLog(orderId, changeId, "改签状态不允许更新数据");
            changeService.updateStatusById(changeId, ChangeStatus.CHANGE_MANAUL);
            throw new RuntimeException("改签状态不允许更新数据");
        }

        if (!success) {
            log.info("改签占位失败,Msg:{}", msg);
            historyService.insertChangeLog(orderId, changeId, "改签占位失败,Msg:" + msg);
            changeService.updateStatusPre(changeId, preStatus, ChangeStatus.CHANGE_MANAUL);
            return;
        }

        // 1低-高，2平价，3低-高
        Integer payType = resultJson.getInteger("priceinfotype");
        Date fromDate = resultJson.getDate("train_date");
        String runtimeStr = resultJson.getString("runtime");
        String startTimeStr = resultJson.getString("start_time");
        Date payLimitTime = resultJson.getDate("clear_time");
        BigDecimal totalPriceDiff = resultJson.getBigDecimal("totalpricediff");

        if (payType == null || fromDate == null || StringUtils.isBlank(runtimeStr) ||
                StringUtils.isBlank(startTimeStr) || payLimitTime == null) {
            log.info("响应参数不合法，必要字段存在null值");
            historyService.insertChangeLog(orderId, changeId, "响应参数不合法，必要字段存在null值");
            changeService.updateStatusById(changeId, ChangeStatus.CHANGE_MANAUL);
            throw new RuntimeException("响应参数不合法，必要字段存在null值");
        }

        //计算出发到达时间
        Date fromTime = DateUtil.parse(resultJson.getString("train_date") + " " + startTimeStr, DateFormat.DATE_HM);
        Date toTime = DateUtil.add(fromTime, DateUtil.mathMinutes(runtimeStr), TimeUnit.MINUTES);

        String toStationName = resultJson.getString("to_station_name");
        String toStationCode = resultJson.getString("to_station_code");
        String fromStationName = resultJson.getString("from_station_name");
        String fromStationCode = resultJson.getString("from_station_code");

        ChangePo updatePo = new ChangePo();
        updatePo.setChangeFromTime(fromTime);
        updatePo.setChangeToTime(toTime);
        updatePo.setBookTicketTime(new Date());
        updatePo.setPayLimitTime(payLimitTime);
        updatePo.setChangeStatus(ChangeStatus.CHANGE_PAY);
        updatePo.setFromCity(fromStationName);
        updatePo.setFromStationCode(fromStationCode);
        updatePo.setToCity(toStationName);
        updatePo.setToStationCode(toStationCode);

        /*
        1 表示新票款高于原票款，
        2 表示新票款与原票款相等，
        3 表示新票款低于原票款。
         */
        if (payType == 1) {
            historyService.insertChangeLog(orderId, changeId, "航天华有:低->高");
            updatePo.setChangeReceiveMoney(totalPriceDiff);
            payType = 3;
        } else if (payType == 2) {
            historyService.insertChangeLog(orderId, changeId, "航天华有:平价改签");
            payType = 1;
        } else if (payType == 3) {
            historyService.insertChangeLog(orderId, changeId, "航天华有:高->低");
            payType = 2;
            updatePo.setChangeReceiveMoney(totalPriceDiff);
        }
        updatePo.setAlterPayType(payType);

        //更新乘客
        JSONArray newtickets = resultJson.getJSONArray("newtickets");
        for (int i = 0; i < newtickets.size(); i++) {
            JSONObject json = newtickets.getJSONObject(i);
            String newTicketNo = json.getString("new_ticket_no");
            String oldTicketNo = json.getString("old_ticket_no");
            String cxin = json.getString("cxin");
            BigDecimal price = json.getBigDecimal("price");
            String[] cxinArr = cxin.split(",");
            String boxName = "";
            String seatName = "";
            if (cxinArr.length == 1) {
                seatName = cxinArr[0];
            } else {
                boxName = cxinArr[0];
                seatName = cxinArr[1];
            }
            ChangeCpPo changeCpPo = ChangeCpPo.builder().changeBuyMoney(price).changeTrainBox(boxName)
                    .changeSeatNo(seatName).subSequence(newTicketNo).build();
            changeCpService.updateByChangeIdAndSeq(changeCpPo, changeId, oldTicketNo);
        }

        updatePo.setChangeNotifyCount(0);
        updatePo.setChangeNotifyTime(new Date());
        updatePo.setChangeNotifyStatus(ChangeNotifyStatus.NOTIFY_WAIT);

        historyService.insertChangeLog(orderId, changeId, "改签成功");
        //更新订单信息
        changeService.updateByChangeId(updatePo, changeId);
    }

    @Override
    public void changePay(String backJsonStr) {
        JSONObject resultJson = JSON.parseObject(backJsonStr);
        String myOrderId = resultJson.getString("orderid");
        Integer changeId = Integer.valueOf(resultJson.getString("reqtoken"));

        ChangePo change = changeService.selectByChangeId(changeId);
        String supplierOrderId = change.getSupplierOrderId();
        String orderId = change.getOrderId();
        log.info("changeId:{}, 分销商ID:{}, 我方ID:{},供货商ID:{}", changeId, orderId, myOrderId, supplierOrderId);

        String preStatus = change.getChangeStatus();
        log.info("当前订单状态:{}", preStatus);
        //前置状态应该为:人工，支付中
        if (!ChangeStatus.PAY_MANUAL.equals(preStatus) && !ChangeStatus.PAY_ING.equals(preStatus)) {
            throw new RuntimeException("改签支付状态不允许更新数据");
        }

        Boolean success = resultJson.getBoolean("success");
        String msg = resultJson.getString("msg");
        if (!success) {
            log.info("改签支付失败,Msg:{}", msg);
            changeService.updateStatusPre(changeId, preStatus, ChangeStatus.PAY_MANUAL);
            historyService.insertChangeLog(orderId, changeId, String.format("改签支付失败,Msg:%s", msg));
            return;
        }

        change.setChangeStatus(ChangeStatus.PAY_SUCCESS);
        change.setPayTime(new Date());
        //changeService. TODO
        //插入通知
        notifyService.updateChangePay(changeId);
        historyService.insertChangeLog(orderId, changeId, "改签支付成功");
    }
}
