package com.l9e.train.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5
 * 
 * @author Wanggq
 *
 */
public class MD5 {
	private static Logger logger = LoggerFactory.getLogger(MD5.class);

	/**
	 * �?strSrc 进行UTF-8编码的MD5散列
	 * 
	 * @param strSrc
	 * @return 16进制串，如果byte转化�?6进制串，只是1位串，需要在前面追加0,保持2�?6进制�?
	 * @throws UnsupportedEncodingException
	 *             NoSuchAlgorithmException
	 */
	public final static String getMd5_UTF8(String strSrc) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			byte[] strTemp = strSrc.getBytes("UTF-8");
			logger.debug("编码格式是：" + "UTF-8");
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
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;

	}

	/**
	 * �?strSrc 进行GBK编码的MD5散列
	 * 
	 * @param strSrc
	 * @return 16进制串，如果byte转化�?6进制串，只是1位串，需要在前面追加0,保持2�?6进制�?
	 * @throws UnsupportedEncodingException
	 *             NoSuchAlgorithmException
	 */
	public final static String getMd5_GBK(String strSrc) throws Exception {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		byte[] strTemp = strSrc.getBytes("GBK");
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

	public static String getSign(Map params, String privateKey) {
		try {
			List keys = new ArrayList(params.keySet());
			Collections.sort(keys);
			StringBuffer prestr = new StringBuffer();
			for (int i = 0; i < keys.size(); i++) {
				String key = (String) keys.get(i);
				String value = (String) params.get(key);
				if (value == null || value.trim().length() == 0) {
					continue;
				}
				// prestr.append((i == 0 ? "" : "&") + key + "=" +
				// URLEncoder.encode(value, "UTF-8"));
				prestr.append((i == 0 ? "" : "") + key + value);
				// prestr.append((i == 0 ? "" : "") + key +
				// URLEncoder.encode(value, "UTF-8"));
			}
			// logger.info("加密前["+prestr +"&"+ privateKey+"]");
			logger.info("加密前\n[" + prestr + privateKey + "]");
			// String md5str = MD5.getMd5(prestr +"&"+ privateKey);
			String md5str = MD5.getMd5_UTF8(prestr + privateKey);
			logger.info("加密后\n[" + md5str + "]");
			return md5str;
		} catch (Exception e) {
			logger.error("获取签名失败！");
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		String mi;
		// String s =
		// "version_id=2.00&merchant_id=2008&order_date=20070201&order_id=577120070201095014&amount=10&currency=RMB&pay_sq=CA200702010942000243&pay_date=20070201094202&pc_id=JXPU00010002&result=Y&merchant_key=asdfc@97bxlj23lj5klkefnv";

		try {
			mi = MD5.getMd5_UTF8("hforderno=qwqw我们&ip=192.168.0.1&ordersource=228&paySq=a&reqIspid=1&reqProvinceid=1&returnType=0&scorderno=8086|111111");
			System.out.println("mi:" + mi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
