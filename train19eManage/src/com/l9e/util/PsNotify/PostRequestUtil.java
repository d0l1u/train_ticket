package com.l9e.util.PsNotify;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * @author yanglj@19pay.com.cn
 * 
 * @说明 POST请求处理
 */
public class PostRequestUtil {

	static Logger logger = Logger.getLogger(PostRequestUtil.class);

	public static String TIME_OUT = "TIMEOUT";

	public static String URL_ERROR = "URLERROR";

	public static String CONNECT_ERROR = "CONNECTERROR";

	/**
	 * post请求方法
	 * 
	 * @param charset
	 * @param url
	 * @param parm
	 * @return
	 */
	public static String getPostRes(String charset, String strURL, String parm) {
		if (StringUtils.isEmpty(strURL)) {
			logger.debug("服务器路径或发送参数为空");
			return null;
		}

		logger.info("发送URL:"+strURL+"?"+parm);
		
		java.net.HttpURLConnection conn = null;
		java.io.InputStream in = null;
		// OutputStreamWriter out = null;
		OutputStreamWriter wr = null;
		java.io.BufferedReader rd = null;
		java.net.URL url = null;
		String tmp = null;
		/**
		 * 
		 * 清除DNS缓存
		 * 
		 */
		/*Class clazz = java.net.InetAddress.class;
		Field cacheField = null;
		try {
			cacheField = clazz.getDeclaredField("addressCache");
			cacheField.setAccessible(true);
			final Object o = cacheField.get(clazz);
			Class clazz2 = o.getClass();
			final Field cachePolicyField = clazz2.getDeclaredField("policy");
			final Field cacheMapField = clazz2.getDeclaredField("cache");
			cacheMapField.setAccessible(true);
			final Map cacheMap = (Map) cacheMapField.get(o);
			cachePolicyField.setAccessible(true);
			cacheMap.clear();// 这步比较关键，用于清除原来的缓存
			// cachePolicyField.setInt(o,24*60*60);//设置缓存的时间，单位秒(-1,永久缓存;0,不缓存;其它>0的值为缓存的秒数)
		} catch (Exception e) {
			logger.debug("清除DNS缓存异常：<" + strURL + "> 原因：" + e.getMessage());
		}*/
		/**
		 * 
		 * 从配置文件获取HTTP ConnectTimeout
		 * 
		 */
		String connectiontimeout = null;//Config.getProperty("connectiontimeout");
		if (null == connectiontimeout || "".equals(connectiontimeout)) {
			// 默认Http连接超时时长：5秒
			connectiontimeout = "10000";
		}
		/**
		 * 
		 * 从配置文件获取HTTP ReadTimeout
		 * 
		 */
		String readtimeout = null;//Config.getProperty("readtimeout");
		if (null == readtimeout || "".equals(readtimeout)) {
			// 默认超时时长：20秒
			readtimeout = "20000";
		}
		StringBuffer result = new StringBuffer();
		try {
			url = new java.net.URL(strURL);
			conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(Integer.parseInt(connectiontimeout));
			conn.setReadTimeout(Integer.parseInt(readtimeout));
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			// conn.addRequestProperty("Content-type", "text/html");
			// conn.addRequestProperty("Accept-Charset", "UTF-8");
			// conn.addRequestProperty("contentType", "UTF-8");
			conn.addRequestProperty("Content-length", "" + parm.length());
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(parm);
			wr.flush();
			wr.close();
			in = conn.getInputStream();
			rd = new java.io.BufferedReader(new java.io.InputStreamReader(in,
					charset));
			while ((tmp = rd.readLine()) != null) {
				if (tmp.trim().length() > 0) {
					result.append(tmp.trim());
					// break;
				}
			}
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error("访问远程服务器错误POST：<" + strURL + "> 原因：" + e.getMessage());
			return CONNECT_ERROR;
		}catch(ProtocolException e){
			logger.error("访问远程服务器错误POST：<" + strURL + "> 原因：" + e.getMessage());
			return CONNECT_ERROR;
		}catch (ConnectException e) {
			// TODO: handle exception
			logger.error("访问远程服务器错误POST：<" + strURL + "> 原因：" + e.getMessage());
			return CONNECT_ERROR;
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			logger.error("访问远程服务器错误POST：<" + strURL + "> 原因：" + e.getMessage());
			return CONNECT_ERROR;
		}catch (IOException e) {
			logger.error("访问远程服务器超时POST：<" + strURL + "> 原因：" + e.getMessage());
			return TIME_OUT;
		}finally{
			try {
				if (rd != null)
					rd.close();
				if (in != null)
					in.close();
			} catch (java.io.IOException e) {
				logger.error("访问远程服务器错误POST：<" + strURL + "> 原因：" + e.getMessage());
			}
		}
		// 将返回的字符串写入log
		logger.info("ResponseStringByPost:[" + result.toString() + "]");
		// logger.info("ResponseStringByPost响应信息："+result);
		return result.toString();
	}

}
