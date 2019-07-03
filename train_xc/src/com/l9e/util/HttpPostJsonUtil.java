package com.l9e.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class HttpPostJsonUtil {
	private static final Logger logger = Logger.getLogger(HttpPostJsonUtil.class);
	
	//发起http的post请求，参数格式为Json
	public static String sendJsonPost(String urlStr, String json, String inputCharset) {
		 
		StringBuffer sb = new StringBuffer(""); 
		InputStream in = null;
		BufferedReader br = null;
		DataOutputStream out = null;
        try { 
            //创建连接 
            URL url = new URL(urlStr); 
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
            connection.setDoOutput(true); 
            connection.setDoInput(true); 
            connection.setReadTimeout(30000);
			connection.setConnectTimeout(30000);
            connection.setRequestMethod("POST"); 
            connection.setUseCaches(false); 
            connection.setInstanceFollowRedirects(true); 
//          connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");


            connection.connect(); 

            //POST请求 
            out = new DataOutputStream(connection.getOutputStream()); 

            out.write(json.getBytes("UTF-8")); 
            out.flush(); 

            in = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(in, inputCharset));
            String lines; 
            
            while ((lines = br.readLine()) != null) { 
//                lines = new String(lines.getBytes("UTF-8"), inputCharset); 
                sb.append(lines); 
            } 
//            logger.info("+++++++"+sb); 
            br.close(); 
            // 断开连接 
            connection.disconnect(); 
        } catch (Exception e) { 
        	logger.error("访问远程服务器超时：<" + urlStr + ">" + e.getMessage());
            e.printStackTrace(); 
		}finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				logger.error("访问远程服务器超时：<" + urlStr + ">，关闭读取流异常"
						+ e.getMessage());
			}
		}

		return sb.toString();
	}



}
