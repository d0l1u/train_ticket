package com.train.system.center.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Refund {

	/** 乘客唯一编号 */
	private String cpId;

	private String name;

	/** 分销商ID */
	private String orderId;

	/** 我方ID */
	private String myOrderId;

	/** 供货商ID */
	private String supplierOrderId;
	private String refundSeq;
	private BigDecimal buyMoney;
	private BigDecimal alterDiffMoney;
	private BigDecimal alterBuyMoney;
	private BigDecimal refundMoney;
	private BigDecimal refundKyfwMoney;
	private String refundPercent;
	private String orderStatus;
	private Date createTime;
	private Date optionTime;
	private Date outTicketTime;
	private String sequence;
	private String trainCode;
	private String alterTrainCode;
	private String fromStationName;
	private String arriveStationName;
	private Date fromTime;
	private Date alterFromTime;
	private Date travelTime;
	private Date alterTravelTime;
	private String seatType;
	private String alterSeatType;
	private String ticketType;
	private String trainBox;
	private String alterTrainBox;
	private String seatNo;
	private String alterSeatNo;
	private String cardType;
	private String cardNo;
	private String errorInfo;
	private String channel;
	private String refuseReason;

	private String returnLog;
	private String refundKyfwSeq;
	private Date verifyTime;
	private String oldRefundSeq;
	private String refundType;
	private String supplierType;
	private String subSequence;
	
	private String accountUsername;
	private String accountPassword;

	

	public String getAccountUsername() {
		return accountUsername;
	}

	public void setAccountUsername(String accountUsername) {
		this.accountUsername = accountUsername;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMyOrderId() {
		return myOrderId;
	}

	public void setMyOrderId(String myOrderId) {
		this.myOrderId = myOrderId;
	}

	public String getSupplierOrderId() {
		return supplierOrderId;
	}

	public void setSupplierOrderId(String supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}

	public String getRefundSeq() {
		return refundSeq;
	}

	public void setRefundSeq(String refundSeq) {
		this.refundSeq = refundSeq;
	}

	public BigDecimal getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(BigDecimal buyMoney) {
		this.buyMoney = buyMoney;
	}

	public BigDecimal getAlterDiffMoney() {
		return alterDiffMoney;
	}

	public void setAlterDiffMoney(BigDecimal alterDiffMoney) {
		this.alterDiffMoney = alterDiffMoney;
	}

	public BigDecimal getAlterBuyMoney() {
		return alterBuyMoney;
	}

	public void setAlterBuyMoney(BigDecimal alterBuyMoney) {
		this.alterBuyMoney = alterBuyMoney;
	}

	public BigDecimal getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}

	public BigDecimal getRefundKyfwMoney() {
		return refundKyfwMoney;
	}

	public void setRefundKyfwMoney(BigDecimal refundKyfwMoney) {
		this.refundKyfwMoney = refundKyfwMoney;
	}

	public String getRefundPercent() {
		return refundPercent;
	}

	public void setRefundPercent(String refundPercent) {
		this.refundPercent = refundPercent;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getOptionTime() {
		return optionTime;
	}

	public void setOptionTime(Date optionTime) {
		this.optionTime = optionTime;
	}

	public Date getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(Date outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

	public String getAlterTrainCode() {
		return alterTrainCode;
	}

	public void setAlterTrainCode(String alterTrainCode) {
		this.alterTrainCode = alterTrainCode;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public String getArriveStationName() {
		return arriveStationName;
	}

	public void setArriveStationName(String arriveStationName) {
		this.arriveStationName = arriveStationName;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getAlterFromTime() {
		return alterFromTime;
	}

	public void setAlterFromTime(Date alterFromTime) {
		this.alterFromTime = alterFromTime;
	}

	public Date getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(Date travelTime) {
		this.travelTime = travelTime;
	}

	public Date getAlterTravelTime() {
		return alterTravelTime;
	}

	public void setAlterTravelTime(Date alterTravelTime) {
		this.alterTravelTime = alterTravelTime;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getAlterSeatType() {
		return alterSeatType;
	}

	public void setAlterSeatType(String alterSeatType) {
		this.alterSeatType = alterSeatType;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getTrainBox() {
		return trainBox;
	}

	public void setTrainBox(String trainBox) {
		this.trainBox = trainBox;
	}

	public String getAlterTrainBox() {
		return alterTrainBox;
	}

	public void setAlterTrainBox(String alterTrainBox) {
		this.alterTrainBox = alterTrainBox;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getAlterSeatNo() {
		return alterSeatNo;
	}

	public void setAlterSeatNo(String alterSeatNo) {
		this.alterSeatNo = alterSeatNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getReturnLog() {
		return returnLog;
	}

	public void setReturnLog(String returnLog) {
		this.returnLog = returnLog;
	}

	public String getRefundKyfwSeq() {
		return refundKyfwSeq;
	}

	public void setRefundKyfwSeq(String refundKyfwSeq) {
		this.refundKyfwSeq = refundKyfwSeq;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getOldRefundSeq() {
		return oldRefundSeq;
	}

	public void setOldRefundSeq(String oldRefundSeq) {
		this.oldRefundSeq = oldRefundSeq;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	public String getSubSequence() {
		return subSequence;
	}

	public void setSubSequence(String subSequence) {
		this.subSequence = subSequence;
	}
}
