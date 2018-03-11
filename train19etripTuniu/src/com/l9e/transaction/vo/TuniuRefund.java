package com.l9e.transaction.vo;

import java.util.Date;

/**
 * 途牛退款记录
 * 
 * @author licheng
 * 
 */
public class TuniuRefund {

	/**
	 * 退款流水号
	 */
	private Integer refundId;
	/**
	 * 途牛退款流水号
	 */
	private String tuniuRefundId;
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 车票号
	 */
	private String cpId;
	/**
	 * 退款流水号
	 */
	private String refundSeq;
	/**
	 * 12306取票单号
	 */
	private String outTicketBillno;
	/**
	 * 退款金额
	 */
	private Double refundMoney;
	/**
	 * 实际退款金额
	 */
	private Double actualRefundMoney;
	/**
	 * 请求方备注
	 */
	private String userRemark;
	/**
	 * 我方备注
	 */
	private String ourRemark;
	/**
	 * 12306退款流水
	 */
	private String refund12306Seq;
	/**
	 * 审核时间
	 */
	private Date verifyTime;
	/**
	 * 失败原因
	 */
	private String failReason;
	/**
	 * 退款状态
	 */
	private String refundStatus;
	/**
	 * 12306详细退款金额
	 */
	private Double detailRefund;
	/**
	 * 12306详细改签金额
	 */
	private Double detailAlter;
	/**
	 * 退款类型
	 */
	private String refundType;
	/**
	 * 退款结果回调地址
	 */
	private String callbackUrl;
	/**
	 * 用户退款时间
	 */
	private Date userRefundTime;

	/**
	 * 操作人
	 */
	private String optPerson;
	
	private String createTime;

	public TuniuRefund() {
		super();
	}

	public Integer getRefundId() {
		return refundId;
	}

	public void setRefundId(Integer refundId) {
		this.refundId = refundId;
	}

	public String getTuniuRefundId() {
		return tuniuRefundId;
	}

	public void setTuniuRefundId(String tuniuRefundId) {
		this.tuniuRefundId = tuniuRefundId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getRefundSeq() {
		return refundSeq;
	}

	public void setRefundSeq(String refundSeq) {
		this.refundSeq = refundSeq;
	}

	public String getOutTicketBillno() {
		return outTicketBillno;
	}

	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}

	public Double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public Double getActualRefundMoney() {
		return actualRefundMoney;
	}

	public void setActualRefundMoney(Double actualRefundMoney) {
		this.actualRefundMoney = actualRefundMoney;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public String getOurRemark() {
		return ourRemark;
	}

	public void setOurRemark(String ourRemark) {
		this.ourRemark = ourRemark;
	}

	public String getRefund12306Seq() {
		return refund12306Seq;
	}

	public void setRefund12306Seq(String refund12306Seq) {
		this.refund12306Seq = refund12306Seq;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Double getDetailRefund() {
		return detailRefund;
	}

	public void setDetailRefund(Double detailRefund) {
		this.detailRefund = detailRefund;
	}

	public Double getDetailAlter() {
		return detailAlter;
	}

	public void setDetailAlter(Double detailAlter) {
		this.detailAlter = detailAlter;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Date getUserRefundTime() {
		return userRefundTime;
	}

	public void setUserRefundTime(Date userRefundTime) {
		this.userRefundTime = userRefundTime;
	}

	public String getOptPerson() {
		return optPerson;
	}

	public void setOptPerson(String optPerson) {
		this.optPerson = optPerson;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
