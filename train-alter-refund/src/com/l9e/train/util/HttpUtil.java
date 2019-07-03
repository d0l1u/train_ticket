package com.l9e.train.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuyou.train.commons.httpclient.client.HttpEngineClient;
import com.kuyou.train.commons.httpclient.client.HttpEngineResponse;

public class HttpUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param params
	 * @param inputCharset
	 * @return
	 */
	public static String sendByPost(String url, String params, String inputCharset) {

		StringBuffer strResponse = new StringBuffer(128);
		java.net.HttpURLConnection connection = null;
		java.net.URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		int charCount = -1;
		try {
			reqUrl = new java.net.URL(url);
			connection = (java.net.HttpURLConnection) reqUrl.openConnection();
			connection.setReadTimeout(30000);
			connection.setConnectTimeout(30000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			reqOut = new OutputStreamWriter(connection.getOutputStream());
			reqOut.write(params);
			reqOut.flush();
			in = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, inputCharset));
			while ((charCount = br.read()) != -1) {
				strResponse.append((char) charCount);
			}
		} catch (Exception e) {
			logger.error("访问远程服务器超时：<" + reqUrl + ">" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (reqOut != null) {
					reqOut.close();
				}
			} catch (Exception e) {
				logger.error("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常" + e.getMessage());
			}
		}
		logger.debug("返回流:[" + strResponse.toString() + "]");

		return strResponse.toString();
	}

	/**
	 * 发送get请求
	 * 
	 * @param reqUrl
	 * @param inputCharset
	 * @return
	 */
	public static String sendByGet(String reqUrl, String inputCharset, String connectiontimeout, String readtimeout) {

		java.net.HttpURLConnection conn = null;
		InputStream in = null;
		BufferedReader rd = null;
		java.net.URL url = null;
		String tmp = null;
		if (connectiontimeout == null) {
			connectiontimeout = "30000";
		}
		if (readtimeout == null) {
			readtimeout = "30000";
		}

		try {
			url = new java.net.URL(reqUrl);
			conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(Integer.parseInt(connectiontimeout));
			conn.setReadTimeout(Integer.parseInt(readtimeout));
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			in = conn.getInputStream();
			rd = new BufferedReader(new InputStreamReader(in, inputCharset));
		} catch (IOException e) {
			logger.error("访问远程服务器超时：<" + reqUrl + ">" + e.getMessage());
			try {
				if (rd != null)
					rd.close();
				if (in != null)
					in.close();
			} catch (IOException ex) {
				logger.error("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常" + e.getMessage());
			}
		}
		StringBuffer strResponse = new StringBuffer();
		try {
			while ((tmp = rd.readLine()) != null) {
				if (tmp.trim().length() > 0) {
					strResponse.append(tmp.trim());
				}
			}
		} catch (IOException e) {
			logger.error("读取远程服务器返回流异常：<" + reqUrl + ">" + e.getMessage());
		} finally {
			try {
				if (rd != null)
					rd.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				logger.error("读取远程服务器返回流后关闭读取流异常");
			}
		}

		logger.debug("返回流:[" + strResponse.toString() + "]");

		return strResponse.toString();
	}

	public static String postJson(String url, String charset, String parameter) {
		String result = "";
		if (StringUtils.isEmpty(url)) {
			logger.debug("请求路径为空，做失败处理");
			return result;
		}

		// logger.info("请求路径:" + url);
		// logger.info("请求参数:" + parameter);

		// 超时默认五分钟
		int timeout = 1000 * 60 * 5;

		logger.info("time out:" + timeout);
		try {
			HttpEngineClient httpclient = new HttpEngineClient().buildCharset(charset).buildTimeout(timeout);
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/json ; charset=utf-8");
			HttpEngineResponse response = httpclient.doPost(url, header, parameter);
			result = response.getResponseContent();
		} catch (SocketTimeoutException | ConnectTimeoutException | NoHttpResponseException e) {
			logger.info("访问机器人服务【TIME OUT】", e);
		} catch (Exception e) {
			logger.info("访问机器人服务【Exception】", e);
		}
		return result;
	}

}
