package com.kuyou.train.common;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * ScheduleConfig
 *
 * @author taokai3
 * @date 2018/12/19
 */
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(new ScheduledThreadPoolExecutor(10));
    }
}
