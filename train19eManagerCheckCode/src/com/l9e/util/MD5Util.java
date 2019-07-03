package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * @author liuyi02
 * 
 * */
public class MD5Util {
	
	
	public static String myMd5(String md5_str,String charset){
		try {
			MessageDigest messDigest=MessageDigest.getInstance("MD5");
			messDigest.update(md5_str.getBytes(charset));
			byte[] digBytes=messDigest.digest();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<digBytes.length;i++){
				int num=digBytes[i];
				if(num<0) num+=256;
				/*sb.append(INDEX_CHAR[num >>> 4]);
				sb.append(INDEX_CHAR[num % 16]);*/
				sb.append(Integer.toHexString(num>>>4));
				sb.append(Integer.toHexString(num%16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
