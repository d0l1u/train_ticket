package test.l9e.tuniu.test.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class SSLUtil {
	
	public static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
	public static String ACCEPT_ENCODING = "gzip,deflate";
	public static String ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8";
	public static String CONNECTION = "keep-alive";
	public static String USER_AGENT = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36";
	
	/*默认超时*/
	public static int DEFAULT_CONNECTION_TIMEOUT = 10000;
	public static int DEFAULT_READ_TIMEOUT = 10000;
	
	/*默认字符集*/
	public static String DEFAULT_CHARSET = "UTF-8";
	
	private static Logger logger = Logger.getLogger(SSLUtil.class);
	
	/**
	 * ssl上下文
	 */
	private static SSLContext sslContext = null;
	
	private static HostnameVerifier hostnameVerifier = null;
	
	
	static {
		
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());
			
			hostnameVerifier = new TrustAnyHostnameVerifier();
		} catch (NoSuchAlgorithmException e) {
			logger.error("sslContext初始化失败,e : " + e.getMessage());
		} catch (KeyManagementException e) {
			logger.error("sslContext初始化失败,e : " + e.getMessage());
		}
	}
	
	/**http*/
	/*get*/
//	
//	public static String httpGet(String url, String charsetName, Proxy proxy) throws IOException {
//		return httpGet(url, charsetName, proxy, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT);
//	}
	
	/**
	 * http get 请求
	 * @param url
	 * @param charsetName
	 * @param proxy
	 * @param connectionTimeout
	 * @param readTimeout
	 * @return
	 * @throws IOException
	 */
	public static String httpGet(String url, String charsetName, int connectionTimeout, int readTimeout) throws IOException {
		String pageHtml = null;
		
		HttpURLConnection conn = null;
		conn = httpConnection(url);
		try{
			
			/*设置超时*/
			conn.setConnectTimeout(connectionTimeout);
			conn.setReadTimeout(readTimeout);
			
			conn.connect();
			
			pageHtml = responseBody(conn, charsetName);
			
			return pageHtml;
		} finally {
			conn.disconnect();
		}
	}
	
	
	/**https*/
	/*get*/
//	public static String httpsGet(String url) throws IOException {
//		return httpsGet(url, null);
//	}
//	
//	public static String httpsGet(String url, String charsetName, int connectionTimeout, int readTimeout) throws IOException {
//		return httpsGet(url, charsetName, null, connectionTimeout, readTimeout);
//	}
//	
//	public static String httpsGet(String url, int connectionTimeout, int readTimeout) throws IOException {
//		return httpsGet(url, DEFAULT_CHARSET, null, connectionTimeout, readTimeout);
//	}
//	
//	public static String httpsGet(String url, Proxy proxy, int connectionTimeout, int readTimeout) throws IOException {
//		return httpsGet(url, DEFAULT_CHARSET, proxy, connectionTimeout, readTimeout);
//	}
//	
//	public static String httpsGet(String url,Proxy proxy) throws IOException {
//		return httpsGet(url, DEFAULT_CHARSET, proxy);
//	}
//	
//	public static String httpsGet(String url, String charsetName, Proxy proxy) throws IOException {
//		return httpsGet(url, charsetName, proxy, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT);
//	}
	
	/**
	 * https get 请求
	 * @param url
	 * @param charsetName
	 * @param proxy
	 * @param connectionTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String httpsGet(String url, String charsetName, int connectionTimeout, int readTimeout) throws IOException {
		
		String pageHtml = null;
		
		HttpsURLConnection conn = null;
		conn = httpsConnection(url);
		try{
			
			/*设置超时*/
			conn.setConnectTimeout(connectionTimeout);
			conn.setReadTimeout(readTimeout);
			
			conn.connect();
			
			pageHtml = responseBody(conn, charsetName);
			
			return pageHtml;
		} finally {
			conn.disconnect();
		}
		
	}
	
	/*post*/
	
	
	
	/**/
	
	/**
	 * 创建https连接
	 */
	private static HttpsURLConnection httpsConnection(String url) throws IOException {
		
		URL connUrl = new URL(url);
		HttpsURLConnection connection = null;
		connection = (HttpsURLConnection) connUrl.openConnection();
		
		connection.setHostnameVerifier(hostnameVerifier);
		connection.setSSLSocketFactory(sslContext.getSocketFactory());
		/*通用设置*/
		wrapURLConnection(connection);
		
		return connection;
	}
	
	/**
	 * 创建http连接
	 */
	private static HttpURLConnection httpConnection(String url) throws IOException {
		
		URL connUrl = new URL(url);
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) connUrl.openConnection();
		
		/*通用设置*/
		wrapURLConnection(connection);
		
		return connection;
	}
	
	/**
	 * 设置通用参数以及通用请求头
	 * @param connection
	 */
	private static void wrapURLConnection(URLConnection connection) {
		/*通用设置*/
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		
		/*请求头*/
		connection.setRequestProperty("Accept", ACCEPT);
		connection.setRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
		connection.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
		connection.setRequestProperty("Connection", CONNECTION);
		connection.setRequestProperty("User-Agent", USER_AGENT);
//		connection.setRequestProperty("Host", "kyfw.12306.cn");
//		connection.setRequestProperty("Referer", "https://kyfw.12306.cn/otn/index/init");
	}
	
	private static String responseBody(URLConnection connection, String charset) throws IOException {
		String page = null;
		
		InputStream in = null;
		BufferedInputStream bi = null;
		
		try{
			
			String contentEncoding = connection.getContentEncoding();
			
			if(!StringUtils.isEmpty(contentEncoding) && contentEncoding.toLowerCase().indexOf("gzip") != -1) {
				in = new GZIPInputStream(connection.getInputStream());
			} else {
				in = connection.getInputStream();
			}
			
			bi = new BufferedInputStream(in);
			
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			byte[] buf = new byte[1024 << 4];
			int len = -1;
			while((len = bi.read(buf)) != -1) {
				bao.write(buf, 0, len);
			}
			bao.close();
			
			page = new String(bao.toByteArray(), charset);
			return page;
		} finally {
			if(bi != null) {
				bi.close();
			}
		}
		
	}
	
//	private static String responseBody(URLConnection connection, String charset) throws IOException {
//		String page = null;
//		
//		InputStream in = null;
//		BufferedReader br = null;
//		try {
//			StringBuilder sb = new StringBuilder();
//			
//			in = connection.getInputStream();
//			br = new BufferedReader(new InputStreamReader(in, charset));
//			
//			String line = null;
//			while((line = br.readLine()) != null) {
//				sb.append(line).append("\n");
//			}
//			
//			page = sb.toString();
//			
//			return page;
//		} finally {
//			if(in != null) {
//				try {
//					in.close();
//				} finally {
//					if(br != null) {
//						br.close();
//					}
//				}
//			}
//			
//		}
//	}
	

	private static class TrustAnyTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[]{};
		}
		
	}
	
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String arg0, SSLSession session) {
			return true;
		}
		
	}
}
