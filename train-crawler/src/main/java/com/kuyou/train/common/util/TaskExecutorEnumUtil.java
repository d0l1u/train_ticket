package com.kuyou.train.common.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * TaskExecutorEnum
 *
 * @author taokai3
 * @date 2018/12/12
 */
public enum TaskExecutorEnumUtil {

    INSTANCE;

    private ThreadPoolTaskExecutor taskExecutor;

    private TaskExecutorEnumUtil(){
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(30);
        taskExecutor.setKeepAliveSeconds(200);
        taskExecutor.setMaxPoolSize(60);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
    }

    public ThreadPoolTaskExecutor init(){
        return taskExecutor;
    }
}
