package com.l9e.train.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class ConfigUtil {
	private static Properties prop;

	static{
		if(prop == null){
			prop = new Properties();
		}
		InputStream ins = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(ins);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得配置文件的值
	 * 
	 */
	public synchronized static String getValue(String value){
		return prop.getProperty(value);
	}

	/**
	 * 设置配置文件的值
	 */
	public synchronized static void setValue(String key, String value){
		prop.setProperty(key, value);
	}
	
	@Test
	public  void main() {
		System.err.println(ConfigUtil.getValue("jdRobotUrl"));
	}

}
