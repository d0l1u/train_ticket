package com.l9e.util;

import org.springframework.web.context.ContextLoader;

/**   
 * @ClassName: ContextUtil   
 * @Description: TODO  
 * @author: taoka
 * @date: 2018年1月10日 上午11:35:38
 * @Copyright: 2018 www.19e.cn Inc. All rights reserved. 
 */
public class ContextUtil {

	public static <T> T getBean(String id, Class<T> clazz) {
		return ContextLoader.getCurrentWebApplicationContext().getBean(id, clazz);
	}
}