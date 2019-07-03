package com.train.system.booking.util;

import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * @ClassName: PropertyUtil
 * @Description: 属性工具类
 * @author: taokai
 * @date: 2017年7月19日 下午7:10:17
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
public class PropertyUtil {
	private static Properties config = new Properties();
	private static final Logger LOGGER = Logger.getLogger(PropertyUtil.class);

	static {
		try {
			config.load(PropertyUtil.class.getResourceAsStream("/property/config.properties"));
		} catch (Exception e) {
			LOGGER.info("【系统异常】:加载config属性文件发生异常",e);
		}
	}
	
	public static String getValue(String key){
		String value = (String)config.get(key);
		return value;
	}
}
