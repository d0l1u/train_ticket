package com.l9e.transaction.vo;

/**
 * 同程参数
 * 
 * @author licheng
 * 
 */
public class TCParameterVO {

	/* 改签参数 */
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 交易单号
	 */
	private String transactionId;
	/**
	 * 取票单号
	 */
	private String orderNumber;
	/**
	 * 改签新车票的车次
	 */
	private String changeCheci;
	/**
	 * 改签新车票出发时间，格式 yyyy-MM-dd HH:mm:ss，如：2014-05-30 17:32:00
	 */
	private String changeDatetime;
	/**
	 * 改签新车票的座位席别编码(同程)
	 */
	private String changeZwcode;
	/**
	 * 原票的座位席别编码(同程)
	 */
	private String oldZwcode;
	/**
	 * 改签车票信息，json数组
	 */
	private String ticketInfo;
	/**
	 * 是否异步 Y、是 N、否
	 */
	private String isAsync;
	/**
	 * 异步回调地址
	 */
	private String callbackUrl;
	/**
	 * 请求特征值
	 */
	private String reqtoken;

	public TCParameterVO() {
		super();
	}

	public TCParameterVO(String orderId, String transactionId,
			String orderNumber, String changeCheci, String changeDatetime,
			String changeZwcode, String oldZwcode, String ticketInfo,
			String isAsync, String callbackUrl, String reqtoken) {
		super();
		this.orderId = orderId;
		this.transactionId = transactionId;
		this.orderNumber = orderNumber;
		this.changeCheci = changeCheci;
		this.changeDatetime = changeDatetime;
		this.changeZwcode = changeZwcode;
		this.oldZwcode = oldZwcode;
		this.ticketInfo = ticketInfo;
		this.isAsync = isAsync;
		this.callbackUrl = callbackUrl;
		this.reqtoken = reqtoken;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getChangeCheci() {
		return changeCheci;
	}

	public void setChangeCheci(String changeCheci) {
		this.changeCheci = changeCheci;
	}

	public String getChangeDatetime() {
		return changeDatetime;
	}

	public void setChangeDatetime(String changeDatetime) {
		this.changeDatetime = changeDatetime;
	}

	public String getChangeZwcode() {
		return changeZwcode;
	}

	public void setChangeZwcode(String changeZwcode) {
		this.changeZwcode = changeZwcode;
	}

	public String getOldZwcode() {
		return oldZwcode;
	}

	public void setOldZwcode(String oldZwcode) {
		this.oldZwcode = oldZwcode;
	}

	public String getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(String ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public String getIsAsync() {
		return isAsync;
	}

	public void setIsAsync(String isAsync) {
		this.isAsync = isAsync;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getReqtoken() {
		return reqtoken;
	}

	public void setReqtoken(String reqtoken) {
		this.reqtoken = reqtoken;
	}

}
