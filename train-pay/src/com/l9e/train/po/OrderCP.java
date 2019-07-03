package com.l9e.train.po;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class OrderCP {

	private String orderId;

	@JsonProperty("cpId")
	private String cpId;

	private String username;
	private Integer trainType;
	private Integer certType;

	@JsonProperty("certNo")
	private String certNo;
	private Integer seatType;

	@JsonProperty("trainbox")
	private String trainbox;

	@JsonProperty("seatNo")
	private String seatNo;

	@JsonProperty("paymoney")
	private String paymoney;

	private String subSequence;

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

	public Integer getSeatType() {
		return seatType;
	}

	public void setSeatType(Integer seatType) {
		this.seatType = seatType;
	}

	public String getSubSequence() {
		return subSequence;
	}

	public void setSubSequence(String subSequence) {
		this.subSequence = subSequence;
	}
}
