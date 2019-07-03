package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

public class HttpPostUtil {
	
public static String sendAndRecive(String url,String params,int soOutTime,int conOutTime) throws Exception{
		
		HttpClientParams hcp = new HttpClientParams();
		hcp.setContentCharset("UTF-8");
		hcp.setSoTimeout(soOutTime);
		hcp.setConnectionManagerTimeout(conOutTime);
		HttpClient hc = new HttpClient();
		hc.setParams(hcp);
		
		PostMethod pm = new PostMethod(url);
//		hcp.addParameter("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		
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
	    } finally {
	      pm.releaseConnection();
	      pm = null;
	      hc = null;
	    }
//	    System.out.println("repMsg:"+repMsg);
	    return repMsg;
	}
	
	public static String sendAndRecive(String url,String params) throws Exception{
		
		HttpClientParams hcp = new HttpClientParams();
		hcp.setContentCharset("UTF-8");
		hcp.setSoTimeout(120000);
		HttpClient hc = new HttpClient();
		hc.setParams(hcp);
		
		PostMethod pm = new PostMethod(url);
//		hcp.addParameter("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		
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
	    } finally {
	      pm.releaseConnection();
	      pm = null;
	      hc = null;
	    }
//	    System.out.println("repMsg:"+repMsg);
	    return repMsg;
	}
	
	public static String UrlEncodeFormat(String str){
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String UrlDecodeFormat(String str){
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
