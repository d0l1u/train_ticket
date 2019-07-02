package com.train.system.center.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringContextUtil
 *
 * @author taokai3
 * @date 2018/11/5
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(SpringContextUtil.class);

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