package com.l9e.train.po;

public class Result {
	public  static String SUCCESS = "success";
	public  static String FAILURE = "failure";
	public  static String RESEND = "resend";
	public  static String MANUAL = "manual";
	public  static String PAY = "pay";
	
	
	private String retValue;
	private String selfId;
	private String sheId;
	private Account account;
	private Worker worker;
	
	private String fromCity;
	private String toCity;
	private String travelTime;
	private String payMoney;
	private String trainNo;
	
	private String errorInfo;

	

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public String getSelfId() {
		return selfId;
	}

	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}

	public String getSheId() {
		return sheId;
	}

	public void setSheId(String sheId) {
		this.sheId = sheId;
	}

	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	
	
}
