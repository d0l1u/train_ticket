package com.l9e.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * @ClassName: HttpClientUtil
 * @Description: http工具类
 * @author: taoka
 * @date: 2017年12月14日 上午11:03:47
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
public class HttpClientUtilNew {

	private static Logger logger = Logger.getLogger(HttpClientUtilNew.class);

	private static PoolingHttpClientConnectionManager connectionManager;
	private static RequestConfig requestConfig;
	private static final String DEFAULT_CHARSET = "UTF-8";

	static {
		requestConfig = RequestConfig.custom()
				// socket读数据超时时间:从服务器获取响应数据的超时时间
				.setSocketTimeout(10 * 1000)
				// 与服务器连接超时时间:httpclient会创建一个异步线程用以创建socket连接,此处设置该socket的连接超时时间
				.setConnectTimeout(10 * 1000)
				// 从连接池中获取连接的超时时间
				.setConnectionRequestTimeout(3 * 1000).build();

		connectionManager = new PoolingHttpClientConnectionManager();
		// 整个连接池最大连接数
		connectionManager.setMaxTotal(300);
		// 每路由最大连接数，默认值是2，这个值不会比MaxTotal大。例如：这个值设置为100.
		// 即代表访问12306的连接最大100个。访问tuniu的连接最大100个
		connectionManager.setDefaultMaxPerRoute(300);
		// 连接不活动时，否检测连接可用，默认 2000 毫秒， 传入正数标示启用该功能，负数表示禁用该功能
		// connectionManager.setValidateAfterInactivity(2000);

	}

