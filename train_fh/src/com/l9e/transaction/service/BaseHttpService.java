package com.l9e.transaction.service;

import org.apache.commons.lang.StringUtils;

import com.l9e.util.HttpUtil;


/**
 * 基础http服务
 * @author licheng
 *
 */
public abstract class BaseHttpService {

	/**
	 * post请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集
	 * @param retry 重试次数
	 * @param interval 重试间隔
	 * @return
	 */
	protected String requestPost(String url, String params, String charset, Integer retry, long interval) {
		String result = null;
		int count = 0;
		do {
			if(count != 0 && interval > 100) {
				sleep(interval);
			}
			try {
				result = HttpUtil.sendByPost(url, params, charset, 30000, 30000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			count++;
		} while (StringUtils.isEmpty(result) && count < retry);
		
		return result;
	}
	
	private void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
