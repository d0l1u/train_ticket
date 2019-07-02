package com.kuyou.train.job.stuck;

import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.RefundStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.RefundPo;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * RefundStuckJob
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Slf4j
@Component("refundStuckJob")
public class RefundStuckJob {

    @Resource
    protected RefundService refundService;

    @Resource
    private HistoryService historyService;

    @Value("${stuckMinutes}")
    private Integer stuckMinutes;


    @MDCLog
    @Scheduled(cron = "0/10 * 5-23 * * ?")
    public void resetStuckStatus() {
        log.info("********* 重置【退票】卡单 *********");

        List<RefundPo> list = refundService.selectStuck(RefundStatus.REFUND_ING, DateUtil.add(-stuckMinutes, TimeUnit.MINUTES));
        for (RefundPo refundPo : list) {
            String orderId = refundPo.getOrderId();
            String cpId = refundPo.getCpId();
            String status = refundPo.getOrderStatus();
            String supplierType = refundPo.getSupplierType();
            try {
                log.info("orderId:{}, cpId:{}超过{}分钟未响应结果，转人工处理", orderId, cpId, stuckMinutes);
                //重置状态,非航天华有订单才转人工处理
                refundService.updateStatusPre(orderId, cpId, RefundStatus.REFUND_MANUAL, status);
                historyService.insertRefundLog(orderId, cpId, String.format("超过%s分钟未响应结果，转人工处理", stuckMinutes));
            } catch (Exception e) {
                log.info("RefundStuckJob.resetStuckStatus 异常:{}", e.getClass().getSimpleName(), e);
            }
        }
    }
}
