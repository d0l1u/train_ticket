package com.kuyou.train.job;

import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.thread.producer.ChangeProducerThread;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * ChangeJobTest
 *
 * @author taokai3
 * @date 2018/11/2
 */
public class ChangeJobTest extends MvcBaseTest {

    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    @Test
    public void changeThread() throws InterruptedException {
        ChangeProducerThread changeThread = new ChangeProducerThread();
        ChangeProducerThread changeThread2 = new ChangeProducerThread();
        ChangeProducerThread changeThread3 = new ChangeProducerThread();
        taskExecutor.execute(changeThread);

        Thread.sleep(1000);
    }
}
