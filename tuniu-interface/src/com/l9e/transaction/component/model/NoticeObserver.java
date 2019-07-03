package com.l9e.transaction.component.model;

import java.util.Map;

/**
 * 通知观察
 * @author licheng
 *
 */
public interface NoticeObserver {
	
	/**
	 * 预订
	 */
	String BOOK = "book";
	/**
	 * 出票
	 */
	String OUT = "out";
	/**
	 * 取消
	 */
	String CANCEL = "cancel";
	/**
	 * 退款
	 */
	String REFUND = "refund";
	/**
	 * 改签
	 */
	String CHANGE = "change";

	/**
	 * 获取参数集合 get
	 * @return
	 */
	Map<String, String> getParameters();
	
	/**
	 * 获取输出实体 post
	 * @return
	 */
	String getEntity();
	/**
	 * 请求发出前
	 */
	void beforeRequest();
	/**
	 * 请求结束后处理响应字符串
	 * @param content
	 */
	void afterResponse(String content);
}
