package com.l9e.train.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @ClassName: HttpClientUtil
 * @Description: http工具类，采用静态代码块实现单例
 * @author: taoka
 * @date: 2018年5月10日 上午11:47:32
 * @Copyright: 2018 www.19e.cn Inc. All rights reserved.
 */
public class HttpClientUtil {
	private static final String CHARSET = "UTF-8";

	private static HttpClientUtil instance = null;
	static {
		instance = new HttpClientUtil();
	}

	public static HttpClientUtil getInstance() {
		return instance;
	}

	private HttpClientUtil() {
	}

	public String httpPost(String url, int timeout, byte[] bytes) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			httpPost.addHeader("User-Agent", "Mozilla/5.0");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + CHARSET);
			ByteArrayEntity entity = new ByteArrayEntity(bytes);
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			colse(reader, httpResponse, httpClient);
		}
		return response.toString();
	}

	public String httpPost(String url, int timeout, String parameter) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			httpPost.addHeader("User-Agent", "Mozilla/5.0");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + CHARSET);
			StringEntity entity = new StringEntity(parameter, CHARSET);
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			colse(reader, httpResponse, httpClient);
		}
		return response.toString();
	}

	public String httpJson(String url, int timeout, String parameter) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			httpPost.addHeader("User-Agent", "Mozilla/5.0");
			httpPost.setHeader("Content-Type", "application/json; charset=" + CHARSET);
			StringEntity entity = new StringEntity(parameter, CHARSET);
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			colse(reader, httpResponse, httpClient);
		}
		return response.toString();
	}

	public String httpGet(String url, int timeout) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		try {
			HttpGet httpget = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();// 设置请求和传输超时时间
			httpget.setConfig(requestConfig);
			httpget.addHeader("User-Agent", "Mozilla/5.0");
			httpResponse = httpClient.execute(httpget);
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			colse(reader, httpResponse, httpClient);
		}
		return response.toString();
	}

	/**
	 * 释放资源
	 * 
	 * @author: taoka
	 * @date: 2018年5月10日 上午11:55:15
	 * @param reader
	 * @param httpResponse
	 * @param httpClient
	 *            void
	 */
	private static void colse(BufferedReader reader, CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (httpResponse != null) {
			HttpClientUtils.closeQuietly(httpResponse);
		}
		if (httpClient != null) {
			HttpClientUtils.closeQuietly(httpClient);
		}
	}

}
