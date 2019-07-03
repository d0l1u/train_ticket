package com.l9e.transaction.vo;

import java.util.Date;

/**
 * 订单信息实体
 * 
 * @author licheng
 * 
 */
public class OrderVo {
	
	/**
	 * 订单状态：开始出票
	 */
	public static final String STATUS_START_OUT_TICKET = "00";
	/**
	 * 订单状态：重发出票
	 */
	public static final String STATUS_RESEND_OUT_TICKET = "01";
	/**
	 * 订单状态：正在预订
	 */
	public static final String STATUS_BOOKING = "11";
	/**
	 * 订单状态：12306排队
	 */
	public static final String STATUS_12306_QUEUE = "15";
	/**
	 * 订单状态：预订失败
	 */
	public static final String STATUS_BOOK_FAIL = "22";
	/**
	 * 订单状态：预订成功
	 */
	public static final String STATUS_BOOK_SUCCESS = "33";
	/**
	 * 订单状态：预订人工
	 */
	public static final String STATUS_BOOK_MANUAL = "44";
	/**
	 * 订单状态：开始支付
	 */
	public static final String STATUS_START_PAY = "55";
	/**
	 * 订单状态：重发支付
	 */
	public static final String STATUS_RESEND_PAY = "56";
	/**
	 * 订单状态：支付人工
	 */
	public static final String STATUS_PAY_MANUAL = "61";
	/**
	 * 订单状态：正在支付
	 */
	public static final String STATUS_PAYING = "66";
	/**
	 * 订单状态：支付失败
	 */
	public static final String STATUS_PAY_FAIL = "77";
	/**
	 * 订单状态：支付成功
	 */
	public static final String STATUS_PAY_SUCCESS = "88";
	/**
	 * 订单状态：支付核对
	 */
	public static final String STATUS_PAY_VERIFY = "81";
	/**
	 * 订单状态：正在取消
	 */
	public static final String STATUS_CANCELING = "83";
	/**
	 * 订单状态：出票成功
	 */
	public static final String STATUS_OUT_TICKET_SUCCESS = "99";
	/**
	 * 订单状态：支付成功
	 */
	public static final String STATUS_OUT_TICKET_FAIL = "10";
	/**
	 * 订单状态：支付成功
	 */
	public static final String STATUS_VERIFY_MANUAL = "82";
	/**
	 * 订单状态：支付成功
	 */
	public static final String STATUS_START_CANCEL = "85";

	/**
	 * 订单主键id
	 */
	private String id;
	/**
	 * 支付金额
	 */
	private Double payMoney;
	/**
	 * 成本金额
	 */
	private Double buyMoney;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 支付时间
	 */
	private Date payTime;
	/**
	 * 出票时间
	 */
	private Date outTicketTime;
	/**
	 * 取票单号
	 */
	private String billno;
	/**
	 * 出发时间
	 */
	private Date fromTime;
	/**
	 * 到达时间
	 */
	private Date toTime;
	/**
	 * 支付账号
	 */
	private String payAccount;
	/**
	 * 支付流水
	 */
	private String paySeq;
	/**
	 * 错误码
	 */
	private String errorInfo;
	/**
	 * 12306账号主键id
	 */
	private Integer accountId;
	/**
	 * 机器人主键id
	 */
	private Integer workerId;

	public OrderVo() {

	}

	public OrderVo(String id, Double payMoney, Double buyMoney, String status,
			Date payTime, Date outTicketTime, String billno, Date fromTime,
			Date toTime, String payAccount, String paySeq, String errorInfo,
			Integer accountId, Integer workerId) {
		super();
		this.id = id;
		this.payMoney = payMoney;
		this.buyMoney = buyMoney;
		this.status = status;
		this.payTime = payTime;
		this.outTicketTime = outTicketTime;
		this.billno = billno;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.payAccount = payAccount;
		this.paySeq = paySeq;
		this.errorInfo = errorInfo;
		this.accountId = accountId;
		this.workerId = workerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	public Double getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(Double buyMoney) {
		this.buyMoney = buyMoney;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(Date outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getPaySeq() {
		return paySeq;
	}

	public void setPaySeq(String paySeq) {
		this.paySeq = paySeq;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

	@Override
	public String toString() {
		return "Order [accountId=" + accountId + ", billno=" + billno
				+ ", buyMoney=" + buyMoney + ", errorInfo=" + errorInfo
				+ ", fromTime=" + fromTime + ", id=" + id + ", outTicketTime="
				+ outTicketTime + ", payAccount=" + payAccount + ", payMoney="
				+ payMoney + ", paySeq=" + paySeq + ", payTime=" + payTime
				+ ", status=" + status + ", toTime=" + toTime + ", workerId="
				+ workerId + "]";
	}

}
