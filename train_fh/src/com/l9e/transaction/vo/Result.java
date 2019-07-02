package com.l9e.transaction.vo;

import java.util.List;

public class Result {
	public  static String SUCCESS = "success";//成功
	public  static String FAILURE = "failure";//失败
	public  static String WAIT = "wait";//排队
	public  static String RESEND = "resend";//重发
	public  static String MANUAL = "manual";//人工
	public  static String NOPASS = "nopass";//未通过
	public  static String CANCEL = "cancel";//需要取消
	public  static String STOP = "stop";//暂时停用
	public  static String END = "end";//封停
	public  static String QUEUE = "queue";//12306排队
	
	private String retValue;
	private String selfId;
	private String sheId;
	private Account account;
	private Worker worker;
	
	private String errorInfo = "";

	private String fromCity;
	private String toCity;
	private String travelTime;
	private String payMoney;
	private String trainNo;
	private String seattime;
	private String buyMoney;
	private boolean isManual;
	
	private String returnOptlog;
	private String refundOnline;
	
	private boolean insertFilter=false;//账号是否需要加入账号过滤表cp_accountinfo_filter
	private String filterScope;//与insertFilter配合使用，ALL 全部加入/PART 部分加入
	
	private boolean priceModify=false;//价格是否需要更新
	
	private String contactsNum;//常用联系人总数
	
	public static String FILTER_ALL = "ALL";
	public static String FILTER_PART = "PART";
	
	
	public String getRefundOnline() {
		return refundOnline;
	}

	public void setRefundOnline(String refundOnline) {
		this.refundOnline = refundOnline;
	}

	public String getReturnOptlog() {
		return returnOptlog;
	}

	public void setReturnOptlog(String returnOptlog) {
		this.returnOptlog = returnOptlog;
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

	public List<OrderCP> orderCps;
	private String failReason;
	
	
	

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getSeattime() {
		return seattime;
	}

	public void setSeattime(String seattime) {
		this.seattime = seattime;
	}

	public String getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}

	public List<OrderCP> getOrderCps() {
		return orderCps;
	}

	public void setOrderCps(List<OrderCP> orderCps) {
		this.orderCps = orderCps;
	}

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

	public boolean isInsertFilter() {
		return insertFilter;
	}

	public void setInsertFilter(boolean insertFilter) {
		this.insertFilter = insertFilter;
	}

	public String getFilterScope() {
		return filterScope;
	}

	public void setFilterScope(String filterScope) {
		this.filterScope = filterScope;
	}

	public String getContactsNum() {
		return contactsNum;
	}

	public void setContactsNum(String contactsNum) {
		this.contactsNum = contactsNum;
	}

	public boolean isPriceModify() {
		return priceModify;
	}

	public void setPriceModify(boolean priceModify) {
		this.priceModify = priceModify;
	}
	
	
}
