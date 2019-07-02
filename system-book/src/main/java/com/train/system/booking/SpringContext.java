package com.train.system.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**   
 * @ClassName: SpringContext
 * @Description: TODO  
 * @author: taoka
 * @date: 2018年6月14日 下午6:23:35
 * @Copyright: 2018 www.19e.cn Inc. All rights reserved. 
 */
@Component
public class SpringContext implements ApplicationContextAware{
	private Logger logger = LoggerFactory.getLogger(SpringContext.class);

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		logger.info("###################################");
		logger.info("##    init ApplicationContext    ##");
		logger.info("###################################");
		applicationContext = context;
	}
	
	public static <T> T getBean(String id, Class<T> clazz) {
		return applicationContext.getBean(id, clazz);
	} 
}
