package com.l9e.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.transaction.vo.TrainInFile;

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
		// return str_return.replaceAll("'", "").replaceAll(";", "");
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

	public static void main(String[] args) throws JsonParseException,
			IOException {

		String result = null;
		result = FileUtil.readFile("file/train_list.js");
		if (result == null || result.isEmpty()) {
			result = sendHttps("https://kyfw.12306.cn/otn/resources/js/query/train_list.js");
		}
		int index = result.indexOf("{");
		result = result.substring(index);
		int endIndex = result.indexOf("}");
		String trainInfo = result.substring(0, endIndex + 1);
		result = result.substring(endIndex + 1);
		int startIndex = trainInfo.lastIndexOf("{");
		String dateString = trainInfo.substring(0, startIndex);
		System.out.println(dateString);
		String date = MatchUtil.matchDay(dateString);
		trainInfo = trainInfo.substring(startIndex);
		OutputStream os = FileUtil.openOutputStream("file/" + date + ".csv");
		int dataIndex = -1;
		while (endIndex != -1) {
			System.out.println(trainInfo);
			endIndex = result.indexOf("}");
			trainInfo = result.substring(0, endIndex + 1);
			result = result.substring(endIndex + 1);
			if (result.indexOf("]}") == 0) {
				FileUtil.closeOutputStream(os);
				break;
			}
			startIndex = trainInfo.lastIndexOf("{");
			trainInfo = trainInfo.substring(startIndex);
			System.out.println(trainInfo);
			ObjectMapper objMapper = new ObjectMapper();
			JsonParser jsonParser = new JsonFactory().createParser(trainInfo);
			TrainInFile trainInFile = objMapper.readValue(jsonParser,
					TrainInFile.class);
			String trainCode = "", initialStation = "", terminalStation = "", trainNo = "";
			String value = trainInFile.getStation_train_code();
			trainCode = MatchUtil.match(value, "[C|Z|K|D|G|T|L][0-9]+|[0-9]+");
			initialStation = value.substring(value.indexOf("(") + 1,
					value.indexOf("-"));
			terminalStation = value.substring(value.indexOf("-") + 1,
					value.indexOf(")"));
			trainNo = trainInFile.getTrain_no();
			trainInfo = trainCode + ", " + initialStation + ", "
					+ terminalStation + ", " + trainNo;
			FileUtil.writeWord(os, trainInfo + "\n");
		}
	}

}
