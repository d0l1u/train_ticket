package com.kuyou.train.job;

import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.job.producer.ChangeProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * ChangeProducerTest
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Slf4j
public class ChangeProducerTest extends MvcBaseTest {

    @Resource
    ChangeProducer changeProducer;

    @Test
    public void consumer() {
        changeProducer.consumer();
        while (true) {

        }
    }

}
