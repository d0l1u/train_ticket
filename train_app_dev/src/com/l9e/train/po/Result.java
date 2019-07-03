package com.l9e.train.po;

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
	public  static String CHANGE_ACCOUNT_RESEND = "changeAccountResend";//更换12306账号后重发
	public  static String CHANGE_JDACCOUNT_RESEND = "changeJdAccountResend";//更换京东账号后重发
	public  static String CHANGE_JDCARD_RESEND = "changeJdcardResend";//更换京东预付卡后重发
	
	private String retValue;
	private String selfId;
	private String sheId;
	private Account account;
	private Worker worker;
	private CtripAcc ctripAcc;
	
	//新增京东账户和京东预付卡
	private JdAcc jdAcc;
	private List<JdPrePayCard> jdPrePayCardList;
	private String jdOrderId;//京东订单号
	private String jdOrderNo;//京东流水号
	private String jdPayMoney;//京东实际支付金额
	private String jdRebateTicketMoney;//京东优惠券金额
	private String jdRebateTicketId;//优惠券编号
	private String kltOrderNo;//开联通流水号
	
	private String errorInfo = "";

	private String fromCity;
	private String toCity;
	private String travelTime;
	private String payMoney;//12306支付金额
	private String trainNo;
	private String seattime;
	private String buyMoney;
	private String ctripId;
	private boolean isManual;
	private String ctrip_bx_money;
	
	
	private String returnOptlog;
	
	private boolean insertFilter=false;//账号是否需要加入账号过滤表cp_accountinfo_filter
	private String filterScope;//与insertFilter配合使用，ALL 全部加入/PART 部分加入
	
	private boolean priceModify=false;//价格是否需要更新
	
	private String contactsNum;//常用联系人总数
	
	public static String FILTER_ALL = "ALL";
	public static String FILTER_PART = "PART";
	
	
	private String refundOnline; 
	
	public String getRefundOnline() {
		return refundOnline;
	}

	public void setRefundOnline(String refundOnline) {
		this.refundOnline = refundOnline;
	}

	public String getCtripId() {
		return ctripId;
	}

	public void setCtripId(String ctripId) {
		this.ctripId = ctripId;
	}

	public CtripAcc getCtripAcc() {
		return ctripAcc;
	}

	public void setCtripAcc(CtripAcc ctripAcc) {
		this.ctripAcc = ctripAcc;
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

	public String getCtrip_bx_money() {
		return ctrip_bx_money;
	}

	public void setCtrip_bx_money(String ctripBxMoney) {
		ctrip_bx_money = ctripBxMoney;
	}

	
	public JdAcc getJdAcc() {
		return jdAcc;
	}

	public void setJdAcc(JdAcc jdAcc) {
		this.jdAcc = jdAcc;
	}
    
	public List<JdPrePayCard> getJdPrePayCardList() {
		return jdPrePayCardList;
	}

	public void setJdPrePayCardList(List<JdPrePayCard> jdPrePayCardList) {
		this.jdPrePayCardList = jdPrePayCardList;
	}

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public String getJdOrderNo() {
		return jdOrderNo;
	}

	public void setJdOrderNo(String jdOrderNo) {
		this.jdOrderNo = jdOrderNo;
	}

	public String getJdPayMoney() {
		return jdPayMoney;
	}

	public void setJdPayMoney(String jdPayMoney) {
		this.jdPayMoney = jdPayMoney;
	}

	public String getJdRebateTicketMoney() {
		return jdRebateTicketMoney;
	}

	public void setJdRebateTicketMoney(String jdRebateTicketMoney) {
		this.jdRebateTicketMoney = jdRebateTicketMoney;
	}

	public String getJdRebateTicketId() {
		return jdRebateTicketId;
	}

	public void setJdRebateTicketId(String jdRebateTicketId) {
		this.jdRebateTicketId = jdRebateTicketId;
	}

	public String getKltOrderNo() {
		return kltOrderNo;
	}

	public void setKltOrderNo(String kltOrderNo) {
		this.kltOrderNo = kltOrderNo;
	}
	
	
}
