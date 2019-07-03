package com.l9e.train.po;

import java.util.List;


public class Result {
	
	private String orderId;
	private String sheId;
	private Worker worker;
	
	private String errorInfo;

	List<ResultCP> cpList;
	
	public List<ResultCP> getCpList() {
		return cpList;
	}

	public void setCpList(List<ResultCP> cpList) {
		this.cpList = cpList;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public String getSheId() {
		return sheId;
	}

	public void setSheId(String sheId) {
		this.sheId = sheId;
	}
}
