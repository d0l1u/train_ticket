package com.l9e.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StrUtil {
	public static int no = 1;

	public static final Object LOCK = new Object();

	/**
	 * 用此方法创建各表的ID
	 * @param prex 前缀名称
	 * @return
	 */
	public static String createID(String prex) {

		String FORMAT1 = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
		String tranDate = sdf.format(new Date());
		int current = 1000;
		synchronized (LOCK) {
			if (no > 9999) {
				no = 1000;
			}
			current += no++;
		}

		StringBuffer sb = new StringBuffer(prex);
		sb.append(tranDate).append(current);

		return sb.toString();
	}
	public static String randomNum(int length){
		Random rd = new Random(10);
		Double low=Math.pow(10,length);
		return (low.intValue()+rd.nextInt(9*low.intValue()))+"";
	}
	public static String dateformat(Date time,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}
	public static boolean isNotBlank(String str){
		
		return str != null && str.trim().length() > 0;
		
	}
	public static boolean isNotEmpty(String value) {
		if(value == null) {
			return false;
		}
		if(value.length() == 0){
			return false;
		}
		return true;
	}
	//判断是不是为空
	public static String getStr(String str){
		boolean flag = str != null && str.trim().length() > 0;
		if(flag){
			return str;
		}else{
			return null;
		}
	}
	
	public static String getObjectToStr(Object obj){
		
		String str = obj==null?null:obj.toString();
		return str;
	}
}

