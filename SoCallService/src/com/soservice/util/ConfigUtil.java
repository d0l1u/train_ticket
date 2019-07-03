package com.soservice.util;

import java.io.InputStream;
import java.util.Properties;


/**
 * 
* @类 ConfigUtil
* @包 com.speed.util; 
* @描述: 加载配置文件工具类
* @作者：liufei   
* @创建时间 2011-4-7 下午04:44:12 
* @版本 V1.0
 */
public class ConfigUtil
{
	private static Properties props = new Properties();
	private static final String CONSTANT_FILE = "config.properties";
	private static InputStream ips = null;
	
	static{
		ips = ConfigUtil.class.getClassLoader().getResourceAsStream(CONSTANT_FILE);
		try{
			props.load(ips);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据文件中的键得到对应的值
	 * @param key
	 * @return
	 */
	public synchronized static String getProperty(String key){
		return props.getProperty(key);
	}

	/**
	 * 设置配置文件的值
	 * @param key
	 * @param value
	 */
	public synchronized static void setValue(String key, String value) {
		props.setProperty(key, value);
	}
	
}
