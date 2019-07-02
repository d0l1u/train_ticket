package com.kuyou.train.thread.consumer;

import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.OptlogConstant;
import com.kuyou.train.common.enums.AccountWayEnum;
import com.kuyou.train.common.status.AccountStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.entity.po.TrainOptlog;
import com.kuyou.train.entity.resp.BookOrderResp;
import com.kuyou.train.entity.resp.BookOrderTicketResp;
import com.kuyou.train.thread.BaseThread;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * BookConsumerThread
 *
 * @author taokai3
 * @date 2018/12/26
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BookConsumerThread extends BaseThread<BookOrderResp> {

    private BookOrderResp orderResp;

    @Override
    public void execute() {
        String orderId = orderResp.getOrderId();
        log.info("orderId:{} 预订占位结果:{}", orderId, orderResp);
        //查询原单信息
        OrderPo orderPo = orderService.selectByOrderId(orderId);
        String status = orderPo.getOrderStatus();
        Integer accountFromWay = orderPo.getAccountFromWay();
        String robotIp = orderResp.getRobotIp();
        Integer resendNum = orderPo.getResendNum();
        log.info("【预订占位响应】{} 当前订单状态:{}, 重试次数:{}", orderId, status, resendNum);

        //是否申请取消
        if (OrderStatus.CANCEL_INIT.equals(status)) {
            log.info("{}订单申请取消,不做任何操作，直接跳过");
            historyService.insertBookLog(orderId, "用户申请取消");
            return;
        }

        //前置状态判断
        if (!OrderStatus.BOOK_ING.equals(status) && !OrderStatus.BOOK_MANUAL.equals(status) && !OrderStatus.BOOK_RESEND.equals(status)) {
            log.info("订单状态非法, 不能进行update");
            historyService.insertBookLog(orderId, "订单状态非法, 不能进行update");
            return;
        }

        //占位成功
        if (orderResp.getSuccess()) {
            if(AccountWayEnum.KUYOU.getValue().equals(accountFromWay)){
                int update = accountService.updateStatusByIds(AccountStatus.TEMP_STOP, Lists.newArrayList(orderPo.getAccountId()));
                log.info("占位成功后，账号休息结果:{}", update);
            }
            success(orderPo, robotIp);
            return;
        }

        //通过message 获取 映射表
        String message = orderResp.getMessage();
        TrainOptlog trainOptlog = trainOptlogService.select4Book(message);
        if(trainOptlog == null){
            //匹配错误信息为空，则转人工处理
            log.info("message匹配为空，转人工处理:{}", message);
            historyService.insertBookLog(orderId, robotIp, message);
            OrderPo update = new OrderPo();
            update.setOrderStatus(OrderStatus.BOOK_MANUAL);
            orderService.updateByOrderId(update, orderId, orderPo.getOrderStatus());
            return;
        }
        String logId = trainOptlog.getLogId();

        //判断高铁订单待核验
        String delayOrder = orderPo.getDelayOrder();
        String channel = orderPo.getChannel();

        // 状态待核验
        if ("301030".equals(channel) && "1".equals(delayOrder) && (logId.equals("E0") || logId.equals("01") || logId.equals("05"))) {
            historyService.insertBookLog(orderId, robotIp, "高铁订单，delayOrder=1并且乘客待核验，转人工处理");
            OrderPo update = new OrderPo();
            update.setOrderStatus(OrderStatus.BOOK_MANUAL);
            update.setReturnOptlog("P1");
            update.setResendNum(1);
            orderService.updateByOrderId(update, orderId, orderPo.getOrderStatus());
            return;
        }

        //判断是否是切换账号
        if(OptlogConstant.SWITCH_ACCOUNT.contains(logId)){
            //判断账号来源，如果是分销商账号直接失败
            if(AccountWayEnum.CHANNEL.getValue().equals(accountFromWay)){
                historyService.insertBookLog(orderId, robotIp, message);
                orderFail(orderId, orderPo.getOrderStatus(), trainOptlog);
            } else {
                String stopCode = OptlogConstant.STOP_MAP.get(logId);
                if(StringUtils.isNotBlank(stopCode)){
                    //停用账号:
                    int stop = accountService.stop(orderPo.getAccountId(), stopCode);
                    log.info("{}:账号停用结果:{}",orderPo.getAccountId(), stop);
                }else{
                    //临时停用账号
                    int stop = accountService.updateStatusByIds(AccountStatus.TEMP_STOP, Lists.newArrayList(orderPo.getAccountId()));
                    log.info("{}:账号临时停用结果:{}",orderPo.getAccountId(), stop);
                }
                //重置accountId为空
                log.info("进行账号切换处理");
                historyService.insertBookLog(orderId, "切换账号:" + message);
                orderSwitchAccount(orderId, orderPo.getOrderStatus(), trainOptlog, orderPo.getResendNum());
            }
            return;
        }

        //判断是否是直接失败
        historyService.insertBookLog(orderId, robotIp, message);
        String type = trainOptlog.getType();
        if("11".equals(type)){
            if(AccountWayEnum.KUYOU.getValue().equals(accountFromWay)){
                int update = accountService.updateStatusByIds(AccountStatus.FREE, Lists.newArrayList(orderPo.getAccountId()));
                log.info("占位失败后，账号释放结果:{}", update);
            }
            orderFail(orderId, orderPo.getOrderStatus(), trainOptlog);
            return;
        }

        //判断是否是重发
        if("00".equals(type)){
            //判断是否超过重发次数
            resendNum = resendNum == null ? 1 : resendNum;
            if(resendNum >= 3){
                //返回人工
                orderManual(orderId, orderPo.getOrderStatus(), trainOptlog);
            }else{
                //进行重发
                orderResend(orderId, orderPo.getOrderStatus(), trainOptlog, resendNum);
            }
            return;
        }

        //其他就是人工
        orderManual(orderId, orderPo.getOrderStatus(), trainOptlog);
    }

    /**
     * 预订失败
     * @param orderId
     * @param statusPre
     * @param trainOptlog
     */
    private void orderFail(String orderId, String statusPre, TrainOptlog trainOptlog) {
        OrderPo update = new OrderPo();
        update.setOrderStatus(OrderStatus.ORDER_FAIL);
        update.setReturnOptlog(trainOptlog.getLogId());
        update.setErrorInfo(trainOptlog.getFailCode());
        orderService.updateByOrderId(update, orderId, statusPre);
        notifyService.updateBookOrder(orderId);
    }

    /**
     * 预订人工
     * @param orderId
     * @param statusPre
     * @param trainOptlog
     */
    private void orderManual(String orderId, String statusPre, TrainOptlog trainOptlog) {
        OrderPo update = new OrderPo();
        update.setOrderStatus(OrderStatus.BOOK_MANUAL);
        update.setReturnOptlog(trainOptlog.getLogId());
        update.setResendNum(0);
        orderService.updateByOrderId(update, orderId, statusPre);
    }

    /**
     * 预订重发
     * @param orderId
     * @param statusPre
     * @param trainOptlog
     * @param num
     */
    private void orderResend(String orderId, String statusPre,  TrainOptlog trainOptlog, Integer num) {
        num = num == null ? 0 : num;
        OrderPo update = new OrderPo();
        update.setOrderStatus(OrderStatus.BOOK_RESEND);
        update.setReturnOptlog(trainOptlog.getLogId());
        update.setResendNum(num + 1);
        orderService.updateByOrderId(update, orderId, statusPre);
    }

    /**
     * 预订重发
     * @param orderId
     * @param statusPre
     * @param trainOptlog
     * @param num
     */
    private void orderSwitchAccount(String orderId, String statusPre, TrainOptlog trainOptlog, Integer num) {
        OrderPo update = new OrderPo();
        num = num == null ? 0 : num;
        if(num >= 3){
            update.setOrderStatus(OrderStatus.BOOK_MANUAL);
            update.setResendNum(0);
        }else{
            update.setOrderStatus(OrderStatus.BOOK_RESEND);
            update.setResendNum(num + 1);
        }
        update.setReturnOptlog(trainOptlog.getLogId());
        update.setAccountId(0);
        orderService.updateByOrderId(update, orderId, statusPre);
    }



    /**
     * 占位成功
     *
     * @param orderPo
     * @param robotIp
     */
    private void success(OrderPo orderPo, String robotIp) {
        String orderId = orderPo.getOrderId();
        //判断预付金额 和 实购金额 是否一致
        String payType = orderPo.getIsPay();
        BigDecimal preMoney = orderPo.getPayMoney();
        BigDecimal buyMoney = orderResp.getTotalPrice();
        log.info("分销商预付金额:{}, 供货商实购金额:{}, payType:{}[00:占位支付, 11:占位]", preMoney, buyMoney, payType);

        OrderPo updateOrder = new OrderPo();
        updateOrder.setFromTime(orderResp.getFromTime());
        updateOrder.setToTime(orderResp.getToTime());
        updateOrder.setFromCity(orderResp.getFromStationName());
        updateOrder.setFrom3c(orderResp.getFromStationCode());
        updateOrder.setToCity(orderResp.getToStationName());
        updateOrder.setTo3c(orderResp.getToStationCode());
        updateOrder.setBuyMoney(orderResp.getTotalPrice());
        updateOrder.setOutTicketBillno(orderResp.getSequence());
        updateOrder.setOutTicketTime(new Date());
        updateOrder.setPayLimitTime(orderResp.getPayLimitTime());
        updateOrder.setReturnOptlog("");
        updateOrder.setErrorInfo("6");
        updateOrder.setOrderStatus(OrderStatus.PAY_WAIT);

        List<OrderCpPo> updateCps = Lists.newArrayList();
        List<BookOrderTicketResp> tickets = orderResp.getTickets();
        for (BookOrderTicketResp ticket : tickets) {
            OrderCpPo cp = new OrderCpPo();
            cp.setCpId(ticket.getCpId());
            cp.setBuyMoney(ticket.getPrice().toString());
            cp.setSubOutticketBillno(ticket.getSubSequence());
            cp.setTrainBox(ticket.getCoachName());
            cp.setSeatNo(ticket.getSeatName());
            updateCps.add(cp);
        }

        if ("00".equals(payType) && buyMoney.compareTo(preMoney) > 0) {
            //重置订单状态为准备取消，并且更新 "3"
            log.info("分销商预付金额 < 供货商实购金额, 转取消");
            historyService.insertBookLog(orderId, "分销商预付金额 < 供货商实购金额, 转取消");
            updateOrder.setOrderStatus(OrderStatus.CANCEL_INIT);
            updateOrder.setErrorInfo("3");
            updateOrder.setReturnOptlog("C3");
        } else {
            //一人多单判断
            boolean lockResult = true;
            String redisOrderId = "";
            try{
                lockResult = dataJedisClient.lockByNX(orderResp.getSequence(), orderId, 30 * 60);
                redisOrderId = dataJedisClient.get(orderResp.getSequence());
            }catch (Exception e){
                log.info("一人多单判断, 设置分布式锁异常", e);
            }
            if (!lockResult && !orderId.equals(redisOrderId)) {
                log.info("可能存在一人多单, 或者往返票赔款的情况，转入人工处理");
                historyService.insertBookLog(orderId, "可能存在一人多单");
                updateOrder.setOrderStatus(OrderStatus.BOOK_MANUAL);
                updateOrder.setReturnOptlog("29");
            } else {
                if ("00".equals(payType)) {
                    log.info("占位成功, 直接支付");
                    // 开始支付
                    updateOrder.setOrderStatus(OrderStatus.PAY_BEGIN);
                    historyService.insertBookLog(orderId, robotIp,"占位成功, 直接支付");
                } else {
                    log.info("占位成功, 等待分销商支付");
                    historyService.insertBookLog(orderId, robotIp, "占位成功, 等待分销商支付");
                }
            }
        }

        //更新数据
        orderService.updateOrderAndCp(orderId, orderPo.getOrderStatus(), updateOrder, updateCps);

        //一人多单，直接支付，不进行回调
        String orderStatus = updateOrder.getOrderStatus();
        if (!orderStatus.equals(OrderStatus.BOOK_MANUAL) && !orderStatus.equals(OrderStatus.PAY_BEGIN)) {
            //插入回调
            notifyService.updateBookOrder(orderId);
        }
    }
}
