package com.kuyou.train.thread.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.constant.WorkerType;
import com.kuyou.train.common.enums.UrlEnum;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.status.WorkerStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.common.util.HttpUtil;
import com.kuyou.train.entity.po.AccountPo;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.entity.po.ServicePo;
import com.kuyou.train.entity.po.WorkerPo;
import com.kuyou.train.entity.req.CancelReq;
import com.kuyou.train.entity.resp.CancelResp;
import com.kuyou.train.thread.BaseThread;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Cancel4BookProducerThread
 *
 * @author taokai3
 * @date 2018/11/10
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Cancel4BookProducerThread extends BaseThread<OrderPo> {

    private ServicePo servicePo;

    @Override
    public void execute() {
        String orderId = servicePo.getOrderId();
        String supplierType = servicePo.getSupplierType();
        log.info("取消订单信息:orderId:{}, supplierType:{}[{}]", orderId, supplierType, SupplierCode.MESSAGE);

        //判断是否超过支付截止时间
        Date payLimitTime = servicePo.getPayLimitTime();
        Date createTime = servicePo.getCreateTime();
        if (payLimitTime != null) {
            if (payLimitTime.before(new Date())) {
                log.info("超过支付截止时间，默认取消成功");
                bookLog(orderId, OrderStatus.ORDER_FAIL, "超过支付截止时间，默认取消成功");
                notifyService.updateBookCancel(orderId);
                return;
            }
        } else if (DateUtil.add(createTime, 1, TimeUnit.DAYS).before(new Date())) {
            log.info("超过30分钟，默认取消成功");
            bookLog(orderId, OrderStatus.ORDER_FAIL, "超过30分钟，默认取消成功");
            notifyService.updateBookCancel(orderId);
            return;
        }

        CancelReq req = CancelReq.builder().orderId(orderId).sequence(servicePo.getSequence())
                .supplierOrderId(servicePo.getSupplierOrderId()).bookFlag(servicePo.isBookFlag()).build();

        //获取账号密码
        Integer accountId = servicePo.getAccountId();
        if (accountId != null && accountId != 0) {
            AccountPo accountPo = accountService.selectByAccountId(accountId);
            req.setUsername(accountPo.getUsername());
            req.setPassword(accountPo.getPassword());
        }

        WorkerPo workerPo = null;
        if (SupplierCode.HTHY.equals(supplierType)) {
            //航天华有
            String httpResult = hthyService.bookCancel(servicePo);
            JSONObject httpJson = JSON.parseObject(httpResult);
            String msg = httpJson.getString("msg");
            Boolean success = httpJson.getBoolean("success");
            if (success) {
                log.info("请求航天华有预订取消，等待回调结果");
                bookLog(orderId, OrderStatus.ORDER_FAIL, String.format("取消成功"));
                notifyService.updateBookCancel(orderId);
            } else {
                log.info("请求航天华有预订取消失败");
                historyService.insertBookLog(orderId, String.format("请求航天华有预订取消失败,Msg:%s", msg));
                bookLog(orderId, OrderStatus.CANCEL_FAIL, String.format("请求航天华有预订取消失败,Msg:%s", msg));
//                orderService.updateStatus(orderId, OrderStatus.CANCEL_FAIL);
            }
            return;
        } else {
            //车票号
            List<String> subSequences = orderCpService.selectSeqByOrderId(orderId);
            req.setSubSequences(subSequences);

            //获取机器人
            workerPo = workerService.available(WorkerType.CANCEL);
            if (workerPo == null) {
                bookLog(orderId, OrderStatus.CANCEL_FAIL, "无空闲机器人");
                return;
            }

            //机器人取消
            String publicIp = workerPo.getPublicIp();
            String url = UrlEnum.CANCEL.format(publicIp);
            log.info("{}开始取消", url);
            historyService.insertBookLog(orderId, String.format("%s开始取消", publicIp));
            String responseStr = new HttpUtil().doHttpPost(url, req.toString(), 1000 * 60 * 2, true);
            log.info("取消机器人响应结果:{}", responseStr);
            CancelResp cancelResp = JSON.parseObject(responseStr, CancelResp.class);

            //取消失败
            if (cancelResp.isSuccess()) {
                bookLog(orderId, OrderStatus.ORDER_FAIL, String.format("取消成功"));
                notifyService.updateBookCancel(orderId);
                return;
            } else {
                bookLog(orderId, OrderStatus.CANCEL_FAIL, String.format("取消失败:%s", cancelResp.getMessage()));
            }
        }

        //释放机器人
        if (workerPo != null) {
            workerService.updateStatus(workerPo.getWorkerId(), WorkerStatus.FREE);
        }
    }
}
