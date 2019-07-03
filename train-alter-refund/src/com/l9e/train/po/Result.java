package com.l9e.train.po;

import java.util.List;


//返回结果实体类
public class Result {
	public  static String SUCCESS = "success";
	public  static String FAILURE = "failure";
	public  static String RESEND = "resend";
	public  static String MANUAL = "manual";
	
	
	private String retValue;
	private String selfId;
	private String sheId;
	private Worker worker;
	
	private String errorInfo;

	private String payMoney;

	private String paybillno;
	
	private String seattime;
	
//	private String orderId;
//	private String outTicketBillno;
//	private String contactsnum;             
//	private String summoney;                 
//	private String from;                            
//	private String to;
//	private String seattime;
//	private String trainno;
//	private List<ReturnAlterPasEntity> cps;

	public String getSeattime() {
		return seattime;
	}

	public void setSeattime(String seattime) {
		this.seattime = seattime;
	}

	public String getPaybillno() {
		return paybillno;
	}

	public void setPaybillno(String paybillno) {
		this.paybillno = paybillno;
	}

	

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
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
