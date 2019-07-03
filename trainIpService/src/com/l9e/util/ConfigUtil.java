package com.l9e.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
	private static Properties props = new Properties();
	private static final String CONSTANT_FILE = "config.properties";
	private static InputStream ips = null;
	static {
		ips = ConfigUtil.class.getClassLoader().getResourceAsStream(
				CONSTANT_FILE);
		try {
			props.load(ips);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}
	
//	public static void main(String[] args){
//		System.out.println(ConfigUtil.getProperty("getIpById"));
//	}
}
