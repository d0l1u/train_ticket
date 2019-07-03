package com.l9e.util.hcpjar.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpsUtil {
	public static SSLContext sc=null;
	static{
		
		try {
			sc = SSLContext.getInstance("SSL"); 
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, 
			new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public static String sendHttps(String urlStr){
		String str_return = ""; 
		URL console;
		
		try {
			console = new URL(urlStr);
			//console.setURLStreamHandlerFactory(fac)
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection(); 
			conn.setSSLSocketFactory(sc.getSocketFactory()); 
			conn.setRequestMethod("GET");  
			conn.addRequestProperty("Connection", "Keep-Alive");
			conn.addRequestProperty("Host", "kyfw.12306.cn");
			//Accept: 
			//Accept-Language: zh-cn
			//User-Agent: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET4.0C; .NET4.0E)
			//x-requested-with: XMLHttpRequest
			//Cache-Control: no-cache
			//Referer: https://kyfw.12306.cn/otn/lcxxcx/init
			//System.out.println("ss");
			//conn.addRequestProperty("Referer", "https://kyfw.12306.cn/otn/lcxxcx/init");
			//conn.addRequestProperty("Cache-Control", "no-cache");
			//conn.addRequestProperty("Accept", "*/*");
			//conn.addRequestProperty("Accept-Language", "zh-cn");
			//conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET4.0C; .NET4.0E)");
			//conn.addRequestProperty("x-requested-with", "XMLHttpRequest");*/
			
			//Connection: Keep-Alive
			//Host: kyfw.12306.cn
			conn.setDoOutput(true);
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier()); 
			conn.connect(); 
			InputStream is = conn.getInputStream(); 
			DataInputStream indata = new DataInputStream(is); 
			String ret = ""; 
			while (ret != null) { 
				ret = indata.readLine(); 
				if (ret != null && !ret.trim().equals("")) { 
					str_return = str_return+ new String(ret.getBytes("ISO-8859-1"), "utf-8"); 
				} 
			} 
			conn.disconnect(); 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return str_return.replaceAll("'", "").replaceAll(";", "");
		return str_return;
	}
	
	private static class TrustAnyTrustManager implements X509TrustManager { 
		 public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
		 } 
		 public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
		 } 
		 public X509Certificate[] getAcceptedIssuers() { 
			 return new X509Certificate[] {}; 
		 } 
	} 
	private static class TrustAnyHostnameVerifier implements HostnameVerifier { 
		 public boolean verify(String hostname, SSLSession session) { 
			 return true; 
		 } 
	} 
	
	
	public static void main(String[] args) {
		System.out.println(sendHttps("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js"));
	}
}
