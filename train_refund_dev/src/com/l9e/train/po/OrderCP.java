package com.l9e.train.po;

public class OrderCP {
	public static String STATUS_REFUND_START = "04";// 开始退票
	public static String STATUS_REFUND_ING = "06";// 正在退票
	public static String STATUS_REFUND_MANUAL = "07";// 退票人工
	public static String STATUS_REFUND_FIN = "08";// 待审核
	public static String STATUS_REFUND_SUC = "33";// 审核退票完成
	public static String STATUS_REFUND_AUTO = "11";// 11退票完成
	public static String STATUS_REFUND_REFUSE = "22";// 22拒绝退票

	private String orderStatus;
	private String orderId;
	private String refundSeq; // 退款流水号
	private String inputCode; // 是否人工打码
	private String accountName; // 12306账号
	private String accountPwd; // 12306密码
	private String outTicketBillno; // 12306订单号
	private String outTicketTime;
	private String trainDate;
	private String cpId;

	private String userName;

	private String ticketType;

	private String idsType;

	private String idsCard;

	private String seatType;

	private String trainBox;

	private String trainNo;

	private String seatNo;

	private String channel;

	private String isRefunding;

	private String account_from_way;// 账号来源

	private String our_remark;// 备注

	private String refuse_reason;// 拒绝退款原因

	private String refundMoney;// 退款金额
	private String alterMyself;// 是否是咱们自己改签的订单 0：否 1：是

	public String getIsRefunding() {
		return isRefunding;
	}

	public void setIsRefunding(String isRefunding) {
		this.isRefunding = isRefunding;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getIdsType() {
		return idsType;
	}

	public void setIdsType(String idsType) {
		this.idsType = idsType;
	}

	public String getIdsCard() {
		return idsCard;
	}

	public void setIdsCard(String idsCard) {
		this.idsCard = idsCard;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getTrainBox() {
		return trainBox;
	}

	public void setTrainBox(String trainBox) {
		this.trainBox = trainBox;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundSeq() {
		return refundSeq;
	}

	public void setRefundSeq(String refundSeq) {
		this.refundSeq = refundSeq;
	}

	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPwd() {
		return accountPwd;
	}

	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}

	public String getOutTicketBillno() {
		return outTicketBillno;
	}

	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}

	public String getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(String outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public String getAccount_from_way() {
		return account_from_way;
	}

	public void setAccount_from_way(String accountFromWay) {
		account_from_way = accountFromWay;
	}

	public String getOur_remark() {
		return our_remark;
	}

	public void setOur_remark(String ourRemark) {
		our_remark = ourRemark;
	}

	public String getRefuse_reason() {
		return refuse_reason;
	}

	public void setRefuse_reason(String refuseReason) {
		refuse_reason = refuseReason;
	}

	public String getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getAlterMyself() {
		return alterMyself;
	}

	public void setAlterMyself(String alterMyself) {
		this.alterMyself = alterMyself;
	}

	public String getTrainDate() {
		return trainDate;
	}

	public void setTrainDate(String trainDate) {
		this.trainDate = trainDate;
	}

}
