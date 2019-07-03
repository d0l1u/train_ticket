package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * qunar接口账号
 * @author zhangjun
 *
 */
public class InterAccount implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;//接口名称 qunar1、qunar2
	
	private String merchantCode;//代理商账号
	
	private String md5Key;//md5加密密钥

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}
	
}
