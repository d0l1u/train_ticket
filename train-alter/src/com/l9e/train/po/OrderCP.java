package com.l9e.train.po;

import org.codehaus.jackson.annotate.JsonProperty;

//退款车票的实体类
public class OrderCP {

	String orderId;
	String changId;
	String newCpId;
	@JsonProperty("cpId") 
	String cpId;
	
	
	String buyMoney;
	String changeBuyMoney;
	String refundMoney;
	String refund12306Money;
	String seatType;
	String changeSeatType;
	public String getChangeSeatType() {
		return changeSeatType;
	}
	public void setChangeSeatType(String changeSeatType) {
		this.changeSeatType = changeSeatType;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	//String optionTime;
	//String optRen;
	String ticketType;
	public String getChangId() {
		return changId;
	}
	public void setChangId(String changId) {
		this.changId = changId;
	}
	public String getNewCpId() {
		return newCpId;
	}
	public void setNewCpId(String newCpId) {
		this.newCpId = newCpId;
	}
	public String getChangeBuyMoney() {
		return changeBuyMoney;
	}
	public void setChangeBuyMoney(String changeBuyMoney) {
		this.changeBuyMoney = changeBuyMoney;
	}
	public String getChangeTrainBox() {
		return changeTrainBox;
	}
	public void setChangeTrainBox(String changeTrainBox) {
		this.changeTrainBox = changeTrainBox;
	}
	public String getChangeSeatNo() {
		return changeSeatNo;
	}
	public void setChangeSeatNo(String changeSeatNo) {
		this.changeSeatNo = changeSeatNo;
	}
	String trainBox;
	String changeTrainBox;
	String seatNo;
	String changeSeatNo;
	String idsType;
	String userName;
	String userIds;
	//String errorInfo;
	//String returnOptlog;
	//String refund12306Seq;
	//String orderStatus;
	
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

	public String getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
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

}
