package com.kuyou.train.job.consumer;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.entity.resp.ChangeOrderResp;
import com.kuyou.train.thread.consumer.ChangeConsumerThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ChangeConsumerJob
 *
 * @author taokai3
 * @date 2018/10/26
 */
@Slf4j
@Component("changeOrderConsumer")
public class ChangeOrderConsumer {

    @Resource
    protected ThreadPoolTaskExecutor taskExecutor;

    @Resource
    protected JedisClient orderJedisClient;

    @MDCLog
    @Scheduled(cron = "0/2 * 5-23 * * ?")
    public void consumer() {

        String rpop = orderJedisClient.rpop(KeyConstant.CHANGE_RESP);
        if (StringUtils.isBlank(rpop)) {
            return;
        }
        log.info("改签占位结果:{}", rpop);
        taskExecutor.execute(new ChangeConsumerThread(JSON.parseObject(rpop, ChangeOrderResp.class)));
    }
}
