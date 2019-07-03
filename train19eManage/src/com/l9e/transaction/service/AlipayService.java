package com.l9e.transaction.service;

public interface AlipayService {
	/**
	 * 查询签证协议结果
	 * @param apliapayAccount
	 * @return
	 */
	public boolean queryConsttommer(String apliapayAccount);
	
	
	
	
	/**
	 * 发起签证请求 --- 支付宝回传签证URL
	 * @param apliapayAccount
	 * @return
	 */
	public String doVisa (String apliapayAccount);
}
