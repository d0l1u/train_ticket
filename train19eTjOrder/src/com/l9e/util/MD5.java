package com.l9e.util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.log4j.Logger;


/**
 * 
* @类 MD5
* @包 com.jiexun.ticket.util 
* @描述: MD5加密工具类 
* @作者：songql   
* @创建时间 2011-4-7 下午04:17:32 
* @版本 V1.0
 */
public class MD5 {
	static Logger logger = Logger.getLogger(MD5.class);
	/**
	 * 对 strSrc 进行GBK编码的MD5散列
	 * @param strSrc
	 * @return 16进制串，如果byte转化为16进制串，只是1位串，需要在前面追加0,保持2位16进制串 
	 * @throws  UnsupportedEncodingException NoSuchAlgorithmException
	 */
	public final static String getMd5(String strSrc) throws Exception {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		
			byte[] strTemp = strSrc.getBytes("UTF-8");
			logger.debug("编码格式是：UTF-8");
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
	 * 今日同步票号验证签名
	 * @param strSrc
	 * @return
	 * @throws Exception
	 */
	public final static String getMd5GBK(String strSrc) throws Exception {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		
			byte[] strTemp = strSrc.getBytes("GB2312");
			logger.debug("编码格式是：GB2312");
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
    //    String s = "version_id=2.00&merchant_id=2008&order_date=20070201&order_id=577120070201095014&amount=10&currency=RMB&pay_sq=CA200702010942000243&pay_date=20070201094202&pc_id=JXPU00010002&result=Y&merchant_key=asdfc@97bxlj23lj5klkefnv";
	
		try {
			mi=MD5.getMd5("hforderno=qwqw我们&ip=192.168.0.1&ordersource=228&paySq=a&reqIspid=1&reqProvinceid=1&returnType=0&scorderno=8086|111111");          
			System.out.println("mi:"+mi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
