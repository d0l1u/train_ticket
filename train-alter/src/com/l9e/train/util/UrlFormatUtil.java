package com.l9e.train.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * URL组装工具类
 * @author yuzj
 * @since 2009-5-23
 */
public class UrlFormatUtil {
	private static Logger logger = LoggerFactory.getLogger(UrlFormatUtil.class);	
	
	private static final String UTF_8 = "UTF-8";

	public static final String GBK = "GBK";
	
	public static final String MD5 = "MD5";
	
	public static final String DSA = "DSA";
	
	@SuppressWarnings("unchecked")
	public static String CreateUrl(String gateway,Map params, String privateKey) throws Exception{
		return UrlFormatUtil.CreateUrl(gateway, params, privateKey, UTF_8);
	}
	
	@SuppressWarnings("unchecked")
	public static String CreateUrl(String gateway,Map params, String privateKey,String charSet) throws Exception{
		String sign = getSign(params, privateKey);
		String parameter = "";
        parameter = parameter + gateway;
        if(StringUtils.isNotEmpty(gateway) && gateway.indexOf("?")<0){
        	parameter = parameter + "?";
        }
        List keys = new ArrayList(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
          	String value =(String) params.get(keys.get(i));
            if(value == null || value.trim().length() ==0){
            	continue;
            } 
            try {
                parameter = parameter +(i==0&&gateway.indexOf("?")<0?"":"&")+ keys.get(i) + "="
                    + URLEncoder.encode(value, charSet) ;
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }
        parameter = parameter + "&sign=" + sign;
        return parameter;
	}
	
    @SuppressWarnings("unchecked")
	public static String getSign(Map params, String privateKey) throws Exception {
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        StringBuffer prestr = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			if (value == null || value.trim().length() == 0 ||"sign".equals(key)||"sign_type".equals(key)) {
				continue;
			}
			prestr.append(key + value);
		}
		logger.info("<"+prestr + privateKey+">");
		String md5str = UrlFormatUtil.getMd5(prestr + privateKey);
		logger.info("<"+md5str+">");
        return md5str;
    }
    
	/**
	 * �� strSrc ����GBK�����MD5ɢ��
	 * @param strSrc
	 * @return 16���ƴ������byteת��Ϊ16���ƴ���ֻ��1λ������Ҫ��ǰ��׷��0,����2λ16���ƴ� 
	 * @throws  UnsupportedEncodingException NoSuchAlgorithmException
	 */
	public final static String getMd5(String strSrc) throws Exception {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
			byte[] strTemp = strSrc.getBytes(GBK);
			MessageDigest mdTemp = MessageDigest.getInstance(MD5);
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
	
		
		@SuppressWarnings("unchecked")
		public static String createUrl(String gateway,Map params) throws Exception{
			//String sign = getSign(params, privateKey);
			
			StringBuffer parameter = new StringBuffer();
			
			parameter.append("ScriptPath="+params.get("ScriptPath"));
			parameter.append("&SessionID="+params.get("SessionID"));
			parameter.append("&Timeout="+params.get("Timeout"));
			parameter.append("&ParamCount="+params.get("ParamCount"));
			parameter.append("&Param1="+URLEncoder.encode(String.valueOf(params.get("Param1")), "UTF-8"));
			
			
	        //parameter = parameter + "&hmac=" + sign;
	        return parameter.toString();
		}
		
		
		public static String createPriceUrl(String gateway,Map params) throws Exception{
			
			StringBuffer parameter = new StringBuffer();
			
			parameter.append("train_no="+params.get("train_no"));
			parameter.append("&from_station_no="+params.get("from_station_no"));
			parameter.append("&to_station_no="+params.get("to_station_no"));
			parameter.append("&seat_types="+params.get("seat_types"));
			parameter.append("&train_date="+params.get("train_date"));
	        return parameter.toString();
		}
}
