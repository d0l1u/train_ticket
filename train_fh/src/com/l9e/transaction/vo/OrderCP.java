package com.l9e.transaction.vo;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class OrderCP {

	String orderId;
	@JsonProperty("cpId") 
	String cpId;
	
	String username;
	Integer trainType;
	Integer certType;
	
	@JsonProperty("certNo") 
	String certNo;
	
	@JsonProperty("seatType") 
	String seatType;
	
	@JsonProperty("trainbox") 
	String trainbox;
	
	@JsonProperty("seatNo") 
	String seatNo;
	
	@JsonProperty("paymoney") 
	String paymoney;
	
	@JsonProperty("subOutTicketBillNo")
	String subOutTicketBillNo;
	
	@JsonIgnore 
	public String getTrainbox() {
		return trainbox;
	}
	public void setTrainbox(String trainbox) {
		this.trainbox = trainbox;
	}
	
	@JsonIgnore 
	public String getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}
	
	@JsonIgnore 
	public String getPaymoney() {
		return paymoney;
	}
	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}
	
	@JsonIgnore 
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
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@JsonIgnore 
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public Integer getTrainType() {
		return trainType;
	}
	public void setTrainType(Integer trainType) {
		this.trainType = trainType;
	}
	public Integer getCertType() {
		return certType;
	}
	public void setCertType(Integer certType) {
		this.certType = certType;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public String getSubOutTicketBillNo() {
		return subOutTicketBillNo;
	}
	public void setSubOutTicketBillNo(String subOutTicketBillNo) {
		this.subOutTicketBillNo = subOutTicketBillNo;
	}
}
