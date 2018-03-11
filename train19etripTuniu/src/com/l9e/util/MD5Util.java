package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * MD5加密工具
 * @author licheng
 *
 */
public class MD5Util {
	
	public static void main(String[] args) {
		String uuid = UUID.randomUUID().toString();
		String md5 = md5(uuid, "UTF-8");
		System.out.println(md5);
	}

	public static String md5(String input, String charsetName) {
		StringBuilder result=new StringBuilder();
		try {
			MessageDigest m=MessageDigest.getInstance("MD5");
			m.update(input.getBytes(charsetName));
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
		return result.toString();
	}
	
	public static String md5_16(String input,String charsetName){
		return md5(input,charsetName).substring(8, 24);
	}
	
}
