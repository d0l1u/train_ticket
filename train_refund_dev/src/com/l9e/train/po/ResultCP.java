package com.l9e.train.po;


public class ResultCP {
	public  static String OVER = "over";
	public  static String SUCCESS = "success";
	public  static String FAILURE = "failure";
	public  static String RESEND = "resend";
	public  static String MANUAL = "manual";
	
	private String retValue;
	private String orderId;
	private String cpId;
	
	private String errorInfo;
	private Worker worker;
	private String returnOptlog;
	
	
	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
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

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public String getReturnOptlog() {
		return returnOptlog;
	}

	public void setReturnOptlog(String returnOptlog) {
		this.returnOptlog = returnOptlog;
	}
	
}
