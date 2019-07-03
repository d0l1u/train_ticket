package com.l9e.train.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

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
		String input = "常用联系人上限调整为15人";
		input = "waitTime为100,请稍后重试";
		input = "手机核验";
		String regex = "联系人.*上限";
		regex = "waitTime";
		regex = "手机.*核验";
		boolean result = containRegex(input, regex);
//		String result = findRegex(input, regex);
		System.out.println(result);
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
}
