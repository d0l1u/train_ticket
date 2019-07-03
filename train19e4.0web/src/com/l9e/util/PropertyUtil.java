package com.l9e.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @Description: 读取url配置文件 
 */
public class PropertyUtil {
	
	private static Properties ctrip = new Properties(); 
	
	static{
		try {
			ctrip.load(PropertyUtil.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("config.properties配置文件或者路径不存在");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("config.properties配置文件读取失败");
		} 
		 
	}
	 
	public static String getCtripValue(String key){
		return (String) ctrip.get(key);
	}
	
	public static void main(String[] args) {
		System.out.println(PropertyUtil.getCtripValue("redis.port"));
	}
	 
}
