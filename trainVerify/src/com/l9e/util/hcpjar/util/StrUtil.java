package com.l9e.util.hcpjar.util;

import java.util.Random;

public class StrUtil {
	public static boolean isNotEmpty(String str){
		if(str!=null&&str.trim().length()>0){
			return true;
		}else{
			return false;
		}
		
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
