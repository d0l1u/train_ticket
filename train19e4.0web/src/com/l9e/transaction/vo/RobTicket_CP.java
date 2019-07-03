package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.l9e.util.DateUtil;

/**
 * 单个车票信息
 * 
 * @author yangwei01
 */
public class RobTicket_CP implements Serializable {
	private static final long serialVersionUID = 2682974745875528606L;

	private String cpId;

	private String orderId;

	private String userName;

	private Integer ticketType;

	private String certType;

	private String certNo;

	private String telephone;

	private String createTime;

	private String payMoney;

	private String buyMoney;
	@JSONField(format=DateUtil.DATE_FMT3)
	private Date modifyTime;

	private String seatType;

	private String trainNo;

	private String trainBox;

	private String seatNo;

	private String checkStatus;

	private String payMoneyExt;

	private String buyMoneyExt;
	private String orderTicketPrice;
	private String orderTicketSeat;
	private String refundStatus;
	
	public static final String REFUNDING = "开始退票";
	public static final String REFUND_SUCC = "退票成功";
	public static final String REFUND_FAIL = "退票失败";
	public static final String REFUND_REQ = "正在退票";

	public String getOrderTicketPrice() {
		return orderTicketPrice;
	}

	public void setOrderTicketPrice(String orderTicketPrice) {
		this.orderTicketPrice = orderTicketPrice;
	}

	public String getOrderTicketSeat() {
		return orderTicketSeat;
	}

	public void setOrderTicketSeat(String orderTicketSeat) {
		this.orderTicketSeat = orderTicketSeat;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId == null ? null : cpId.trim();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public Integer getTicketType() {
		return ticketType;
	}

	public void setTicketType(Integer ticketType) {
		this.ticketType = ticketType;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType == null ? null : certType.trim();
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo == null ? null : certNo.trim();
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone == null ? null : telephone.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime == null ? null : createTime.trim();
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney == null ? null : payMoney.trim();
	}

	public String getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney == null ? null : buyMoney.trim();
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo == null ? null : trainNo.trim();
	}

	public String getTrainBox() {
		return trainBox;
	}

	public void setTrainBox(String trainBox) {
		this.trainBox = trainBox == null ? null : trainBox.trim();
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo == null ? null : seatNo.trim();
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus == null ? null : checkStatus.trim();
	}

	public String getPayMoneyExt() {
		return payMoneyExt;
	}

	public void setPayMoneyExt(String payMoneyExt) {
		this.payMoneyExt = payMoneyExt == null ? null : payMoneyExt.trim();
	}

	public String getBuyMoneyExt() {
		return buyMoneyExt;
	}

	public void setBuyMoneyExt(String buyMoneyExt) {
		this.buyMoneyExt = buyMoneyExt == null ? null : buyMoneyExt.trim();
	}

}