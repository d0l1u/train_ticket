package com.l9e.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class CreateIDUtil {
	public static String SYSTEM_ID;//系统编号

	public static int no = 1;
	
	public static final Object LOCK = new Object();
	static{
		Properties prop = new Properties();    
        InputStream in = CreateIDUtil.class.getResourceAsStream("/config.properties");   
         try {    
             prop.load(in);    
             SYSTEM_ID = prop.getProperty("SYSTEM_ID").trim();    
             System.out.println("加载systemId："+SYSTEM_ID);
         } catch (IOException e) {    
             e.printStackTrace();    
         }    

	}

	/**
	 * 用此方法创建各表的ID
	 * @param prex 前缀名称
	 * @return
	 */
	public static String createID(String prex) {

		String FORMAT1 = "yyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
		String tranDate = sdf.format(new Date());
		int current = 1000;
		synchronized (LOCK) {
			if (no > 8999) {
				no = 1000;
			}
			current += no++;
		}

		StringBuffer sb = new StringBuffer(prex);
		sb.append(SYSTEM_ID);
		sb.append(tranDate).append(current);

		return sb.toString();
	}
	
	/**
	 * 用此方法创建用户密码随机六位数字
	 * @param
	 * @return
	 */
	public static String createSixNum() {
		String result = "";
		for(int i = 0 ; i < 6 ; i++){ 
		   int intval = (int)(Math.random() * 10);
		   result = intval+"" + result;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(result);

		return sb.toString();
	}
}
