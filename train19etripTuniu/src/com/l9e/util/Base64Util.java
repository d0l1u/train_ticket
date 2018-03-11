package com.l9e.util;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

/**
 * base64加密，解密工具
 * 
 * @author licheng
 * 
 */
public class Base64Util {

	/**
	 * 字符串转base64串
	 * 
	 * @param input
	 * @param charsetName
	 * @return
	 */
	public static String encode(byte[] buff) {
		return Base64.encodeBase64URLSafeString(buff);
	}

	/**
	 * base64串转数据字符串
	 * 
	 * @param input
	 * @param charsetName
	 * @return
	 */
	public static byte[] decode(String input) {
		return Base64.decodeBase64(input);
	}

	// 将 s 进行 BASE64 编码
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
}
