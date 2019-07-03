package com.l9e.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtil {
	private static Properties prop;
	private static final Logger logger = Logger.getLogger(ConfigUtil.class);

	static {
		prop = new Properties();
		try {
			InputStream ins = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(ins);
		} catch (Exception e) {
			logger.info(e);
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
