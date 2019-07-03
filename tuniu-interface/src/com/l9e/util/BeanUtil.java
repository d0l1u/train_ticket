package com.l9e.util;

import org.springframework.web.context.ContextLoader;

/**
 * spring管理对象工具
 * @author licheng
 *
 */
public class BeanUtil {
	
	public static Object getBean(String beanName) {
		return ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
	}
}
