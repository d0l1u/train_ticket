package com.l9e.train.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
	private static Properties prop;

	public ConfigUtil()

	{

		this("config.properties");

	}

	public ConfigUtil(String propFile)

	{

		prop = new Properties();

		try

		{

			InputStream ins = this.getClass().getClassLoader()

			.getResourceAsStream(propFile);

			prop.load(ins);

		}

		catch (Exception e)

		{

		}

	}

	/**
	 * 
	 * 取得配置文件的值
	 * 
	 */

	public synchronized static String getValue(String value)

	{

		return prop.getProperty(value);

	}

	/**
	 * 
	 * 
	 * 
	 * 设置配置文件的值
	 * 
	 */

	public synchronized static void setValue(String key, String value)

	{

		prop.setProperty(key, value);

	}

}
