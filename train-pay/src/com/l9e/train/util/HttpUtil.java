package com.l9e.train.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	public static final String TIME_OUT = "TIMEOUT";
	public static final String URL_ERROR = "URLERROR";
	public static final String CONNECT_ERROR = "CONNECTERROR";
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
			logger.info("访问远程服务器超时：<" + reqUrl + ">" + e.getMessage());
			try {
				if (rd != null)
					rd.close();
				if (in != null)
					in.close();
			} catch (IOException ex) {
				logger.info("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常" + e.getMessage());
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
			logger.info("读取远程服务器返回流异常：<" + reqUrl + ">" + e.getMessage());
		} finally {
			try {
				if (rd != null)
					rd.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				logger.info("读取远程服务器返回流后关闭读取流异常");
			}
		}

		logger.debug("返回流:[" + strResponse.toString() + "]");

		return strResponse.toString();
	}

	/**
	 * POST 请求
	 * 
	 * @author: taoka
	 * @date: 2018年2月28日 上午11:17:27
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param charset
	 *            字符集
	 * @param timeout
	 *            超时时间:毫秒
	 * @param isJson
	 *            是否为Json请求
	 * @return String
	 */
	public static String httpPost(String url, String params, String charset, int timeout, boolean isJson) {
		if (StringUtils.isBlank(charset)) {
			charset = "UTF-8";
		}
		CloseableHttpClient client = null;
		HttpPost post = null;
		CloseableHttpResponse response = null;
		InputStreamReader inputStreamReader = null;
		String result = "";
		try {
			client = HttpClients.createDefault();
			post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
			post.setConfig(requestConfig);
			if (isJson) {
				post.setHeader("Content-Type", "application/json; charset=" + charset);
			} else {
				post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
			}
			StringEntity strEntity = new StringEntity(params, charset);
			post.setEntity(strEntity);
			response = client.execute(post);

			HttpEntity entity = response.getEntity();
			BufferedReader br = null;
			StringBuffer buffer = new StringBuffer();
			inputStreamReader = new InputStreamReader(entity.getContent(), charset);
			br = new BufferedReader(inputStreamReader);
			String line = "";
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
			result = buffer.toString();
		} catch (SocketTimeoutException | ConnectTimeoutException e) {
			logger.info("请求超时<" + url + ">, Exception:" + e.getClass().getSimpleName(), e);
			result = TIME_OUT;
		} catch (NoHttpResponseException | ConnectionClosedException e) {
			logger.info("请求错误<" + url + ">, Exception:" + e.getClass().getSimpleName(), e);
			result = CONNECT_ERROR;
		} catch (Exception e) {
			logger.info("请求异常<" + url + ">, Exception:" + e.getClass().getSimpleName(), e);
			result = CONNECT_ERROR;
		} finally {
			// 释放资源
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (post != null) {
					post.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
