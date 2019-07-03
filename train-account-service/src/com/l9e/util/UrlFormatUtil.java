package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * url组装工具
 * @author licheng
 *
 */
public class UrlFormatUtil {

	/**
	 * 组装请求地址，没有url则返回queryString，没有参数map则空串
	 * @param url
	 * @param params
	 * @return
	 */
	public static String createUrl(String url,Map<String, String> params) {
		return createUrl(url, params, "UTF-8");
	}
	/**
	 * 组装请求地址，没有url则返回queryString，没有参数map则空串,默认utf-8字符集
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String createUrl(String url,Map<String, String> params, String charset) {
		StringBuilder builder = new StringBuilder();
		url = url == null ? "" : url;
		charset = StringUtils.isEmpty(charset) ? "UTF-8" : charset;
		String queryString = "";
		
		try {
			if(params != null && params.size() > 0) {
				for(String key : params.keySet()) {
					String value = params.get(key);
					if(value == null) {
						value = "";
					}
					builder.append(key)
						.append("=")
						.append(URLEncoder.encode(value, charset))
						.append("&");
				}
				builder.setLength(builder.length() - 1);
				queryString = builder.toString();
			}
			
			builder.setLength(0);
			if(!StringUtils.isEmpty(url)) {
				builder.append(url)
					.append("?");
			}
			builder.append(queryString);
		} catch (UnsupportedEncodingException e) {
			builder.setLength(0);
			e.printStackTrace();
		}
		return builder.toString();
	}
}
