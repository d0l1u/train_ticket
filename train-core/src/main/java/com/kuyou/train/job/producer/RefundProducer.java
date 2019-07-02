package com.kuyou.train.job.producer;

import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.RefundStatus;
import com.kuyou.train.entity.po.RefundPo;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.RefundService;
import com.kuyou.train.thread.producer.RefundProducerThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * RefundProducer
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Slf4j
@Component("refundProducer")
public class RefundProducer {

    @Resource
    private RefundService refundService;

    @Resource
    protected ThreadPoolTaskExecutor taskExecutor;


    @Resource
    protected HistoryService historyService;

    @Value("${producer.refundMax}")
    private int refundMax;

    @MDCLog
    @Scheduled(cron = "0/5 * 5-23 * * ?")
    public void consumer() {
        log.info("********* 开始退票 *********");
        List<RefundPo> refundPos = refundService.selectWaitRefund(RefundStatus.REFUND_WAIT, refundMax);
        if (refundPos.isEmpty()) {
            log.info("退票 refundPos 为空，不做处理");
            return;
        }

        for (RefundPo refundPo : refundPos) {
            String orderId = refundPo.getOrderId();
            String cpId = refundPo.getCpId();
            try {
                log.info("修改订单状态为[06:退票中]orderId:{}", orderId);
                int result = refundService
                        .updateStatusPre(orderId, cpId, RefundStatus.REFUND_ING, refundPo.getOrderStatus());
                if (result > 0) {
                    historyService.insertRefundLog(orderId, cpId, "开始线上退票");
                    //将需要处理的处理的改签单通过线程池处理
                    taskExecutor.execute(new RefundProducerThread(refundPo));
                }
            } catch (Exception e) {
                log.info("RefundProducer.consumer异常:{}", e.getClass().getSimpleName(), e);
            }
        }
    }
}
