package com.l9e.transaction.vo;

public class OrderInfoTrip {
	
	private String trip_id;
	private String order_id;
	private String trip_seq;
	private String order_name;
	private String order_status;
	private String pay_money;
	private String buy_money;
	private String order_time;//用户下单时间
	private String create_time;//创建时间
	private String out_ticket_time;//出票时间
	private String out_fail_reason;//出票失败原因
	private String out_ticket_billno;//12306单号
	private String out_ticket_type;
	private String train_no;//车次
	private String from_city;//出发城市
	private String to_city;//到达城市
	private String travel_time;
	private String from_time;//出发时间
	private String to_time;//到达时间
	private String seat_type;//座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下） 5、软卧 （51、软卧上 52、软卧下） 6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他
	private String qunar_seat_type;//qunar座位类型：0、站票 1、硬座 2、软座 3、一等软座 4、二等软座 5、硬卧上 6、硬卧中 7、硬卧下 8、软卧上 9、软卧下 10、高级软卧上 11、高级软卧下
	private String ext_seat;//扩展坐席
	private String qunar_ext_seat;//qunar扩展坐席
	private String order_source;//qunar订单来源（qunar1、quanr2）
	private String channel;
	
	public String getOrder_name() {
		return order_name;
	}
	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}
	public String getOut_ticket_type() {
		return out_ticket_type;
	}
	public void setOut_ticket_type(String out_ticket_type) {
		this.out_ticket_type = out_ticket_type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(String trip_id) {
		this.trip_id = trip_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getTrip_seq() {
		return trip_seq;
	}
	public void setTrip_seq(String trip_seq) {
		this.trip_seq = trip_seq;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getBuy_money() {
		return buy_money;
	}
	public void setBuy_money(String buy_money) {
		this.buy_money = buy_money;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getOut_ticket_time() {
		return out_ticket_time;
	}
	public void setOut_ticket_time(String out_ticket_time) {
		this.out_ticket_time = out_ticket_time;
	}
	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}
	public void setOut_ticket_billno(String out_ticket_billno) {
		this.out_ticket_billno = out_ticket_billno;
	}
	public String getTrain_no() {
		return train_no;
	}
	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}
	public String getFrom_city() {
		return from_city;
	}
	public void setFrom_city(String from_city) {
		this.from_city = from_city;
	}
	public String getTo_city() {
		return to_city;
	}
	public void setTo_city(String to_city) {
		this.to_city = to_city;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}
	public String getTo_time() {
		return to_time;
	}
	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seat_type) {
		this.seat_type = seat_type;
	}
	public String getQunar_seat_type() {
		return qunar_seat_type;
	}
	public void setQunar_seat_type(String qunar_seat_type) {
		this.qunar_seat_type = qunar_seat_type;
	}
	public String getExt_seat() {
		return ext_seat;
	}
	public void setExt_seat(String ext_seat) {
		this.ext_seat = ext_seat;
	}
	public String getQunar_ext_seat() {
		return qunar_ext_seat;
	}
	public void setQunar_ext_seat(String qunar_ext_seat) {
		this.qunar_ext_seat = qunar_ext_seat;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}
	public String getOut_fail_reason() {
		return out_fail_reason;
	}
	public void setOut_fail_reason(String out_fail_reason) {
		this.out_fail_reason = out_fail_reason;
	}
	public String getOrder_source() {
		return order_source;
	}
	public void setOrder_source(String order_source) {
		this.order_source = order_source;
	}
	public String getTravel_time() {
		return travel_time;
	}
	public void setTravel_time(String travel_time) {
		this.travel_time = travel_time;
	}
	
}
