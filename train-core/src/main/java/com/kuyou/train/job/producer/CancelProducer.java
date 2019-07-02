package com.kuyou.train.job.producer;

import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.entity.po.ServicePo;
import com.kuyou.train.service.ChangeService;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.OrderService;
import com.kuyou.train.thread.producer.Cancel4BookProducerThread;
import com.kuyou.train.thread.producer.Cancel4ChangeProducerThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * CancelProducer：占位取消
 *
 * @author taokai3
 * @date 2018/11/10
 */
@Slf4j
@Component("cancelProducer")
public class CancelProducer {

    @Resource
    protected ThreadPoolTaskExecutor taskExecutor;

    @Resource
    protected HistoryService historyService;

    @Resource
    private OrderService orderService;

    @Resource
    private ChangeService changeService;

    @Value("${producer.cancelMax}")
    private Integer cancelMax;

    @MDCLog
    @Scheduled(cron = "0/5 * 5-23 * * ?")
    public void consumer() {
        log.info("********* 开始取消 *********");
        List<ServicePo> servicePos = orderService
                .selectWaitService(OrderStatus.CANCEL_INIT, ChangeStatus.CANCEL_INIT, cancelMax);
        if (servicePos.isEmpty()) {
            log.info("取消 servicePos 为空，不做处理");
            return;
        }

        for (ServicePo servicePo : servicePos) {
            String orderId = servicePo.getOrderId();
            Integer changeId = servicePo.getChangeId();
            boolean bookFlag = servicePo.isBookFlag();
            try {
                if (bookFlag) {
                    log.info("修改订单状态为[83:预订取消中]orderId:{}", orderId);
                    int res = orderService.updateStatusPre(orderId, OrderStatus.CANCEL_INIT, OrderStatus.CANCEL_ING);
                    if (res > 0) {
                        historyService.insertBookLog(orderId, "开始占位取消");
                        //将需要处理的处理的改签单通过线程池处理
                        taskExecutor.execute(new Cancel4BookProducerThread(servicePo));
                    }
                } else {
                    log.info("修改订单状态为[22:改签取消中]changeId:{}", orderId);
                    int res = changeService
                            .updateStatusPre(changeId, ChangeStatus.CANCEL_INIT, ChangeStatus.CANCEL_ING);
                    if (res > 0) {
                        historyService.insertChangeLog(orderId, changeId, "开始改签占位取消");
                        //将需要处理的处理的改签单通过线程池处理
                        taskExecutor.execute(new Cancel4ChangeProducerThread(servicePo));
                    }
                }
            } catch (Exception e) {
                log.info("CancelProducer.consumer异常:{}", e.getClass().getSimpleName(), e);
            }
        }
    }
}
