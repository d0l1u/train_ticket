package com.l9e.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static String getOrderRedis(String channel){
		
		return "com.train.order.redis"+channel;
	}
	
	/**
	 * 依据正则表达式判断是否包含符合条件的字串
	 * @param input
	 * @param regex
	 * @return
	 */
	public static boolean containRegex(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		if(m.find())
			return true;
		return false;
	}
	
	/**
	 * 依据正则表达式查找字串
	 * @param input
	 * @param regex
	 * @return
	 */
	public static String findRegex(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		if(m.find())
			return m.group();
		return null;
	}
	
	public static void main(String[] args) {
		String input = "您填写的身份信息有误，未能通过国家身份信息管理权威部门核验，请检查您的姓名和身份证件号码填写是否正确。如有疑问，可致电12306客服咨询";
		String regex = "身份证件号码填写是否正确";
		boolean result = containRegex(input, regex);
		System.out.println(result);
	}
}

