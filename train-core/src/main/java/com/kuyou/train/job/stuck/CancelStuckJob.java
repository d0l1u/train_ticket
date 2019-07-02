package com.kuyou.train.job.stuck;

import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.ServicePo;
import com.kuyou.train.service.ChangeService;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * CancelStuckJob：取消卡状态
 *
 * @author taokai3
 * @date 2018/11/26
 */
@Slf4j
@Component("cancelStuckJob")
public class CancelStuckJob {

    @Resource
    private OrderService orderService;

    @Resource
    private ChangeService changeService;

    @Resource
    private HistoryService historyService;

    @Value("${stuckMinutes}")
    private Integer stuckMinutes;

    @MDCLog
    @Scheduled(cron = "0/10 * 5-23 * * ?")
    public void resetStuckStatus() {
        log.info("********* 重置【取消】卡单 *********");
        List<ServicePo> cancelPos = orderService.selectStuckService(OrderStatus.CANCEL_ING, ChangeStatus.CANCEL_ING, DateUtil.add(-stuckMinutes, TimeUnit.MINUTES));
        if (cancelPos.isEmpty()) {
            return;
        }

        for (ServicePo cancelPo : cancelPos) {
            String orderId = cancelPo.getOrderId();
            Integer changeId = cancelPo.getChangeId();
            boolean bookFlag = cancelPo.isBookFlag();
            String supplierType = cancelPo.getSupplierType();
            try {
                log.info("orderId:{}, changeId:{},  supplierType:{}, bookFlag:{}超过{}分钟未响应结果，转人工处理", orderId, changeId,
                        supplierType, bookFlag, stuckMinutes);
                //重置状态,非航天华有订单才转人工处理
                if (bookFlag) {
                    historyService.insertBookLog(orderId, String.format("超过%s分钟未响应结果，转人工处理", stuckMinutes));
                    orderService.updateStatus(orderId, OrderStatus.CANCEL_FAIL);
                } else {
                    historyService.insertChangeLog(orderId, changeId, String.format("超过%s分钟未响应结果，转人工处理", stuckMinutes));
                    changeService.updateStatusById(changeId, ChangeStatus.CANCEL_FAIL);
                }
            } catch (Exception e) {
                log.info("CancelStuckJob.resetStuckStatus 异常:{}", e.getClass().getSimpleName(), e);
            }
        }
    }
}
