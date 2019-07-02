package com.kuyou.train.job.stuck;

import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.WorkerStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.WorkerPo;
import com.kuyou.train.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * WorkerStuckJob
 *
 * @author taokai3
 * @date 2018/11/24
 */
@Slf4j
@Component("workerStuckJob")
public class WorkerStuckJob {

    @Resource
    protected WorkerService workerService;

    @Value("${stuckMinutes}")
    private Integer stuckMinutes;


    @MDCLog
    @Scheduled(cron = "0/10 * 5-23 * * ?")
    public void resetStuckStatus() {
        log.info("********* 重置【机器】卡单 *********");

        List<WorkerPo> list = workerService
                .selectStuck(WorkerStatus.ING, DateUtil.add(-stuckMinutes, TimeUnit.MINUTES));
        for (WorkerPo workerPo : list) {
            Integer workerId = workerPo.getWorkerId();
            try {
                log.info("workerId:{}, 超过{}分钟未响应结果，转人工处理", workerId, stuckMinutes);
                //重置状态
                workerService.updateStatus(workerId, WorkerStatus.FREE);
            } catch (Exception e) {
                log.info("WorkerStuckJob.resetStuckStatus 异常:{}", e.getClass().getSimpleName(), e);
            }
        }
    }
}
