package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * MD5加密算法
 */
public class Md5Encrypt {
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * 
	 * @return 密文
	 */
	public static String md5(String text, String charset) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes(charset));

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException("System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}
	 /**
	  * MD5消息摘要
	  * @param data 待处理的消息 明文
	  * @return byte[] 消息摘要 密文
	  * @throws Exception
	  */
	 public static String encodeMD5Hex(String data){
	    return DigestUtils.md5Hex(data);
	 }
	
	 //19payMd5加密
	 public static String getKeyedDigestFor19Pay(String strSrc, String key, String charset) {
         try {
             MessageDigest md5 = MessageDigest.getInstance("MD5");
             md5.update(strSrc.getBytes(charset));

             String result = "";
             byte[] temp;
             temp = md5.digest(key.getBytes(charset));

             for (int i = 0; i < temp.length; i++) {
                 result += Integer.toHexString(
                         (0x000000ff & temp[i]) | 0xffffff00).substring(6);
             }
             return result;
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null;
     }
}
