package com.l9e.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.log4j.Logger;

public class HttpsUtil {
	public static SSLContext sc = null;
	private static Logger logger = Logger.getLogger(HttpsUtil.class);
	static {

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

	public static String sendHttps(String urlStr) {
		long start=System.currentTimeMillis();
		String str_return = "";
		URL console;

		try {
			console = new URL(urlStr);
			HttpsURLConnection conn = (HttpsURLConnection) console
					.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
	/*
			conn.setRequestProperty("method", "get");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/;q=0.8");
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Accept-Language", "zh-CN");
	*/
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			//DataInputStream indata = new DataInputStream(is);
			BufferedInputStream bis = new BufferedInputStream(is);
			String ret = "";
			
			/*byte[] bytes = new byte[br.available()];
			System.out.println(br.available());
			br.read(bytes);
			*/

			ret = br.readLine();
			while (ret != null) {
				sb.append(ret);
				ret = br.readLine();
			}
			if(br != null) {
				br.close();
			}
			if(is != null) {
				is.close();
			}
			conn.disconnect();

			//str_return = new String(sb.toString().getBytes(), "UTF-8");
			//str_return = new String(bytes, "UTF-8");
			str_return = sb.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		logger.info((System.currentTimeMillis()-start)+"ms耗时");
		return str_return;
	}

	private static class TrustAnyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
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

}
