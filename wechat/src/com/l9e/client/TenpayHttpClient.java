package com.l9e.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;

import com.l9e.weixin.util.HttpClientUtil;

/**
 * 财付通http或者https网络通信客户端<br/>
 * ========================================================================<br/>
 * api说明：<br/> setReqContent($reqContent),设置请求内容，无论post和get，都用get方式提供<br/>
 * getResContent(), 获取应答内容<br/> setMethod(method),设置请求方法,post或者get<br/>
 * getErrInfo(),获取错误信息<br/> setCertInfo(certFile, certPasswd),设置证书，双向https时需要使用<br/>
 * setCaInfo(caFile), 设置CA，格式未pem，不设置则不检查<br/> setTimeOut(timeOut)， 设置超时时间，单位秒<br/>
 * getResponseCode(), 取返回的http状态码<br/> call(),真正调用接口<br/>
 * getCharset()/setCharset(),字符集编码<br/>
 * 
 * ========================================================================<br/>
 * 
 */
public class TenpayHttpClient {
	private static final Logger logger = Logger
			.getLogger(TenpayHttpClient.class);

	private static final String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)";

	private static final String JKS_CA_FILENAME = "tenpay_cacert.jks";

	private static final String JKS_CA_ALIAS = "tenpay";

	private static final String JKS_CA_PASSWORD = "1219762701";

	/** ca证书文件 */
	private File caFile;

	/** 证书文件 */
	private File certFile;

	/** 证书密码 */
	private String certPasswd;

	/** 请求内容，无论post和get，都用get方式提供 */
	private String reqContent;

	/** 应答内容 */
	private String resContent;

	/** 请求方法 */
	private String method;

	/** 错误信息 */
	private String errInfo;

	/** 超时时间,以秒为单位 */
	private int timeOut;

	/** http应答编码 */
	private int responseCode;

	/** 字符编码 */
	private String charset;

	private InputStream inputStream;

	public TenpayHttpClient() {
		this.caFile = null;
		this.certFile = null;
		this.certPasswd = "";

		this.reqContent = "";
		this.resContent = "";
		this.method = "POST";
		this.errInfo = "";
		this.timeOut = 30;// 30秒

		this.responseCode = 0;
		this.charset = "GBK";

		this.inputStream = null;
	}

	/**
	 * 设置证书信息
	 * 
	 * @param certFile
	 *            证书文件
	 * @param certPasswd
	 *            证书密码
	 */
	public void setCertInfo(File certFile, String certPasswd) {
		this.certFile = certFile;
		this.certPasswd = certPasswd;
	}

	/**
	 * 设置ca
	 * 
	 * @param caFile
	 */
	public void setCaInfo(File caFile) {
		this.caFile = caFile;
	}

	/**
	 * 设置请求内容
	 * 
	 * @param reqContent
	 *            表求内容
	 */
	public void setReqContent(String reqContent) {
		this.reqContent = reqContent;
	}

	/**
	 * 获取结果内容
	 * 
	 * @return String
	 * @throws IOException
	 */
	public String getResContent() {
		try {
			this.doResponse();
		} catch (IOException e) {
			this.errInfo = e.getMessage();
			// return "";
		}

		return this.resContent;
	}

	/**
	 * 设置请求方法post或者get
	 * 
	 * @param method
	 *            请求方法post/get
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 获取错误信息
	 * 
	 * @return String
	 */
	public String getErrInfo() {
		return this.errInfo;
	}

	/**
	 * 设置超时时间,以秒为单位
	 * 
	 * @param timeOut
	 *            超时时间,以秒为单位
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * 获取http状态码
	 * 
	 * @return int
	 */
	public int getResponseCode() {
		return this.responseCode;
	}

	/**
	 * 执行http调用。true:成功 false:失败
	 * 
	 * @return boolean
	 */
	public boolean call() {

		boolean isRet = false;
		logger.info("pem文件的路径：" + caFile.getAbsolutePath() + ", pfx文件的路径："
				+ certFile.getAbsolutePath());
		// http
		if (null == this.caFile && null == this.certFile) {
			try {
				this.callHttp();
				isRet = true;
			} catch (IOException e) {
				this.errInfo = e.getMessage();
				logger.error("发生错误:" + e);
			}
			return isRet;
		}

		// https
		try {
			this.callHttps();
			isRet = true;
		} catch (UnrecoverableKeyException e) {
			logger.error("发生错误UnrecoverableKeyException：" + e);
			this.errInfo = e.getMessage();
		} catch (KeyManagementException e) {
			logger.error("发生错误KeyManagementException：" + e);
			this.errInfo = e.getMessage();
		} /*
			 * catch (CertificateException e) {
			 * logger.error("发生错误CertificateException：" + e); this.errInfo =
			 * e.getMessage(); }
			 */catch (KeyStoreException e) {
			logger.error("发生错误KeyStoreException：" + e);
			this.errInfo = e.getMessage();
		} catch (NoSuchAlgorithmException e) {
			logger.error("发生错误NoSuchAlgorithmException：" + e);
			this.errInfo = e.getMessage();
		} 

		return isRet;

	}

	protected void callHttp() throws IOException {
		logger.info("进入callHttp");
		if ("POST".equals(this.method.toUpperCase())) {
			logger.info("callHttp reqContent: " + reqContent);
			String url = HttpClientUtil.getURL(this.reqContent);
			logger.info("callHttp url: " + url);
			String queryString = HttpClientUtil.getQueryString(this.reqContent);
			byte[] postData = queryString.getBytes(this.charset);
			logger.info("callHttp url: " + url + "   , postData: " + postData);
			this.httpPostMethod(url, postData);

			return;
		}

		this.httpGetMethod(this.reqContent);

	}

	protected void callHttps() throws KeyStoreException,
			NoSuchAlgorithmException, UnrecoverableKeyException,
			KeyManagementException {

		// ca目录
		String caPath = this.caFile.getParent();

		File jksCAFile = new File(caPath + "/"
				+ TenpayHttpClient.JKS_CA_FILENAME);
		logger.info("jks文件路径为: " + jksCAFile.getAbsolutePath());
		if (!jksCAFile.isFile()) {
			/*X509Certificate cert = null;
			FileOutputStream out = null;

			try {
				//cert = (X509Certificate) HttpClientUtil.getCertificate(this.caFile);
				CertificateFactory certCF = CertificateFactory.getInstance("X.509");
			    cert = (X509Certificate) certCF.generateCertificate(new FileInputStream(this.caFile));
			} catch (IOException e) {
				logger.error("没有找到caFile" + e);
			} catch (CertificateException e) {
				logger.error("生成X509Certificate时发生异常：" + e);
			}

			try {
				out = new FileOutputStream(jksCAFile);
			} catch (FileNotFoundException e) {
				logger.error("初始化out失败" + e);
			}

			// store jks file
			try {
				logger.info("--------------" + cert.getVersion());
				logger.info("------------- " + cert.toString()); 
				HttpClientUtil.storeCACert(cert, TenpayHttpClient.JKS_CA_ALIAS,
						TenpayHttpClient.JKS_CA_PASSWORD, out);
			} catch (IOException e) {
				logger.error("storeCACert失败" + e);
			}

			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			coverTokeyStore(this.certFile, jksCAFile, this.certPasswd);

		}

		FileInputStream trustStream = null;
		FileInputStream keyStream = null;
		try {
			trustStream = new FileInputStream(jksCAFile);
			keyStream = new FileInputStream(this.certFile);
		} catch (FileNotFoundException e1) {
			logger.error("初始化keyStream或发生异常" + e1);
		}
		SSLContext sslContext = null;
		logger.info("JKS_CA_PASSWORD:" + JKS_CA_PASSWORD + ",  certPasswd:" + certPasswd);
		try {
			sslContext = HttpClientUtil.getSSLContext(trustStream,
					TenpayHttpClient.JKS_CA_PASSWORD, keyStream,
					this.certPasswd);
			if(sslContext == null) {
				logger.info("callHttps sslContext为空， 无法继续执行！");
				return;
			}
		} catch (CertificateException e) {
			logger.error("callHttps生成sslContext时发生异常：" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 关闭流
		try {
			keyStream.close();
			trustStream.close();
		} catch (Exception e) {
			logger.error("流关闭发生异常：" + e);
		}

		if ("POST".equals(this.method.toUpperCase())) {
			String url = HttpClientUtil.getURL(this.reqContent);
			logger.info("callHttps url: " + url);
			String queryString = HttpClientUtil.getQueryString(this.reqContent);
			byte[] postData = null;
			try {
				postData = queryString.getBytes(this.charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			try {
				this.httpsPostMethod(url, postData, sslContext);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;
		}

		try {
			this.httpsGetMethod(this.reqContent, sslContext);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 以http post方式通信
	 * 
	 * @param url
	 * @param postData
	 * @throws IOException
	 */
	protected void httpPostMethod(String url, byte[] postData)
			throws IOException {

		HttpURLConnection conn = HttpClientUtil.getHttpURLConnection(url);

		this.doPost(conn, postData);
	}

	/**
	 * 以http get方式通信
	 * 
	 * @param url
	 * @throws IOException
	 */
	protected void httpGetMethod(String url) throws IOException {

		HttpURLConnection httpConnection = HttpClientUtil
				.getHttpURLConnection(url);

		this.setHttpRequest(httpConnection);

		httpConnection.setRequestMethod("GET");

		this.responseCode = httpConnection.getResponseCode();

		this.inputStream = httpConnection.getInputStream();

	}

	/**
	 * 以https get方式通信
	 * 
	 * @param url
	 * @param sslContext
	 * @throws IOException
	 */
	protected void httpsGetMethod(String url, SSLContext sslContext)
			throws IOException {

		SSLSocketFactory sf = sslContext.getSocketFactory();

		HttpsURLConnection conn = HttpClientUtil.getHttpsURLConnection(url);

		conn.setSSLSocketFactory(sf);

		this.doGet(conn);

	}

	protected void httpsPostMethod(String url, byte[] postData,
			SSLContext sslContext) throws IOException {

		SSLSocketFactory sf = sslContext.getSocketFactory();

		HttpsURLConnection conn = HttpClientUtil.getHttpsURLConnection(url);

		conn.setSSLSocketFactory(sf);
		this.doPost(conn, postData);

	}

	/**
	 * 设置http请求默认属性
	 * 
	 * @param httpConnection
	 */
	protected void setHttpRequest(HttpURLConnection httpConnection) {

		// 设置连接超时时间
		httpConnection.setConnectTimeout(this.timeOut * 1000);

		// User-Agent
		httpConnection.setRequestProperty("User-Agent",
				TenpayHttpClient.USER_AGENT_VALUE);

		// 不使用缓存
		httpConnection.setUseCaches(false);

		// 允许输入输出
		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);
	}

	/**
	 * 处理应答
	 * 
	 * @throws IOException
	 */
	protected void doResponse() throws IOException {

		if (null == this.inputStream) {
			return;
		}

		// 获取应答内容
		this.resContent = HttpClientUtil.InputStreamTOString(this.inputStream,
				this.charset);

		// 关闭输入流
		this.inputStream.close();

	}

	/**
	 * post方式处理
	 * 
	 * @param conn
	 * @param postData
	 * @throws IOException
	 */
	protected void doPost(HttpURLConnection conn, byte[] postData)
			throws IOException {

		// 以post方式通信
		conn.setRequestMethod("POST");

		// 设置请求默认属性
		this.setHttpRequest(conn);

		// Content-Type
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		BufferedOutputStream out = new BufferedOutputStream(conn
				.getOutputStream());

		final int len = 1024; // 1KB
		HttpClientUtil.doOutput(out, postData, len);

		// 关闭流
		out.close();

		// 获取响应返回状态码
		this.responseCode = conn.getResponseCode();

		// 获取应答输入流
		this.inputStream = conn.getInputStream();

	}

	/**
	 * get方式处理
	 * 
	 * @param conn
	 * @throws IOException
	 */
	protected void doGet(HttpURLConnection conn) throws IOException {

		// 以GET方式通信
		conn.setRequestMethod("GET");

		// 设置请求默认属性
		this.setHttpRequest(conn);

		// 获取响应返回状态码
		this.responseCode = conn.getResponseCode();

		// 获取应答输入流
		this.inputStream = conn.getInputStream();
	}
	
	
	public void coverTokeyStore(File certFile, File jksFile, String password) {  
        try {  
            KeyStore inputKeyStore = KeyStore.getInstance("PKCS12");  
            FileInputStream fis = new FileInputStream(certFile);  
            char[] nPassword = null;  
            if ((password == null)  
                    || password.trim().equals("")) {  
                nPassword = null;  
            } else {  
                nPassword = password.toCharArray();  
            }  
            inputKeyStore.load(fis, nPassword);  
            fis.close();  
            KeyStore outputKeyStore = KeyStore.getInstance("JKS");  
            outputKeyStore.load(null, password.toCharArray());  
            Enumeration enums = inputKeyStore.aliases();  
            while (enums.hasMoreElements()) { // we are readin just one  
                                                // certificate.  
                String keyAlias = (String) enums.nextElement();  
                System.out.println("alias=[" + keyAlias + "]");  
                if (inputKeyStore.isKeyEntry(keyAlias)) {  
                    Key key = inputKeyStore.getKey(keyAlias, nPassword);  
                    Certificate[] certChain = inputKeyStore  
                            .getCertificateChain(keyAlias);  
                    outputKeyStore.setKeyEntry(keyAlias, key, password  
                            .toCharArray(), certChain);  
                }  
            }  
            FileOutputStream out = new FileOutputStream(jksFile);  
            outputKeyStore.store(out, password.toCharArray());  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 

}
