package com.kuyou.train.job.consumer;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.entity.resp.BookOrderResp;
import com.kuyou.train.thread.consumer.BookConsumerThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * BookRespJob
 *
 * @author taokai3
 * @date 2018/10/26
 */
@Slf4j
@Component("bookConsumerJob")
public class BookConsumerJob {

    @Resource
    protected ThreadPoolTaskExecutor taskExecutor;

    @Resource
    protected JedisClient orderJedisClient;


    @MDCLog
    @Scheduled(cron = "0/2 * 5-23 * * ?")
    public void consumer() {
        int index = 5;
        while (index-- > 0){
            String rpop = orderJedisClient.rpop(KeyConstant.BOOK_RESP);
            if (StringUtils.isBlank(rpop)) {
                return;
            }
            log.info("预订占位结果:{}", rpop);
            taskExecutor.execute(new BookConsumerThread(JSON.parseObject(rpop, BookOrderResp.class)));
        }
    }
}