	public String get(String url, Map<String, String> parameters, int timeout, String charset) throws IOException {

		// 将参数拼接成URL路径
		StringBuffer sb = new StringBuffer(url);
		sb.append("?");

		Set<Entry<String, String>> entrySet = parameters.entrySet();
		for (Entry<String, String> entry : entrySet) {
			sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset));
		}
		url = sb.toString();
		return get(url, timeout, charset);
	}

	public String get(String url, int timeout, String charset) throws IOException {
		// 获取http请求
		HttpRequestBase httpRequest = getHttpRequest(url, HttpMethod.GET);
		httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
	
		// 获取请求配置
		RequestConfig config = buildRequestConfig(timeout);
		httpRequest.setConfig(config);

		// 获取客户端
		CloseableHttpClient httpClient = getHttpClient(config);

		// 执行请iqu
		String result = execute(httpRequest, httpClient, charset);

		return result;
	}

	public String postJson(String url, String parameters, int timeout, String charset) throws IOException {
		// 获取http请求
		HttpPost httpPost = (HttpPost) getHttpRequest(url, HttpMethod.POST);
		httpPost.addHeader("Content-Type", "application/json ; charset=" + charset);

		// 获取请求配置
		RequestConfig config = buildRequestConfig(timeout);
		httpPost.setConfig(config);

		// 添加请求参数
		if (StringUtils.isNotBlank(parameters)) {
			httpPost.setEntity(new StringEntity(parameters, charset));
		}

		// 获取客户端
		CloseableHttpClient httpClient = getHttpClient(config);

		// 执行请求
		String result = execute(httpPost, httpClient, charset);

		return result;
	}
	
	
	
	public String post(String url, Map<String,String> parameters, int timeout, String charset) throws IOException {
		// 获取http请求
		HttpPost httpPost = (HttpPost) getHttpRequest(url, HttpMethod.POST);
		
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset" + charset);
			
		// 获取请求配置
		RequestConfig config = buildRequestConfig(timeout);
		httpPost.setConfig(config);

		// 添加请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
        Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();
        while(iterator.hasNext()){  
            Entry<String,String> elem = (Entry<String, String>) iterator.next();
            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
        }  
        if(list.size() > 0){
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
            httpPost.setEntity(entity);
        }  
			
		// 获取客户端
		CloseableHttpClient httpClient = getHttpClient(config);

		// 执行请iqu
		String result = execute(httpPost, httpClient, charset);

		return result;
	}
	
	
	public String post(String url, String parameters, int timeout, String charset) throws IOException {
		
		Map<String,String> parameterMap=new HashMap<String,String>();
		
		String [] str =parameters.split("&");
 		for(String kv:str) {
 			String [] nameValue=kv.split("=");
 			parameterMap.put(nameValue[0], nameValue[1]);
 		}
		
		return post(url,parameterMap,timeout,charset);
	}
	
	
	

	private String execute(HttpRequestBase httpRequest, CloseableHttpClient httpClient, String charset)
			throws IOException {
		long begin = System.currentTimeMillis();
		String uuid = "[";
		for (int i = 0; i < 5; i++) {
			uuid = uuid + new Random().nextInt(9);
		}
		uuid = uuid + "] ";

		logger.info(uuid + "-------------------------");
		logger.info(uuid + "请求路径:" + httpRequest.getURI().toString());
		logger.info(uuid + "字符集:" + charset);
		logger.info(uuid + "建立连接超时:" + httpRequest.getConfig().getConnectTimeout() + "ms");
		logger.info(uuid + "数据传输超时:" + httpRequest.getConfig().getSocketTimeout() + "ms");

		CloseableHttpResponse response = null;
		IOException exception = null;
		try {
			response = httpClient.execute(httpRequest);
			// 将响应数据转换成数组
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = inputStream.read(b, 0, 1024)) > 0) {
				out.write(b, 0, i);
			}
			byte[] bytes = out.toByteArray();
			return new String(bytes, charset);
		} catch (SocketTimeoutException e) {
			exception = e;
		} catch (IOException e) {
			exception = e;
		} finally {
			// 释放资源
			try {
				if (response != null) {
					response.close();
				}
				/*if (response != null && response.getEntity() != null) {
					EntityUtils.consume(response.getEntity());
				}*/
				if (httpRequest != null) {
					httpRequest.abort();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (Exception e2) {
				logger.info(uuid + "释放资源异常", e2);
			}

			if (exception != null) {
				logger.info(uuid + "请求异常，耗时:" + (System.currentTimeMillis() - begin) + " ms 【"
						+ exception.getClass().getSimpleName() + "】: " + exception.getMessage(),exception);
				//throw exception;
			} else {
				logger.info(uuid + "请求耗时:" + (System.currentTimeMillis() - begin));
			}
		}
		return null;
	}

	/**
	 * 获取请求
	 * 
	 * @author: taoka
	 * @date: 2017年12月14日 下午3:24:50
	 * @param url
	 * @param httpMethod
	 * @param timeout
	 * @return HttpRequestBase
	 */
	private HttpRequestBase getHttpRequest(String url, HttpMethod httpMethod) {
		HttpRequestBase httpRequest = null;
		switch (httpMethod) {
		case POST:
			httpRequest = new HttpPost(url);
			break;
		case GET:
			httpRequest = new HttpGet(url);
			break;
		default:
			throw new RuntimeException("传入HttpMethod错误");
		}
		return httpRequest;

	}

	/**
	 * 构造请求配置
	 * 
	 * @author: taoka
	 * @date: 2017年12月14日 下午3:15:04
	 * @param timeout
	 * @return RequestConfig
	 */
	private RequestConfig buildRequestConfig(int timeout) {
		if (timeout <= 0) {
			return RequestConfig.copy(requestConfig).build();
		}

		return RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.setConnectionRequestTimeout(3 * 1000).build();
	}

	/**
	 * 获取客户端
	 * 
	 * @author: taoka
	 * @date: 2017年12月14日 下午3:04:16
	 * @param requestConfig
	 * @return CloseableHttpClient
	 */
	private CloseableHttpClient getHttpClient(RequestConfig requestConfig) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				// 连接池
				.setConnectionManager(connectionManager)
				// 共享连接池，有连接池管理连接
				.setConnectionManagerShared(true);

		if (requestConfig == null) {
			httpClientBuilder.setDefaultRequestConfig(HttpClientUtilNew.requestConfig);
		} else {
			// 设置单个连接的连接配置
			httpClientBuilder.setDefaultRequestConfig(requestConfig);
		}

		return httpClientBuilder.build();
	}

	public HttpClientUtilNew() {
		super();
	}

	public HttpClientUtilNew(PoolingHttpClientConnectionManager connectionManager, RequestConfig requestConfig) {
		super();
		HttpClientUtilNew.connectionManager = connectionManager;
		HttpClientUtilNew.requestConfig = requestConfig;
	}

	public PoolingHttpClientConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public static enum HttpMethod {
		GET(), POST();
	}

}
