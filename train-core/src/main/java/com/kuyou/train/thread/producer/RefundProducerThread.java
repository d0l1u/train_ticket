package com.kuyou.train.thread.producer;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.constant.WorkerType;
import com.kuyou.train.common.enums.UrlEnum;
import com.kuyou.train.common.status.RefundStatus;
import com.kuyou.train.common.status.WorkerStatus;
import com.kuyou.train.common.util.HttpUtil;
import com.kuyou.train.entity.dto.HthySyncDto;
import com.kuyou.train.entity.po.RefundPo;
import com.kuyou.train.entity.po.WorkerPo;
import com.kuyou.train.entity.req.RefundReq;
import com.kuyou.train.entity.resp.RefundResp;
import com.kuyou.train.thread.BaseThread;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * RefundProducerThread
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RefundProducerThread extends BaseThread<RefundPo> {

    private RefundPo refundPo;

    @Override
    public void execute() {
        String orderId = refundPo.getOrderId();
        String cpId = refundPo.getCpId();
        String subSequence = refundPo.getSubOutTicketBillno();
        log.info("线上退票订单orderId:{}, cpId:{}, subSequence:{}", orderId, cpId, subSequence);

        /* //判断是否已经发车，考虑停运列车，已过发车时间，依旧可以线上退票
        Date fromTime = refundPo.getFromTime();
        if (fromTime != null && fromTime.before(new Date())) {
            //已过发车时间，不可以退票
        }
        */

        String refundType = refundPo.getRefundType();
        refundType = StringUtils.isBlank(refundType) ? "11" : refundType;
        log.info("refundType:{}[11：正常单退票， 55：改签单退票]", refundType);

        //判断乘客的车票号是否存在
        String subTicketNo = refundPo.getSubOutTicketBillno();
        if (StringUtils.isBlank(subTicketNo)) {
            if (refundType.equals("11")) {
                //查询cp_orderinfo_cp表
                subTicketNo = orderCpService.selectByCpId(cpId).getSubOutticketBillno();
            } else {
                //查询elong_change_cp表
                subTicketNo = changeCpService.selectSequenceByNewCpId(cpId).getSubSequence();
            }

            log.info("cpId:{} 订单号补全:{}", cpId, subTicketNo);
            RefundPo updatePo = new RefundPo();
            updatePo.setOrderId(orderId);
            updatePo.setCpId(cpId);
            updatePo.setOutTicketBillno(subTicketNo);
            refundPo.setOutTicketBillno(subTicketNo);
            refundService.updateRefund(updatePo, orderId, cpId);
        }

        String supplierType = refundPo.getSupplierType();
        supplierType = StringUtils.isBlank(supplierType) ? SupplierCode.KYFW : supplierType;
        log.info("供货商supplierType:{}{}", supplierType, SupplierCode.MESSAGE);

        //组装参数
        RefundReq refundReq = RefundReq.builder().orderId(refundPo.getOrderId()).cpId(refundPo.getCpId())
                .myOrderId(refundPo.getMyOrderId()).supperOrderId(refundPo.getSupplierOrderId())
                .sequence(refundPo.getOutTicketBillno())
                .username(refundPo.getAccountName()).password(refundPo.getAccountPwd())
                .sequence(refundPo.getOutTicketBillno()).name(refundPo.getUserName())
                .cardType(refundPo.getIdsType().toString()).cardNo(refundPo.getUserIds())
                .subSequence(refundPo.getSubOutTicketBillno()).build();

        if (supplierType.equals(SupplierCode.KYFW)) {
            robot(refundReq);
            return;
        }

        HthySyncDto hthySyncDto = hthyService.refund(refundReq);
        if (hthySyncDto.isSuccess()) {
            historyService.insertRefundLog(orderId, cpId, "请求已接收，等待退票结果");
        } else {
            historyService.insertRefundLog(orderId, cpId, hthySyncDto.getMessage());
            //转人工处理
            refundService.updateStatus(orderId, cpId, RefundStatus.REFUND_MANUAL);
        }
    }

    /**
     * 机器人
     * @param refundReq
     */
    private void robot(RefundReq refundReq) {
        String orderId = refundPo.getOrderId();
        String cpId = refundPo.getCpId();

        //获取机器人
        WorkerPo workerPo = workerService.available(WorkerType.REFUND);
        if (workerPo == null) {
            historyService.insertRefundLog(orderId, cpId, "无空闲机器人");
            refundService.updateStatus(orderId, cpId, RefundStatus.REFUND_MANUAL);
            return;
        }

        String publicIp = workerPo.getPublicIp();
        String url = UrlEnum.REFUND.format(publicIp);
        log.info("{}开始退票", url);
        historyService.insertRefundLog(orderId, cpId, String.format("%s开始退票", publicIp));

        //调用机器人
        String responseStr = new HttpUtil().doHttpPost(url, refundReq.toString(), 1000 * 60 * 2, true);
        log.info("退票机器人响应结果:{}", responseStr);
        //处理结果
        RefundResp refundResp = JSON.parseObject(responseStr, RefundResp.class);
        if (refundResp.isSuccess()) {
            String fee = refundResp.getFee();
            BigDecimal refundMoney = refundResp.getRefundMoney();
            String refundNo = refundResp.getRefundNo();
            if (StringUtils.isBlank(refundNo) || refundMoney == null) {
                historyService.insertRefundLog(orderId, cpId, "退票流水号为空");
                refundService.updateStatus(orderId, cpId, RefundStatus.REFUND_MANUAL);
                return;
            }
            String logInfo = "";
            log.info(logInfo = String.format("实际退款金额:%s, 手续费率:%s, 退票流水号:%s", refundMoney, fee, refundNo));
            historyService.insertRefundLog(orderId, cpId, logInfo);
            //更新数据
            RefundPo update = RefundPo.builder().cpId(cpId).refund12306Money(refundMoney).refundMoney(refundMoney)
                    .refundPercent(fee).orderStatus(RefundStatus.REFUND_AUTO_OVER).refund12306Seq(refundNo).build();
            refundService.updateRefund(update, orderId, cpId);

            //通知
            notifyService.updateRefund(orderId, cpId);
        } else {
            String message = refundResp.getMessage();
            //通过message，获取对应的枚举 TODO
            log.info("退票失败:{}", message);
            historyService.insertRefundLog(orderId, cpId, message);
            RefundPo update = RefundPo.builder().cpId(cpId).orderStatus(RefundStatus.REFUND_MANUAL).build();
            refundService.updateRefund(update, orderId, cpId);
        }

        //释放机器人
        workerService.updateStatus(workerPo.getWorkerId(), WorkerStatus.FREE);
    }
}
