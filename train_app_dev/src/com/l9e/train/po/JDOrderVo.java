package com.l9e.train.po;

import org.codehaus.jackson.annotate.JsonProperty;

public class JDOrderVo {

	/**
	 * 12306支付金额
	 */
	@JsonProperty("payMoney")  
	String payMoney;
	
	/**
	 * 京东抵扣优惠券后支付金额
	 */
	@JsonProperty("jdPayMoney")  
	String jdPayMoney;
	
	/**
	 * 京东优惠券金额
	 */
	@JsonProperty("jdRebateTicketMoney")  
	String jdRebateTicketMoney;
	
	/**
	 * 京东优惠券编号
	 */
	@JsonProperty("jdRebateTicketId")  
	String jdRebateTicketId;
	
	/**
	 * 开联通流水号
	 */
	@JsonProperty("kltOrderNo ")  
	String kltOrderNo ;
		
	/**
	 * 12306订单号
	 */
	@JsonProperty("outTicketBillno")  
	String outTicketBillno;
	
	/**
	 * 预定结果信息
	 */
	@JsonProperty("retInfo")  
	String retInfo;
	
	/**
	 * 咱们的订单号
	 */
	@JsonProperty("orderId")  
	String orderId;
	
	/**
	 * 预定结果状态
	 */
	@JsonProperty("retValue")  
	String retValue;
	
	/**
	 * 京东订单号
	 */
	@JsonProperty("jdOrderId")  
	String jdOrderId;
	
	/**
	 * 京东流水号
	 */
	@JsonProperty("jdOrderNo")  
	String jdOrderNo;


	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getOutTicketBillno() {
		return outTicketBillno;
	}

	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}

	public String getRetInfo() {
		return retInfo;
	}

	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public String getJdOrderNo() {
		return jdOrderNo;
	}

	public void setJdOrderNo(String jdOrderNo) {
		this.jdOrderNo = jdOrderNo;
	}

	public String getJdPayMoney() {
		return jdPayMoney;
	}

	public void setJdPayMoney(String jdPayMoney) {
		this.jdPayMoney = jdPayMoney;
	}

	public String getJdRebateTicketMoney() {
		return jdRebateTicketMoney;
	}

	public void setJdRebateTicketMoney(String jdRebateTicketMoney) {
		this.jdRebateTicketMoney = jdRebateTicketMoney;
	}

	public String getJdRebateTicketId() {
		return jdRebateTicketId;
	}

	public void setJdRebateTicketId(String jdRebateTicketId) {
		this.jdRebateTicketId = jdRebateTicketId;
	}

	public String getKltOrderNo() {
		return kltOrderNo;
	}

	public void setKltOrderNo(String kltOrderNo) {
		this.kltOrderNo = kltOrderNo;
	}
	
}
