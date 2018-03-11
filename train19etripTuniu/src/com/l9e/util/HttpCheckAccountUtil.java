package com.l9e.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class HttpCheckAccountUtil {
	private static final Logger logger = Logger.getLogger(HttpCheckAccountUtil.class);
	
	/**
	 * 发送post请求
	 * @param url
	 * @param params
	 * @param inputCharset
	 * @return
	 */
	public static String sendByPost(String url, String params, String inputCharset) {
		
		StringBuffer strResponse = new StringBuffer(128);
		java.net.HttpURLConnection connection = null;
		java.net.URL reqUrl = null;
		PrintWriter pWriter = null;
		InputStream in = null;
		BufferedReader br = null;
		int charCount = -1;
		try {
//			System.getProperties().setProperty("http.proxyHost","114.247.40.65");
//			System.getProperties().setProperty("http.proxyPort", "41000" );
			reqUrl = new java.net.URL(url);
			connection = (java.net.HttpURLConnection) reqUrl.openConnection();
			connection.setReadTimeout(50000);
			connection.setConnectTimeout(50000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			pWriter = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), inputCharset));
			pWriter.write(params);
			pWriter.flush();
			in = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, inputCharset));
			while ((charCount = br.read()) != -1) {
				strResponse.append((char) charCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("访问远程服务器超时：<" + reqUrl + ">" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (pWriter != null) {
					pWriter.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常"
						+ e.getMessage());
			}
		}
		
		return strResponse.toString();
	}


}
