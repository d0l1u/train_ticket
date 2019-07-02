package com.l9e.train.po;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Order {
	
	
	
	
	public static String ORDER_ERROR = "1001"; 
	public static String ORDER_SUCCESS = "0000"; 
	
	public static String STATUS_PAY_START= "55";//开始支付
	public static String STATUS_PAY_ING= "66";//正在支付
	public static String STATUS_PAY_MANUAL= "61";//支付人工
	public static String STATUS_PAY_FAILURE= "77";//支付失败
	public static String STATUS_PAY_SUCCESS= "88";//支付成功
	public static String STATUS_BILL_SUCCESS= "99";//订单成功
	public static String STATUS_BILL_FAILURE= "10";//订单失败
	
	public static String STATUS_PAY_MQS= "46";//等待支付消息队列

	@JsonProperty("orderId")  
	public String orderId;
	
	public String orderstr;
	
	public String orderStatus;
	
	@JsonProperty("retValue")  
	public String retValue;//返回值
	
	@JsonProperty("retInfo")  
	public String retInfo;//返回信息
	
	@JsonProperty("outTicketBillno")  
	public String outTicketBillNo;
	
	public String userName;
	
	public Integer accountId;
	
	public List<String> orderCps;
	
	@JsonProperty("cps")  
	public List<OrderCP> cps;

	
	@JsonProperty("summoney")  
	public String buymoney;
	
	public String paymoney;
	
	@JsonProperty("from")  
	public String from;
	
	@JsonProperty("to")  
	public String to;
	
	@JsonProperty("seattime")  
	public String seattime;
	
	@JsonProperty("trainno")  
	public String trainno;
	
	@JsonProperty("paybillno")  
	public String paybillno;
	
	public String robotNum;
	
	public String balance;
	
	/**
	 * 订单级别
	 */
	public String level;
	
	/**
	 * 出票时间
	 */
	private Date outTicketTime;

	/**
	 * 列车发车时间
	 */
	private Date fromTime;
	
	/**
	 * 点击那个按钮的标识
	 * 00：批量支付按钮  11：批量反支按钮
	 */
	public String isClickButton;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPaybillno() {
		return paybillno;
	}

	public void setPaybillno(String paybillno) {
		this.paybillno = paybillno;
	}

	public String getBuymoney() {
		return buymoney;
	}

	public void setBuymoney(String buymoney) {
		this.buymoney = buymoney;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSeattime() {
		return seattime;
	}

	public void setSeattime(String seattime) {
		this.seattime = seattime;
	}

	public String getTrainno() {
		return trainno;
	}

	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}

	public String getRetInfo() {
		return retInfo;
	}

	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}

	
	public List<OrderCP> getCps() {
		return cps;
	}

	public void setCps(List<OrderCP> cps) {
		this.cps = cps;
	}
	
	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	
	
	public String getOutTicketBillNo() {
		return outTicketBillNo;
	}

	public void setOutTicketBillNo(String outTicketBillNo) {
		this.outTicketBillNo = outTicketBillNo;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRobotNum() {
		return robotNum;
	}

	public void setRobotNum(String robotNum) {
		this.robotNum = robotNum;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(Date outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public String getIsClickButton() {
		return isClickButton;
	}

	public void setIsClickButton(String isClickButton) {
		this.isClickButton = isClickButton;
	}
	
	
}
