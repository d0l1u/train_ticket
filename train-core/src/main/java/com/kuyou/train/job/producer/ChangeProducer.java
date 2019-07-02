package com.kuyou.train.job.producer;

import com.google.common.collect.Lists;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.entity.po.ChangePo;
import com.kuyou.train.service.ChangeService;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.thread.producer.ChangeProducerThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * ChangeConsumer：改签占位消费
 *
 * @author taokai3
 * @date 2018/10/26
 */
@Slf4j
@Component("changeProducer")
public class ChangeProducer {

    @Resource
    protected HistoryService historyService;

    @Resource
    protected ThreadPoolTaskExecutor taskExecutor;

    @Value("${producer.changeMax}")
    private Integer changeMax;

    @Resource
    private ChangeService changeService;

    @MDCLog
    @Scheduled(cron = "0/3 * 5-23 * * ?")
    public void consumer() {
        log.info("********* 开始改签占位 *********");
        //获取状态为 的改签订单
        List<ChangePo> changes = changeService.selectByStatus(Lists.newArrayList(ChangeStatus.CHANGE_WAIT), changeMax);
        if (changes.isEmpty()) {
            log.info("改签占位 changePos 为空，不做处理");
            return;
        }

        for (ChangePo changePo : changes) {
            Integer changeId = changePo.getChangeId();
            String orderId = changePo.getOrderId();
            try {
                log.info("修改改签单状态为[12:改签中]changeId:{}", changeId);
                int result = changeService.updateStatusPre(changeId, changePo.getChangeStatus(), ChangeStatus.CHANGE_ING);
                if (result > 0) {
                    historyService.insertChangeLog(orderId, changeId, "开始改签占位");
                    //将需要处理的处理的改签单通过线程池处理
                    taskExecutor.execute(new ChangeProducerThread(changePo));
                }
            } catch (Exception e) {
                log.info("ChangeProducer.consumer异常:{}", e.getClass().getSimpleName(), e);
            }
        }
    }
}
