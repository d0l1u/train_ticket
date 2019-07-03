package com.l9e.train.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	@Test
	public void test() {
		String params = "{\"cards\":[{\"cardMoney\":\"100\",\"cardNo\":\"6660200000734845\",\"cardPwd\":\"488919\"}],\"channalName\":\"jd_43cd73bd96bf5\",\"channalPwd\":\"pu583299\",\"contacts\":\"陶凯\",\"fromCode\":\"HFH\",\"fromName\":\"合肥\",\"passengers\":[{\"cpId\":\"CP1702161144372603\",\"idNo\":\"34112619681010721x\",\"idType\":\"2\",\"name\":\"孙天星\",\"seatType\":\"8\"}],\"payMoney\":\"64.500\",\"phone\":\"15201004872\",\"railwayName\":\"msyw0862\",\"railwayPwd\":\"uacq8404\",\"timeOut\":\"300000\",\"toCode\":\"HZH\",\"toName\":\"杭州\",\"trainCode\":\"K425\",\"trainDate\":\"2017-02-17\"}";
		String res = sendByPostforJD("http://123.56.155.207:9000/", params, "utf-8");
		System.err.println(res);
	}
	
	/**
	 * 京东预定--发送post请求
	 * @param url
	 * @param params
	 * @param inputCharset
	 * @return
	 */
	public static String sendByPostforJD(String url, String params, String inputCharset) {
		
		StringBuffer strResponse = new StringBuffer(128);
		java.net.HttpURLConnection connection = null;
		java.net.URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		String result = "";
		int charCount = -1;
		try {
			reqUrl = new java.net.URL(url);
			connection = (java.net.HttpURLConnection) reqUrl.openConnection();
			connection.setReadTimeout(300000);
			connection.setConnectTimeout(30000);
			logger.info("京东请求参数encode前为："+params);
			params = URLEncoder.encode(params,inputCharset);
			logger.info("京东请求参数encode后为："+params);
			
			connection.setRequestProperty("Accept-Charset",inputCharset);//设置编码语言
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset="+inputCharset);		
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
			result = strResponse.toString();
			result = URLDecoder.decode(result, inputCharset);
			result = new UnicodeUtils().decode(result);
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
				logger.error("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常"
						+ e.getMessage());
			}
		}
		logger.debug("返回值[" + result + "]");
		
		return result;
	}
	
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
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		int charCount = -1;
		try {
			reqUrl = new java.net.URL(url);
			connection = (java.net.HttpURLConnection) reqUrl.openConnection();
			connection.setReadTimeout(300000);
			connection.setConnectTimeout(30000);
			connection.setRequestProperty("Accept-Charset",inputCharset);//设置编码语言
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
				logger.error("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常"
						+ e.getMessage());
			}
		}
		logger.debug("返回结果[" + strResponse.toString() + "]");
		
		return strResponse.toString();
	}

	/**
	 * 发送get请求
	 * @param reqUrl
	 * @param inputCharset
	 * @return
	 */
	public static String sendByGet(String reqUrl, String inputCharset,
			String connectiontimeout, String readtimeout) {

		java.net.HttpURLConnection conn = null;
		InputStream in = null;
		BufferedReader rd = null;
		java.net.URL url = null;
		String tmp = null;
		if(connectiontimeout == null){
			connectiontimeout = "30000";
		}
		if(readtimeout == null){
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
				logger.error("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常"
						+ e.getMessage());
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
			logger.error("读取远程服务器返回流异常" + reqUrl + ">" + e.getMessage());
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

		logger.debug("返回结果[" + strResponse.toString() + "]");

		return strResponse.toString();
	}

	/**
	 * 发送get请求
	 * 
	 * @param reqUrl
	 * @param inputCharset
	 * @return
	 */
	public static String sendByGet(String reqUrl, String inputCharset, int connectiontimeout, int readtimeout) {

		java.net.HttpURLConnection conn = null;
		InputStream in = null;
		BufferedReader rd = null;
		java.net.URL url = null;
		String tmp = null;
		try {
			url = new java.net.URL(reqUrl);
			conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connectiontimeout);
			conn.setReadTimeout(readtimeout);
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
			logger.error("读取远程服务器返回流异常" + reqUrl + ">" + e.getMessage());
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

		logger.debug("返回结果[" + strResponse.toString() + "]");

		return strResponse.toString();
	}
}
