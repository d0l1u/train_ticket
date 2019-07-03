package com.l9e.train.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring容器持久类
 * 
 * @author licheng
 *
 */
public class SpringContext {

	private static Logger logger = LoggerFactory.getLogger(SpringContext.class);

	private static ApplicationContext context;

	static {
		try {
			context = new ClassPathXmlApplicationContext("classpath:context/applicationContext.xml");
		} catch (Exception e) {
			logger.info("### 【Loading SpringContext Exception:】{}", e.getClass().getSimpleName(), e);
		}
	}

	private SpringContext() {
		super();
	}

	/**
	 * 从spring容器根据id获取对象
	 * 
	 * @param beanId
	 * @return
	 */
	public static Object getBean(String beanId) {
		if (context == null) {
			return null;
		}
		return context.getBean(beanId);
	}

	/**
	 * 从spring容器根据类型获取对象
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		if (context == null) {
			return null;
		}
		return context.getBean(clazz);
	}

	/**
	 * 从spring容器根据id和类型获取对象
	 * 
	 * @param <T>
	 * @param beanId
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(String beanId, Class<T> clazz) {
		if (context == null) {
			return null;
		}
		return context.getBean(beanId, clazz);
	}

}
