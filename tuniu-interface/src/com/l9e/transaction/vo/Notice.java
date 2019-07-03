package com.l9e.transaction.vo;

/**
 * 通知
 * 
 * @author licheng
 * 
 */
public class Notice {

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 退款号
	 */
	private Integer refundId;
	/**
	 * 车票号
	 */
	private String cpId;
	/**
	 * 出票系统通知状态
	 */
	private String cpNotifyStatus;
	/**
	 * 出票系统通知次数
	 */
	private Integer cpNotifyNum;
	/**
	 * 出票系统通知开始时间(标记字符串，在数据库中更新时间)
	 */
	private String cpNotifyTime;
	/**
	 * 出票系统通知结束时间(标记字符串，在数据库中更新时间)
	 */
	private String cpNotifyFinishTime;
	/**
	 * 结果通知状态
	 */
	private String notifyStatus;
	/**
	 * 结果通知次数
	 */
	private Integer notifyNum;
	/**
	 * 结果异步回调地址
	 */
	private String notifyUrl;
	/**
	 * 结果异步回调开始时间(标记字符串，在数据库中更新时间)
	 */
	private String notifyTime;
	/**
	 * 结果异步回调结束时间(标记字符串，在数据库中更新时间)
	 */
	private String notifyFinishTime;
	
	private String tuniuChangeId;

	public Notice() {
		super();
	}

	public Integer getRefundId() {
		return refundId;
	}

	public void setRefundId(Integer refundId) {
		this.refundId = refundId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCpNotifyStatus() {
		return cpNotifyStatus;
	}

	public void setCpNotifyStatus(String cpNotifyStatus) {
		this.cpNotifyStatus = cpNotifyStatus;
	}

	public Integer getCpNotifyNum() {
		return cpNotifyNum;
	}

	public void setCpNotifyNum(Integer cpNotifyNum) {
		this.cpNotifyNum = cpNotifyNum;
	}

	public String getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public Integer getNotifyNum() {
		return notifyNum;
	}

	public void setNotifyNum(Integer notifyNum) {
		this.notifyNum = notifyNum;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getNotifyFinishTime() {
		return notifyFinishTime;
	}

	public void setNotifyFinishTime(String notifyFinishTime) {
		this.notifyFinishTime = notifyFinishTime;
	}

	public String getCpNotifyTime() {
		return cpNotifyTime;
	}

	public void setCpNotifyTime(String cpNotifyTime) {
		this.cpNotifyTime = cpNotifyTime;
	}

	public String getCpNotifyFinishTime() {
		return cpNotifyFinishTime;
	}

	public void setCpNotifyFinishTime(String cpNotifyFinishTime) {
		this.cpNotifyFinishTime = cpNotifyFinishTime;
	}

	public String getTuniuChangeId() {
		return tuniuChangeId;
	}

	public void setTuniuChangeId(String tuniuChangeId) {
		this.tuniuChangeId = tuniuChangeId;
	}

}
