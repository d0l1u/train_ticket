package com.l9e.train.util;

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
			if (no > 8999) {
				no = 1000;
			}
			current += no++;
		}

		StringBuffer sb = new StringBuffer(prex);
		sb.append(tranDate).append(current);

		return sb.toString();
	}
	
	public static String randomNum(int length){
		String s = "";
		 while(s.length()<length)
		  s+=(int)(Math.random()*10);
		return s;
	}
	

	public static String dateformat(Date time,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}
	public static boolean isNotBlank(String str){
		
		return str != null && str.trim().length() > 0;
		
	}
	
	public static String getObjectToStr(Object obj){
		
		String str = obj==null?null:obj.toString();
		return str;
	}
	
	
	/**
	 * 
	 * @Title: getRandomString 
	 * @Description: TODO(java 随即生成字符) 
	 * @param @param length
	 * @param @return    
	 * @return String   
	 * @throws
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 } 
	
	/**
	 * 
	 * @Title: buildRandom 
	 * @Description: TODO(随机生成数字) 
	 * @param @param length
	 * @param @return    
	 * @return int   
	 * @throws
	 */
	public static Integer buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
             random = random + 0.1;
        } for (int i = 0; i < length; i++) {
             num = num * 10;
        }
        return (int) ((random * num));
 }
	public static void main(String[] args) {
		System.out.println(buildRandom(5));
	}

	public static String isNulltoStr(Object obj) {
		String str = obj==null?"":obj.toString();
		return str;
	}
	
	/**
	 * 获取账号queue
	 * @param channel
	 * @return
	 */
	public static String getOrderQueue(String channel){
		
		return "com.train.order."+channel;
	}
	
	

	/**
	 * 获取账号queue
	 * @param channel
	 * @return
	 */
	public static String getPayQueue(String channel){
		
		return "com.train.pay."+channel;
	}
}

