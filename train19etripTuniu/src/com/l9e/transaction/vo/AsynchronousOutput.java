package com.l9e.transaction.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.l9e.transaction.service.TuniuCommonService;

/**
 * 途牛异步输出
 * 
 * @author licheng
 * 
 */
public class AsynchronousOutput {

	/**
	 * 途牛接入账号
	 */
	private String account;
	/**
	 * 请求签名
	 */
	private String sign;
	/**
	 * 时间戳yyyy-MM-dd HH:mm:ss
	 */
	private String timestamp;
	/**
	 * 结果码
	 */
	@JsonIgnore
	private String returnCode = TuniuCommonService.RETURN_CODE_SUCCESS;
	/**
	 * 异常错误信息
	 */
	private String errorMsg = "";
	/**
	 * 返回结果
	 */
	private String data;
	
	/*输出参数*/
	/**
	 * 实际返回结果码
	 */
	@JsonProperty("returnCode")
	private Integer _returnCode;

	public AsynchronousOutput() {
		super();
	}

	public AsynchronousOutput(String account, String sign, String timestamp,
			String returnCode, String errorMsg) {
		super();
		this.account = account;
		this.sign = sign;
		this.timestamp = timestamp;
		this.returnCode = returnCode;
		this.errorMsg = errorMsg;
	}

	public AsynchronousOutput(String account, String sign, String timestamp,
			String returnCode, String errorMsg, String data) {
		super();
		this.account = account;
		this.sign = sign;
		this.timestamp = timestamp;
		this.returnCode = returnCode;
		this.errorMsg = errorMsg;
		this.data = data;
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

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Integer get_returnCode() {
		if(returnCode == null) 
			_returnCode = null;
		else {
			if(returnCode.equals("") || !returnCode.matches("\\d+")) {
				returnCode = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
				errorMsg = "未知异常";
			}
		}
		try {
			_returnCode = Integer.valueOf(returnCode);
		} catch (NumberFormatException e) {

		}
		return _returnCode;
	}

	public void set_returnCode(Integer returnCode) {
		_returnCode = returnCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
