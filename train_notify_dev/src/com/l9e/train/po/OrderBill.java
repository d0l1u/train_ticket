package com.l9e.train.po;

public class OrderBill {
	private String orderId;
	private String buyMoney;
	private String orderStatus;
	private String outTicketBillno;
	private String notifyUrl;
	private StringBuffer cps;
	private String seattrains;
	private String errorInfo;
	private String accUsername;
	private String accPassword;
	private String extSeattype;
	private String level;
	private String passengers;
	private StringBuffer sbPassenger;
	private String isPay;
	
	private String fromTime;
	private String toTime;
	private String returnoptlog;
	private String outTicketTime;
	private String payLimitTime;
	
	public static String BILL_SUCCESS= "99";
	public static String BILL_FAILURE= "10";
	public static String BILL_CANCELING= "83";
	public static String BILL_PAY_FAILURE= "77";
	
	
	public String getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(String outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public String getPayLimitTime() {
		return payLimitTime;
	}

	public void setPayLimitTime(String payLimitTime) {
		this.payLimitTime = payLimitTime;
	}

	public String getReturnOptlog() {
		return returnoptlog;
	}

	public void setReturnOptlog(String returnOptlog) {
		returnoptlog = returnOptlog;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public String getExtSeattype() {
		return extSeattype;
	}


	public void setExtSeattype(String extSeattype) {
		this.extSeattype = extSeattype;
	}


	public String getAccUsername() {
		return accUsername;
	}


	public void setAccUsername(String accUsername) {
		this.accUsername = accUsername;
	}


	public String getAccPassword() {
		return accPassword;
	}


	public void setAccPassword(String accPassword) {
		this.accPassword = accPassword;
	}


	public String getErrorInfo() {
		return errorInfo;
	}


	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}


	public String getSeattrains() {
		return cps.toString().substring(0, cps.toString().lastIndexOf("#"));
	}
	
	
	public OrderBill() {
		// TODO Auto-generated constructor stub
		cps=new StringBuffer();
		sbPassenger = new StringBuffer();
	}
	
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getBuyMoney() {
		return buyMoney;
	}


	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}


	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOutTicketBillno() {
		return outTicketBillno;
	}
	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}
	public void addOrderCp(String string) {
		// TODO Auto-generated method stub
		cps.append(string+"#");
	}
	
	public void addPassengers(String str){
		sbPassenger.append(str+"#");
	}


	public String getPassengers() {
		return sbPassenger.toString().substring(0, sbPassenger.toString().lastIndexOf("#"));
	}

	
	
	
}
