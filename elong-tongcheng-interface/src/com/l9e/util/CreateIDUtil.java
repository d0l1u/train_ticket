package com.l9e.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import com.l9e.util.elong.ElongMd5Util;

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
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(UUID.randomUUID().toString())
			.append(Math.random())
			.append(System.currentTimeMillis());
		
		String md5_16 = ElongMd5Util.md5_16(builder.toString(), "UTF-8");
		builder.setLength(0);
		builder.append(prex)
			.append(md5_16);
		return builder.toString();

	}
	
	
	
	/**
	 * 用此方法创建各表的ID
	 * @param prex 前缀名称
	 * @return
	 */
	public static String returnLock(String prex) {

		
		int current = 100;
		synchronized (LOCK) {
			if (no > 999) {
				no = 100;
			}
			current += no++;
		}

		StringBuffer sb = new StringBuffer(prex);
		sb.append(current);

		return sb.toString();
	}
	
	
	/**
	 * 用此方法创建用户密码随机六位字母
	 * @param prex 前缀名称
	 * @return
	 */
	public static String createPwd() {
		String result = "";
		for(int i = 0 ; i < 6 ; i++){ 
		   int intval = (int)(Math.random() * 26 + 97);
		   result = result + (char)intval;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(result);

		return sb.toString();
	}
	
}
