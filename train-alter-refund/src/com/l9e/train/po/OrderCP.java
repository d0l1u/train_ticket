package com.l9e.train.po;

import org.codehaus.jackson.annotate.JsonProperty;

//退款车票的实体类
public class OrderCP {

	String orderId;
	@JsonProperty("cpId") 
	String cpId;
	String buyMoney;
	String alterBuyMoney;
	String refundMoney;
	String refund12306Money;
	String optionTime;
	String optRen;
	String ticketType;
	String trainBox;
	String alterTrainBox;
	String seatNo;
	String alterSeatNo;
	String idsType;
	String userName;
	String userIds;
	String errorInfo;
	String returnOptlog;
	String refund12306Seq;
	String orderStatus;
	
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
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getBuyMoney() {
		return buyMoney;
	}
	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}
	public String getAlterBuyMoney() {
		return alterBuyMoney;
	}
	public void setAlterBuyMoney(String alterBuyMoney) {
		this.alterBuyMoney = alterBuyMoney;
	}
	public String getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}
	public String getRefund12306Money() {
		return refund12306Money;
	}
	public void setRefund12306Money(String refund12306Money) {
		this.refund12306Money = refund12306Money;
	}
	public String getOptionTime() {
		return optionTime;
	}
	public void setOptionTime(String optionTime) {
		this.optionTime = optionTime;
	}
	public String getOptRen() {
		return optRen;
	}
	public void setOptRen(String optRen) {
		this.optRen = optRen;
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
	public String getIdsType() {
		return idsType;
	}
	public void setIdsType(String idsType) {
		this.idsType = idsType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public String getReturnOptlog() {
		return returnOptlog;
	}
	public void setReturnOptlog(String returnOptlog) {
		this.returnOptlog = returnOptlog;
	}
	public String getRefund12306Seq() {
		return refund12306Seq;
	}
	public void setRefund12306Seq(String refund12306Seq) {
		this.refund12306Seq = refund12306Seq;
	}
}
