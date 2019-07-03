package com.l9e.transaction.vo;

import java.util.List;

/**
 * 数据库表映射 orderInfo VO
 * 
 * */
public class DBOrderInfo {
	private String order_id ;       
	private String order_name   ;
	private String order_level   ;  
	private String pay_money    ;
	private String buy_money  ;
	private String order_status ;
	private String notice_status ;
	private String order_time  ;
	private String create_time;
	private String out_ticket_time ;
	private String out_ticket_billno ;
	private String out_fail_reason ;
	private String train_no ;
	private String from_city  ;
	private String to_city  ;
	private String from_time; 
	private String to_time   ;
	private String travel_date ;
	private String elong_seat_type ;
	private String seat_type  ;
	private String opt_ren  ;
	private String opt_time   ;
	private String datetime  ;
	private String passenger_reason ;
	private String ext_field1   ;     //同城：reqtoken请求物证值[异步时填写]
	private String ext_field2  ;  //通知出票系统 备选无座类型    
	private String ticket_num  ;
	private String  channel;
	private List<DBPassengerInfo> passengers;
	private String cp_notify_level;//通知出票系统优先级
	
	/**同城扩展字段*/
	private String callbackurl;//出票结果回调接口
	private String from_station_code;
	private String to_station_code;
	
	private String pay_limit_time;
	private String wait_for_order;
	
	private String stationName; //车站名称
	private String stationCode; //车站三字码
	
	List<DBStudentInfo> students;
	
	
	public List<DBStudentInfo> getStudents() {
		return students;
	}
	public void setStudents(List<DBStudentInfo> students) {
		this.students = students;
	}
	public String getWait_for_order() {
		return wait_for_order;
	}
	public void setWait_for_order(String waitForOrder) {
		wait_for_order = waitForOrder;
	}
	public String getPay_limit_time() {
		return pay_limit_time;
	}
	public void setPay_limit_time(String payLimitTime) {
		pay_limit_time = payLimitTime;
	}
	public String getCp_notify_level() {
		return cp_notify_level;
	}
	public void setCp_notify_level(String cpNotifyLevel) {
		cp_notify_level = cpNotifyLevel;
	}
	public String getFrom_station_code() {
		return from_station_code;
	}
	public void setFrom_station_code(String fromStationCode) {
		from_station_code = fromStationCode;
	}
	public String getTo_station_code() {
		return to_station_code;
	}
	public void setTo_station_code(String toStationCode) {
		to_station_code = toStationCode;
	}
	public String getCallbackurl() {
		return callbackurl;
	}
	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public List<DBPassengerInfo> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<DBPassengerInfo> passengers) {
		this.passengers = passengers;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getOrder_name() {
		return order_name;
	}
	public void setOrder_name(String orderName) {
		order_name = orderName;
	}
	public String getOrder_level() {
		return order_level;
	}
	public void setOrder_level(String orderLevel) {
		order_level = orderLevel;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String payMoney) {
		pay_money = payMoney;
	}
	public String getBuy_money() {
		return buy_money;
	}
	public void setBuy_money(String buyMoney) {
		buy_money = buyMoney;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String orderStatus) {
		order_status = orderStatus;
	}
	public String getNotice_status() {
		return notice_status;
	}
	public void setNotice_status(String noticeStatus) {
		notice_status = noticeStatus;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String orderTime) {
		order_time = orderTime;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getOut_ticket_time() {
		return out_ticket_time;
	}
	public void setOut_ticket_time(String outTicketTime) {
		out_ticket_time = outTicketTime;
	}
	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}
	public void setOut_ticket_billno(String outTicketBillno) {
		out_ticket_billno = outTicketBillno;
	}
	public String getOut_fail_reason() {
		return out_fail_reason;
	}
	public void setOut_fail_reason(String outFailReason) {
		out_fail_reason = outFailReason;
	}
	public String getTrain_no() {
		return train_no;
	}
	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}
	public String getFrom_city() {
		return from_city;
	}
	public void setFrom_city(String fromCity) {
		from_city = fromCity;
	}
	public String getTo_city() {
		return to_city;
	}
	public void setTo_city(String toCity) {
		to_city = toCity;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String fromTime) {
		from_time = fromTime;
	}
	public String getTo_time() {
		return to_time;
	}
	public void setTo_time(String toTime) {
		to_time = toTime;
	}
	public String getTravel_date() {
		return travel_date;
	}
	public void setTravel_date(String travelDate) {
		travel_date = travelDate;
	}
	public String getElong_seat_type() {
		return elong_seat_type;
	}
	public void setElong_seat_type(String elongSeatType) {
		elong_seat_type = elongSeatType;
	}
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seatType) {
		seat_type = seatType;
	}
	public String getOpt_ren() {
		return opt_ren;
	}
	public void setOpt_ren(String optRen) {
		opt_ren = optRen;
	}
	public String getOpt_time() {
		return opt_time;
	}
	public void setOpt_time(String optTime) {
		opt_time = optTime;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getPassenger_reason() {
		return passenger_reason;
	}
	public void setPassenger_reason(String passengerReason) {
		passenger_reason = passengerReason;
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
	public String getTicket_num() {
		return ticket_num;
	}
	public void setTicket_num(String ticketNum) {
		ticket_num = ticketNum;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	} 
	
}
