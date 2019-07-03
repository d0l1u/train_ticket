package com.l9e.train.po;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Order {
	public static String ORDER_ERROR = "1001";
	public static String ORDER_SUCCESS = "0000";
	public static String STATUS_PAY_START = "55";
	public static String STATUS_PAY_ING = "66";
	public static String STATUS_PAY_MANUAL = "61";
	public static String STATUS_PAY_FAILURE = "77";
	public static String STATUS_PAY_SUCCESS = "88";
	public static String STATUS_BILL_SUCCESS = "99";
	public static String STATUS_BILL_FAILURE = "10";
	public static String STATUS_PAY_MQS = "46";
	@JsonProperty("orderId")
	public String orderId;
	public String orderstr;
	public String orderStatus;
	@JsonProperty("retValue")
	public String retValue;
	@JsonProperty("retInfo")
	public String retInfo;
	@JsonProperty("outTicketBillno")
	public String outTicketBillNo;
	public String userName;
	public Integer accountId;
	public List<String> orderCps;
	@JsonProperty("cps")
	public List<OrderCP> cps;
	@JsonProperty("ticketEntrances")
	public List<TicketEntrance> ticketEntrances;
	@JsonProperty("summoney")
	public String buymoney;
	public String paymoney;
	@JsonProperty("from")
	public String from;
	public String from3c;
	@JsonProperty("to")
	public String to;
	public String to3c;
	@JsonProperty("seattime")
	public String seattime;
	@JsonProperty("trainno")
	public String trainno;
	@JsonProperty("paybillno")
	public String paybillno;
	public String robotNum;
	public String balance;
	public String level;
	private Date outTicketTime;
	private Date fromTime;
	private String tranDate;
	public String isClickButton;

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPaybillno() {
		return this.paybillno;
	}

	public void setPaybillno(String paybillno) {
		this.paybillno = paybillno;
	}

	public String getBuymoney() {
		return this.buymoney;
	}

	public void setBuymoney(String buymoney) {
		this.buymoney = buymoney;
	}

	public String getPaymoney() {
		return this.paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSeattime() {
		return this.seattime;
	}

	public void setSeattime(String seattime) {
		this.seattime = seattime;
	}

	public String getTrainno() {
		return this.trainno;
	}

	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}

	public String getRetInfo() {
		return this.retInfo;
	}

	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}

	public List<OrderCP> getCps() {
		return this.cps;
	}

	public void setCps(List<OrderCP> cps) {
		this.cps = cps;
	}

	public String getRetValue() {
		return this.retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public String getOutTicketBillNo() {
		return this.outTicketBillNo;
	}

	public void setOutTicketBillNo(String outTicketBillNo) {
		this.outTicketBillNo = outTicketBillNo;
	}

	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRobotNum() {
		return this.robotNum;
	}

	public void setRobotNum(String robotNum) {
		this.robotNum = robotNum;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getOutTicketTime() {
		return this.outTicketTime;
	}

	public void setOutTicketTime(Date outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public Date getFromTime() {
		return this.fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public String getIsClickButton() {
		return this.isClickButton;
	}

	public void setIsClickButton(String isClickButton) {
		this.isClickButton = isClickButton;
	}

	public List<TicketEntrance> getTicketEntrances() {
		return this.ticketEntrances;
	}

	public void setTicketEntrances(List<TicketEntrance> ticketEntrances) {
		this.ticketEntrances = ticketEntrances;
	}

	public String getFrom3c() {
		return this.from3c;
	}

	public void setFrom3c(String from3c) {
		this.from3c = from3c;
	}

	public String getTo3c() {
		return this.to3c;
	}

	public void setTo3c(String to3c) {
		this.to3c = to3c;
	}

	public String getTranDate() {
		return this.tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
}
