package com.l9e.util.elong;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 请求路径创建工具类
 * @author liuyi02
 * 针对 elong
 * */
public class ElongUrlFormatUtil {
	public static String createGetUrl(String url,Map<String,String> params,String charset){
		StringBuffer re=new StringBuffer();
		List keys = new ArrayList(params.keySet());
		re.append(url);
	    for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			if (value == null || value.trim().length() == 0) {
				continue;
			}
			re.append(key +"="+ encode(value,charset)+"&");
		}
		return re.substring(0, re.lastIndexOf("&"));
	}
	
	
	/**字符串编码*/
	public static String encode(String inputStr,String charset){
		try {
			return	URLEncoder.encode(inputStr, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**字符串解码*/
	public static String decode(String inputStr,String charset){
		try {
			return	URLDecoder.decode(inputStr, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**字符串编码-解码*/
	public static String enDecode(String enCharset,String deCharset,String inputStr){
		return	decode(encode(inputStr,enCharset),deCharset);
	}
	/**字符串编码-解码*/
	public static String enDeStr(String enCharset,String deCharset,String inputStr){
		try {
			return	new String(inputStr.getBytes(enCharset),deCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) {
		//%C5%AE%D7%D3%B4%F2%CB%C04%CB%EA%C5%AE%B6%F9
		System.out.println(decode("%C5%AE%D7%D3%B4%F2%CB%C04%CB%EA%C5%AE%B6%F9","gbk"));
	}
}
