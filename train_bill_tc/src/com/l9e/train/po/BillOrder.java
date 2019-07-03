package com.l9e.train.po;

import java.util.Date;

public class BillOrder {

	/**
	 * 准备通知
	 */
	public static final String NOTIFY_PREPARE = "00";
	/**
	 * 正在通知
	 */
	public static final String NOTIFY_ING = "11";
	/**
	 * 通知成功
	 */
	public static final String NOTIFY_SUCCESS = "22";
	/**
	 * 通知失败
	 */
	public static final String NOTIFY_FAIL = "33";

	/**
	 * 主键
	 */
	private Integer billId;
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 12306出票单号
	 */
	private String out_ticket_billno;
	/**
	 * 结算金额
	 */
	private Double amount;
	/**
	 * 结算类型
	 */
	private Integer settlementType;
	/**
	 * 票数
	 */
	private Integer quantity;
	/**
	 * 交易时间
	 */
	private Date tradeDate;
	/**
	 * 结算归属日期
	 */
	private Date settlementDate;
	/**
	 * 供应商
	 */
	private String channel;
	/**
	 * 余额
	 */
	private Double accountBalance;

	/* 通知 */
	/**
	 * 通知状态
	 */
	private String notifyStatus;
	/**
	 * 通知次数
	 */
	private Integer notifyNum;
	/**
	 * 通知时间
	 */
	private Date notifyTime;
	/**
	 * 通知结束时间
	 */
	private Date notifyFinishTime;

	public BillOrder() {
		super();
	}

	public BillOrder(Integer billId, String orderId, String outTicketBillno,
			Double amount, Integer settlementType, Integer quantity,
			Date tradeDate, Date settlementDate, String channel,
			Double accountBalance, String notifyStatus, Integer notifyNum,
			Date notifyTime, Date notifyFinishTime) {
		super();
		this.billId = billId;
		this.orderId = orderId;
		out_ticket_billno = outTicketBillno;
		this.amount = amount;
		this.settlementType = settlementType;
		this.quantity = quantity;
		this.tradeDate = tradeDate;
		this.settlementDate = settlementDate;
		this.channel = channel;
		this.accountBalance = accountBalance;
		this.notifyStatus = notifyStatus;
		this.notifyNum = notifyNum;
		this.notifyTime = notifyTime;
		this.notifyFinishTime = notifyFinishTime;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}

	public void setOut_ticket_billno(String outTicketBillno) {
		out_ticket_billno = outTicketBillno;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(Integer settlementType) {
		this.settlementType = settlementType;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
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

	public Date getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}

	public Date getNotifyFinishTime() {
		return notifyFinishTime;
	}

	public void setNotifyFinishTime(Date notifyFinishTime) {
		this.notifyFinishTime = notifyFinishTime;
	}

}
