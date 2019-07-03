package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * URL组装工具类
 * @author yuzj
 * @since 2009-5-23
 */
public class UrlFormatUtil {
	
	private static final String UTF_8 = "UTF-8";

	static Logger logger = Logger.getLogger(UrlFormatUtil.class);
	
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
	public static String CreateUrl(Map params,String charSet) throws Exception{
		
		StringBuffer parameter = new StringBuffer();
     
        List keys = new ArrayList(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
          	String value =(String) params.get(keys.get(i));
           /* if(value == null || value.trim().length() ==0){
            	continue;
            } */
          	
            try {
            	URLEncoder.encode(value, charSet);
            	parameter.append(keys.get(i)).append("=").append(value);
            	if(i<keys.size()-1){
            		parameter.append("&");
            	}
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }
        return parameter.toString();
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
	
	
	public static void main(String[] args) {
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("action", "yupiao");
		param.put("format", "json");
		param.put("version", "3");
		param.put("encode", "utf-8");
		param.put("is_mobile", "");
		param.put("from", "");
		param.put("to", "");
		param.put("date", "");
		param.put("real_time", "");
		param.put("train_number", "");
		param.put("user", "");
		param.put("reqtime", "");
		param.put("sign", "");
		
		try {
			System.out.println(UrlFormatUtil.CreateUrl( param, "utf-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
