package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	/**
	 * 对 strSrc 进行GBK编码的MD5散列
	 * @param strSrc
	 * @return 16进制串，如果byte转化为16进制串，只是1位串，需要在前面追加0,保持2位16进制串 
	 * @throws  UnsupportedEncodingException NoSuchAlgorithmException
	 */
	public final static String getMd5(String strSrc) throws Exception {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		
			byte[] strTemp = strSrc.getBytes("utf-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
	
	}
	
	/**
	 * 对 strSrc按制定编码方式进行编码MD5散列
	 * 
	 * @param strSrc
	 * @param encode
	 * @return 16进制串，如果byte转化为16进制串，只是1位串，需要在前面追加0,保持2位16进制串 
	 * @throws  UnsupportedEncodingException NoSuchAlgorithmException
	 */
	public final static String getMd5(String strSrc, String encode) throws Exception {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		
			byte[] strTemp = strSrc.getBytes(encode);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
	}

	public static void main(String[] args) {
		String mi;
		try {
			String bb = "shopid=110&billno=AG011001251157028628&billdate=20100125&meid=002346&gameid=GAME1953&regionid=3443&serverid=-1&chargeaccount=zhangrui&timestamp=20100125115708|b8ede87ea11cdc4451520eb0c2074c4b";
			mi = MD5.getMd5(bb,"GBK");
			System.out.println("mi:"+mi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] getKeyedDigest(byte[] buffer, byte[] key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            return md5.digest(key);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}
