package com.l9e.transaction.vo;

import java.util.List;

public class ElongOrderInfo {
	private static final long serialVersionUID = 1L;
	
	/**请求参*/
	private String arrStation;			// 	到达站名(arrive)
	private String dptStation; 			//  出发站名(departure)
	private String orderDate;			//	下单时间
	private String contactMobile; 		//	手机号
	private String contactName; 		//	手机号
	private String orderId;				//	订单号	
	private String seatType;			//	艺龙席类型
	private String sysSeatType;			//	系统 席类型[]
	private String ticketPrice;			//	车票单价
	private String sumTicketPrice;		//	总价[]
	private String trainEndTime;		//	到站时间
	private String trainNo;				//	车次
	private String trainStartTime;		//	发车时间
	private String acceptStand;			//是否接受站票  1 接受 0 不接受   默认0不接受
	//private ExtSeat[] extSeats;
	private String[] extSeats;				//
	
	private List<ElongPassengerInfo> passengers;//乘客信息列表
	
	/**系统参数*/
	private String order_status	;		//订单状态
	private String ext_field1;
	private String ext_field2;
	private String order_name;
	private String ticket_num;
	private String channel;
	
	
	private String order_type; //11先预订后支付  22 先支付后预订
	
	
	
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String orderType) {
		order_type = orderType;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getAcceptStand() {
		return acceptStand;
	}
	public void setAcceptStand(String acceptStand) {
		this.acceptStand = acceptStand;
	}
	public String getTicket_num() {
		return ticket_num;
	}
	public void setTicket_num(String ticketNum) {
		ticket_num = ticketNum;
	}
	public String getOrder_name() {
		return order_name;
	}
	public void setOrder_name(String orderName) {
		order_name = orderName;
	}
	public String getExt_field1() {
		return ext_field1;
	}
	public void setExt_field1(String extField1) {
		ext_field1 = extField1;
	}
	public String getExt_field2() {
		return ext_field2;
	}
	public void setExt_field2(String extField2) {
		ext_field2 = extField2;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String orderStatus) {
		order_status = orderStatus;
	}
	public String getSysSeatType() {
		return sysSeatType;
	}
	public void setSysSeatType(String sysSeatType) {
		this.sysSeatType = sysSeatType;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public String getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	public String getSumTicketPrice() {
		return sumTicketPrice;
	}
	public void setSumTicketPrice(String sumTicketPrice) {
		this.sumTicketPrice = sumTicketPrice;
	}
	public String getTrainEndTime() {
		return trainEndTime;
	}
	public void setTrainEndTime(String trainEndTime) {
		this.trainEndTime = trainEndTime;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getTrainStartTime() {
		return trainStartTime;
	}
	public void setTrainStartTime(String trainStartTime) {
		this.trainStartTime = trainStartTime;
	}
	public String getArrStation() {
		return arrStation;
	}
	public void setArrStation(String arrStation) {
		this.arrStation = arrStation;
	}
	public String getDptStation() {
		return dptStation;
	}
	public void setDptStation(String dptStation) {
		this.dptStation = dptStation;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<ElongPassengerInfo> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<ElongPassengerInfo> passengers) {
		this.passengers = passengers;
	}
/*	public ExtSeat[] getExtSeats() {
		return extSeats;
	}
	public void setExtSeats(ExtSeat[] extSeats) {
		this.extSeats = extSeats;
	}*/
	public String[] getExtSeats() {
		return extSeats;
	}
	public void setExtSeats(String[] extSeats) {
		this.extSeats = extSeats;
	}
	
  

}
