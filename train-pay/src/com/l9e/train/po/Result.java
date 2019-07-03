package com.l9e.train.po;

import java.util.List;


public class Result {
	public  static String SUCCESS = "success";
	public  static String FAILURE = "failure";
	public  static String RESEND = "resend";
	public  static String MANUAL = "manual";
	public  static String WAIT = "wait";//排队
	public  static String NOPASS = "nopass";//未通过
	public  static String CANCEL = "cancel";//需要取消
	
	
	private String retValue;
	private String selfId;
	private String sheId;
	private Account account;
	private Worker worker;
	private PayCard payCard;
	
	private String returnOptlog;
	
	private String errorInfo;

	private String payMoney;

	private String paybillno;
	
	private String seattime;
	
	private String balance;
	
	/**
	 * 12306返回的检票口信息列表
	 * @return
	 */
	private List<TicketEntrance> ticketEntrances;

	public PayCard getPayCard() {
		return payCard;
	}

	public void setPayCard(PayCard payCard) {
		this.payCard = payCard;
	}

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

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String getReturnOptlog() {
		return returnOptlog;
	}

	public void setReturnOptlog(String returnOptlog) {
		this.returnOptlog = returnOptlog;
	}

	public List<TicketEntrance> getTicketEntrances() {
		return ticketEntrances;
	}

	public void setTicketEntrances(List<TicketEntrance> ticketEntrances) {
		this.ticketEntrances = ticketEntrances;
	}
	
}
