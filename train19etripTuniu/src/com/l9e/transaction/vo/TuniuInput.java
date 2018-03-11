package com.l9e.transaction.vo;

/**
 * 途牛传入参数
 * 
 * @author licheng
 * 
 */
public class TuniuInput {

	/**
	 * 途牛接入账号
	 */
	private String account;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 时间戳yyyy-MM-ddHH:mm:ss
	 */
	private String timestamp;
	/**
	 * base64加密数据
	 */
	private String data;
	
	
	/**
	 * 返回码,推送超时订单,有这个字段
	 */
	private String returnCode;

	public TuniuInput() {
		super();
	}

	public TuniuInput(String account, String sign, String timestamp, String data) {
		super();
		this.account = account;
		this.sign = sign;
		this.timestamp = timestamp;
		this.data = data;
	}

	public TuniuInput(String account, String sign, String timestamp,
			String data, String returnCode) {
		super();
		this.account = account;
		this.sign = sign;
		this.timestamp = timestamp;
		this.data = data;
		this.returnCode = returnCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

}
