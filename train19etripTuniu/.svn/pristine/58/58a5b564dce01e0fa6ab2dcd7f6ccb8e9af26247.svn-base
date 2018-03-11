package com.l9e.util;


/**
 * 数据加密
 * @author licheng
 *
 */
public class EncryptUtil {

	/**
	 * data数据加密
	 * @param data
	 * @param key
	 * @return
	 */
	public static String encode(String data, String key, String charset) {
		String result = null;
		try {
			byte[] dataBuff = data.getBytes(charset);
			byte[] keyBuff = key.getBytes(charset);
			byte[] desBuff = DESUtil.encrypt(dataBuff, keyBuff);
			result = Base64Util.encode(desBuff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * data数据加密
	 * @param data
	 * @param key
	 * @return
	 */
	public static String decode(String data, String key, String charset) {
		String result = null;
		try {
			byte[] desBuff = Base64Util.decode(data);
			byte[] keyBuff = key.getBytes(charset);
			byte[] dataBuff = DESUtil.decrypt(desBuff, keyBuff);
			result = new String(dataBuff, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
