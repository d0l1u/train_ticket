package com.train.system.booking;

import com.train.system.booking.service.OrderService;
import com.train.system.booking.thread.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Main 程序入口
 *
 * @author taokai3
 * @date 2018/6/17
 */
public class Main {

    private Logger logger = LoggerFactory.getLogger(Main.class);

    @Resource
    private OrderService orderService;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @PostConstruct
    public void init() throws InterruptedException {
        logger.info("===============================");
        logger.info("||       应用启动 Begin       ||");
        logger.info("||---------------------------||");
        logger.info("||      启动 5 个生产线程      ||");
        logger.info("||---------------------------||");
        for (int i = 0; i < 1; i++) {
            Producer producer = new Producer();
            producer.setProducerId(i);
            taskExecutor.execute(producer);
            Thread.sleep(100);
        }
        logger.info("||        应用启动 End        ||");
        logger.info("===============================");
    }
}
