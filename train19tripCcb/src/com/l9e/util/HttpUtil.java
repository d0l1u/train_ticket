package com.l9e.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

public class HttpUtil {
	private static final Logger logger = Logger.getLogger(HttpUtil.class);
	
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
				logger.error("访问远程服务器超时：<" + reqUrl + ">，关闭读取流异常"
						+ e.getMessage());
			}
		}
		logger.debug("返回流:[" + strResponse.toString() + "]");
		
		return strResponse.toString();
	}
	
	
	/**
	 * 发送实名核验的post请求，超时时间为4秒
	 * @param url
	 * @param params
	 * @param inputCharset
	 * @return
	 */
	public static String sendRealNameByPost(String url, String params, String inputCharset) {
		
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
			connection.setReadTimeout(4000);
			connection.setConnectTimeout(4000);
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
		logger.debug("返回流:[" + strResponse.toString() + "]");
		
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

public static String sendAndRecive(String url,String params,String charset) throws Exception{
		
		HttpClientParams hcp = new HttpClientParams();
		hcp.setContentCharset(charset);
		hcp.setSoTimeout(120000);
		HttpClient hc = new HttpClient();
		hc.setParams(hcp);
		
		PostMethod pm = new PostMethod(url);
		pm.addParameter("Content-Type","application/x-www-form-urlencoded;charset="+charset);
		
		String[] aParam = params.split("&");
	    if (aParam.length == 0) {
	      return null;
	    }
	    int z = 0;
	    for (int i = 0; i < aParam.length; i++) {
	      z = aParam[i].indexOf('=');
	      if (z != -1) {
	        pm.addParameter(aParam[i].substring(0, z++), aParam[i].substring(z));
	      }

	    }
	   
	    String repMsg = "";
	    try {
	      hc.executeMethod(pm);
	      repMsg = pm.getResponseBodyAsString();
	      logger.info("返回数据："+repMsg);
	    } finally {
	      pm.releaseConnection();
	      pm = null;
	      hc = null;
	    }

	    return repMsg;
	}

	public static String getValue(String str, String key){
		String[] strArr = str.split("&");
		Map<String,String> map = new HashMap<String,String>();
		for(String ss:strArr){
			map.put(ss.split("=")[0], ss.split("=")[1]);
		}
		return map.get(key);
	}
}
