package com.kuyou.train.job.producer;

import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ChangePayProducer
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Slf4j
@Component("payProducer")
public class PayProducer {

    @Value("${producer.bookMax}")
    private int bookMax;

    @Resource
    private OrderService orderService;

    @Resource
    protected ThreadPoolTaskExecutor taskExecutor;

    @Resource
    private HistoryService historyService;

    @MDCLog
    @Scheduled(cron = "* * 5-23 * * ?")
    public void consumer() {
        log.info("********* PayProducer Start *********");


    }
}
