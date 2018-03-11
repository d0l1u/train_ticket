package com.l9e.transaction.thread;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.transaction.component.model.NoticeObserver;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 通知请求对象
 * 
 * @author licheng
 * 
 */
public class SimpleRequest implements Runnable {

	private static final Logger logger = Logger.getLogger(SimpleRequest.class);

	/**
	 * get请求方式
	 */
	public static final int METHOD_GET = -234;
	/**
	 * post请求方式
	 */
	public static final int METHOD_POST = -543;
	/**
	 * 请求方式
	 */
	private Integer method;
	/**
	 * 地址
	 */
	private String url;
	/**
	 * 字符集
	 */
	private String charset = "UTF-8";
	/**
	 * 通知观察处理
	 */
	private NoticeObserver observer;

	public SimpleRequest(Integer method, String url, NoticeObserver observer) {
		super();
		this.method = method;
		this.url = url;
		this.observer = observer;
	}

	@Override
	public void run() {
		
		try {
			Map<String, String> parameterMap = null;

			if (method == null)
				method = METHOD_POST;
			if (StringUtils.isEmpty(url)) {
				logger.info("请求地址异常url: " + url );
				return;
			}

			if (observer == null) {
				logger.info("没有设置响应处理对象，停止继续请求，url：" + url);
				return;
			}

			String response = "";
			/* 请求开始 */
			observer.beforeRequest();
			if (method == METHOD_GET) {// get请求
				parameterMap = observer.getParameters();
				String requestUrl = UrlFormatUtil.createUrl(url, parameterMap);
				logger.info("get请求地址：" + requestUrl);
				response = HttpUtil
						.sendByGet(requestUrl, charset, "30000", "30000");
			} else if (method == METHOD_POST) {// post请求
				String params = observer.getEntity();
				params = params == null ? "" : params;
				logger.info("post请求地址: " + url + ", 参数: " + params);
				response = HttpUtil.sendByPost(url, params, charset);
			} else {
				logger.info("请求方式非法，method：" + method);
			}

			logger.info("请求响应结果: " + response);
			observer.afterResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
