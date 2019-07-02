package com.kuyou.train.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringContextUtil
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        log.info("###################################");
        log.info("##    init ApplicationContext    ##");
        log.info("###################################");

        applicationContext = context;
    }

    public static <T> T getBean(String id, Class<T> clazz) {
        return applicationContext.getBean(id, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}