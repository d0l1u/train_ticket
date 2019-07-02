package com.kuyou.train.job.stuck;

import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.ChangePo;
import com.kuyou.train.service.ChangeService;
import com.kuyou.train.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ChangeStatusJob：预订占位卡单处理
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Slf4j
@Component("changeStuckJob")
public class ChangeStuckJob {

    @Resource
    protected ChangeService changeService;

    @Resource
    private HistoryService historyService;

    @Value("${stuckMinutes}")
    private Integer stuckMinutes;

    @MDCLog
    @Scheduled(cron = "0/10 * 5-23 * * ?")
    public void resetStuckStatus() {
        log.info("********* 重置【改签】卡单 *********");
        List<ChangePo> changePos = changeService.selectStuck(ChangeStatus.CHANGE_ING, Lists.newArrayList(SupplierCode.KYFW, SupplierCode.HTHY), DateUtil.add(-stuckMinutes, TimeUnit.MINUTES));
        if (changePos.isEmpty()) {
            return;
        }
        for (ChangePo changePo : changePos) {
            Integer changeId = changePo.getChangeId();
            String orderId = changePo.getOrderId();
            String changeStatus = changePo.getChangeStatus();
            String supplierType = changePo.getSupplierType();
            try {
                log.info("changeId:{}, orderId:{}超过{}分钟未响应结果，转人工处理", changeId, orderId, stuckMinutes);
                //重置状态,非航天华有订单才转人工处理
                changeService.updateStatusPre(changeId, changeStatus, ChangeStatus.CHANGE_MANAUL);
                historyService.insertChangeLog(orderId, changeId, String.format("超过%s分钟未响应结果，转人工处理", stuckMinutes));
            } catch (Exception e) {
                log.info("ChangeStuckJob.resetStuckStatus 异常:{}", e.getClass().getSimpleName(), e);
            }
        }
    }
}
