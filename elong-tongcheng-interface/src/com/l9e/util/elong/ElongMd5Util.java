package com.l9e.util.elong;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 针对elong 的MD5加密方式
 * @author liuyi02
 * 加密规则32位utf-8为大写输出
 * */
public class ElongMd5Util {
	public static String getParam(Map<String,String> params,String sign_key){
		 List keys = new ArrayList(params.keySet());
	     Collections.sort(keys);
	     StringBuffer paramStr = new StringBuffer();
	     for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			if (value == null || value.trim().length() == 0) {
				continue;
			}
			paramStr.append(key +"="+ value+"&");
		}
	     String returnStr= paramStr.substring(0, paramStr.lastIndexOf("&"))+sign_key;
	     return returnStr;
	}
	public static String md5_32(String paramStr,String charSet){
		StringBuilder result=new StringBuilder();
		try {
			MessageDigest m=MessageDigest.getInstance("MD5");
			m.update(paramStr.getBytes(charSet));
			byte[] rB=m.digest();
			int num=0;
			for(int i=0;i<rB.length;i++){
				num=rB[i];
				if(num<0) num+=256;
				result.append(Integer.toHexString(num>>>4));
				result.append(Integer.toHexString(num%16));
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result.toString().toUpperCase();
	}
	public static String md5_16(String paramStr,String charSet){
		return md5_32(paramStr,charSet).substring(8, 24);
	}
	
	public static String md5_32(Map<String,String> params,String sign_key,String charSet){
		 return md5_32(getParam(params,sign_key),charSet);
	}
	
	public static String md5_16(Map<String,String> params,String sign_key,String charSet){
		 return md5_32(params,sign_key,charSet).substring(8, 24);
	}
	
	public static void main(String[] args) {
		Map<String ,String> params=new HashMap<String, String>();
		params.put("name", "zs");
		params.put("age", "12");
		System.out.println(getParam(params,"sign_key"));
	}
}

