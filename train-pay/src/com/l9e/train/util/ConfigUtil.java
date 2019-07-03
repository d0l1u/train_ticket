package com.l9e.train.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {

	private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

	private static Properties prop;
	static {
		prop = new Properties();
		try {
			InputStream ins = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(ins);
		} catch (Exception e) {
			logger.info("Exception", e);
		}
	}

	/**
	 * 
	 * 取得配置文件的值
	 * 
	 */

	public synchronized static String getValue(String value) {
		return prop.getProperty(value);
	}

	/**
	 * 
	 * 
	 * 
	 * 设置配置文件的值
	 * 
	 */

	public synchronized static void setValue(String key, String value) {
		prop.setProperty(key, value);

	}

	public static void main(String[] args) {
		String value = ConfigUtil.getValue("queryPrice");
		System.err.println(value);
	}
}
