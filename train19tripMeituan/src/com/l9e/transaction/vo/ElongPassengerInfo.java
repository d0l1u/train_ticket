package com.l9e.transaction.vo;

public class ElongPassengerInfo {
	/**请求参*/
	private String certNo;			// 证件号
	private String certType; 		// 艺龙证件类型
	private String name;			// 姓名
	private String orderItemId; 	// 票item号
	private String ticketType;		// 票类型
	
	/**系统参*/
	private String sysCertType; 	// 系统证件类型
	private String sysTicketType; 	// 系统票类型
	
	private String pay_money;
	private String telephone;
	private String order_id;
	private String seat_type;
	private String elong_seat_type;
	
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seatType) {
		seat_type = seatType;
	}
	public String getElong_seat_type() {
		return elong_seat_type;
	}
	public void setElong_seat_type(String elongSeatType) {
		elong_seat_type = elongSeatType;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String payMoney) {
		pay_money = payMoney;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getSysCertType() {
		return sysCertType;
	}
	public void setSysCertType(String sysCertType) {
		this.sysCertType = sysCertType;
	}
	public String getSysTicketType() {
		return sysTicketType;
	}
	public void setSysTicketType(String sysTicketType) {
		this.sysTicketType = sysTicketType;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	

}
