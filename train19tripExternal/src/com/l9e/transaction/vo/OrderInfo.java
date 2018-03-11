package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * hc_orderinfo
 * @author zhangjun
 *
 */
public class OrderInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String order_id;			//订单号;
	private String agent_id;			//代理商ID;
	private String agent_name;			//代理商登陆账号;
	private String order_name;
	private String order_level;			//订单级别
	private String pay_money;			//商户实际支付价格
	private String pay_money_show;		//用户支付价格
	private String buy_money;			//成本价格
	private String ticket_pay_money;	//票价总额
	private String bx_pay_money;		//保险总额
	private String order_status;		//订单状态 00、预下单 11、支付成功 12、EOP发货 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支付失败
	private String create_time;
	private String pay_time;
	private String eop_order_id;		//平台订单id
	private String merchant_order_id;	//	合作商订单id
	private String merchant_id;			//合作商id
	private String out_ticket_time;
	private String out_ticket_type;		//出票方式11、电子票 22、配送票
	private String opt_ren;				//最后操作人
	private String out_ticket_billno;	//12306单号
	private String train_no;			//车次
	private String from_station;		//出发城市
	private String arrive_station;		//到达城市
	private String from_time;			//出发时间
	private String arrive_time;			//到达时间
	private String travel_time;			//乘车日期
	private String ticket_num;			//订单内车票数量
	private String seat_type;//座位类型
	
	private String sms_notify;			//是否短信通知
	private String link_name;			//短信通知联系人
	private String link_phone;			//短信通知号码
	private String pay_result_url;		//订单支付结果异步通知地址
	private String order_book_url;		//订单预定结果通知合作商户url
	private String order_result_url;	//订单处理结果通知合作商户url
	private String eop_refund_url;		//接收eop退款结果url
	private String eop_pay_number;		//eop支付流水号
	private String send_notify_url;		//向eop发货地址
	private String order_pro1;			//备用字段1
	private String order_pro2;			//备用字段2
	private String pay_type;			//扣费渠道
	//private String refund_create_time;
	//private String refund_time;//退款时间
	//private String refund_money;//退款金额
	//private String refund_memo;//退款备注
	//private String refund_purl;//退款小票地址
	//private String refund_12306_seq;//12306退款流水单号
	private String refund_status;	//退款类型：22、拒绝退款 33、部分退款完成 44、全部退款完成
	private String is_deadline;//是否为退款截止日期（0：不是 1：是 列车发车12小时之后视为退款截止日期）
	private String deadline_ignore;//是否无视退款截止日期 （1：是）
	private String can_refund;//是否可以退款（默认可以退 0:不能退 1:能退）
	
	private String ext_seat;//扩展坐席
	private String account_name;//12306账号
	private String account_pwd;//12306密码
	
	private Integer  isChooseSeats; //是否选座  : 1 选座 , 2 不选座
	private String   chooseSeats;   // 选座信息: (选座个数要和乘客数量一致)
	
	public void CalculateSum(){
		Double sum = Double.valueOf(this.bx_pay_money)+Double.valueOf(this.ticket_pay_money);
		this.pay_money_show = String.valueOf(sum);
	}
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String orderId) {
		order_id = orderId;
	}

	public String getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(String agentId) {
		agent_id = agentId;
	}
	public String getAgent_name() {
		return agent_name;
	}
	public void setAgent_name(String agentName) {
		agent_name = agentName;
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

	public String getPay_money_show() {
		return pay_money_show;
	}
	public void setPay_money_show(String payMoneyShow) {
		pay_money_show = payMoneyShow;
	}
	public String getBuy_money() {
		return buy_money;
	}

	public void setBuy_money(String buyMoney) {
		buy_money = buyMoney;
	}

	public String getTicket_pay_money() {
		return ticket_pay_money;
	}

	public void setTicket_pay_money(String ticketPayMoney) {
		ticket_pay_money = ticketPayMoney;
	}

	public String getBx_pay_money() {
		return bx_pay_money;
	}

	public void setBx_pay_money(String bxPayMoney) {
		bx_pay_money = bxPayMoney;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String orderStatus) {
		order_status = orderStatus;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String payTime) {
		pay_time = payTime;
	}

	public String getEop_order_id() {
		return eop_order_id;
	}
	public void setEop_order_id(String eopOrderId) {
		eop_order_id = eopOrderId;
	}
	public String getMerchant_order_id() {
		return merchant_order_id;
	}

	public void setMerchant_order_id(String merchantOrderId) {
		merchant_order_id = merchantOrderId;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchantId) {
		merchant_id = merchantId;
	}

	public String getOut_ticket_time() {
		return out_ticket_time;
	}

	public void setOut_ticket_time(String outTicketTime) {
		out_ticket_time = outTicketTime;
	}

	public String getOut_ticket_type() {
		return out_ticket_type;
	}

	public void setOut_ticket_type(String outTicketType) {
		out_ticket_type = outTicketType;
	}

	public String getOpt_ren() {
		return opt_ren;
	}

	public void setOpt_ren(String optRen) {
		opt_ren = optRen;
	}

	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}

	public void setOut_ticket_billno(String outTicketBillno) {
		out_ticket_billno = outTicketBillno;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}

	public String getFrom_station() {
		return from_station;
	}

	public void setFrom_station(String fromStation) {
		from_station = fromStation;
	}

	public String getArrive_station() {
		return arrive_station;
	}

	public void setArrive_station(String arriveStation) {
		arrive_station = arriveStation;
	}

	public String getFrom_time() {
		return from_time;
	}

	public void setFrom_time(String fromTime) {
		from_time = fromTime;
	}

	public String getArrive_time() {
		return arrive_time;
	}

	public void setArrive_time(String arriveTime) {
		arrive_time = arriveTime;
	}

	public String getTravel_time() {
		return travel_time;
	}

	public void setTravel_time(String travelTime) {
		travel_time = travelTime;
	}

	public String getSeat_type() {
		return seat_type;
	}

	public void setSeat_type(String seatType) {
		seat_type = seatType;
	}

	public String getSms_notify() {
		return sms_notify;
	}
	public void setSms_notify(String smsNotify) {
		sms_notify = smsNotify;
	}
	public String getLink_name() {
		return link_name;
	}

	public void setLink_name(String linkName) {
		link_name = linkName;
	}

	public String getLink_phone() {
		return link_phone;
	}

	public void setLink_phone(String linkPhone) {
		link_phone = linkPhone;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refundStatus) {
		refund_status = refundStatus;
	}

	public String getIs_deadline() {
		return is_deadline;
	}

	public void setIs_deadline(String isDeadline) {
		is_deadline = isDeadline;
	}

	public String getDeadline_ignore() {
		return deadline_ignore;
	}

	public void setDeadline_ignore(String deadlineIgnore) {
		deadline_ignore = deadlineIgnore;
	}

	public String getCan_refund() {
		return can_refund;
	}

	public void setCan_refund(String canRefund) {
		can_refund = canRefund;
	}

	public String getExt_seat() {
		return ext_seat;
	}
	public void setExt_seat(String extSeat) {
		ext_seat = extSeat;
	}
	public String getOrder_result_url() {
		return order_result_url;
	}
	public void setOrder_result_url(String orderResultUrl) {
		order_result_url = orderResultUrl;
	}
	public String getOrder_book_url() {
		return order_book_url;
	}
	public void setOrder_book_url(String orderBookUrl) {
		order_book_url = orderBookUrl;
	}
	public String getEop_refund_url() {
		return eop_refund_url;
	}
	public void setEop_refund_url(String eopRefundUrl) {
		eop_refund_url = eopRefundUrl;
	}
	public String getEop_pay_number() {
		return eop_pay_number;
	}
	public void setEop_pay_number(String eopPayNumber) {
		eop_pay_number = eopPayNumber;
	}
	public String getSend_notify_url() {
		return send_notify_url;
	}
	public void setSend_notify_url(String sendNotifyUrl) {
		send_notify_url = sendNotifyUrl;
	}
	public String getPay_result_url() {
		return pay_result_url;
	}
	public void setPay_result_url(String payResultUrl) {
		pay_result_url = payResultUrl;
	}
	public String getOrder_pro1() {
		return order_pro1;
	}
	public void setOrder_pro1(String orderPro1) {
		order_pro1 = orderPro1;
	}
	public String getOrder_pro2() {
		return order_pro2;
	}
	public void setOrder_pro2(String orderPro2) {
		order_pro2 = orderPro2;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String payType) {
		pay_type = payType;
	}
	public String getTicket_num() {
		return ticket_num;
	}
	public void setTicket_num(String ticketNum) {
		ticket_num = ticketNum;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String accountName) {
		account_name = accountName;
	}
	public String getAccount_pwd() {
		return account_pwd;
	}
	public void setAccount_pwd(String accountPwd) {
		account_pwd = accountPwd;
	}
	
	public Integer getIsChooseSeats() {
		return isChooseSeats;
	}
	public void setIsChooseSeats(Integer isChooseSeats) {
		this.isChooseSeats = isChooseSeats;
	}
	public String getChooseSeats() {
		return chooseSeats;
	}
	public void setChooseSeats(String chooseSeats) {
		this.chooseSeats = chooseSeats;
	}
	
	
	
}
