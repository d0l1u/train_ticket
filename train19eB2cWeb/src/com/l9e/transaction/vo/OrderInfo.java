package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * hc_orderinfo
 *
 */
public class OrderInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String order_id;//订单号
	private String order_name;//订单名称
	private String pay_money;//支付价格(sum_amount)
	private String ticket_pay_money;//票价总额
	private String buy_money;//成本价格
	private String bx_pay_money;//保险总额
	private String order_status;//订单状态 00、未支付 11、支付成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、已取消
	private String create_time;//创建时间
	private String pay_time;//支付时间
	private String finish_time;//完成时间
	private String opt_time;//最后操作时间
	private String opt_ren;//最后操作人
	private String refund_deadline_ignore;//1：无视截止时间（发车12小时后为退款截止时间）
	private String refund_total;//退款金额总计
	private String can_refund;//是否可以退款（默认可以退 0:不能退 1:能退）
	private String sms_notify;//是否短信通知 1：是；0:否
	private String ext_seat;//扩展坐席
	private String order_level;//订单级别：0：普通订单；1：VIP订单
	private String link_name;//短信通知联系人
	private String link_phone;//短信通知号码
	private String order_pro1;//备用字段1
	private String order_pro2;//备用字段2
	private String user_id;//用户表主键user_id
	private String pay_order_id;//支付订单号（支付接口使用）
	private String product_type;//产品类型别：1、有 0、没有（用二进制10000表示，第一位代表火车票，第二位代表飞机票， 第三位代表景点门票，第四位代表酒店，其它几位空闲）
	private String gps_info;//位置信息
	private String zfb_trade_no;//支付宝交易号
	private String pay_type;//11、联动优势支付；22、支付宝支付; 33、微信支付
	private String channel;//渠道ID
	private String notify_id;//微信支付结果通知id
	private String transaction_id;//微信交易号
	private String return_fee;//微信返回支付金额
	private String discount;//微信返回折扣券
	private String ticket_num;//车票数量
	private String sub_time;//距离出票时间多少秒
	private String refund_fail_reason;//拒绝退款原因/退款失败原因
	private String order_time;//订单日期：格式为‘YYYYMMDD’
	private String stop_pay_time;//停止支付时间（超时未支付时间）
	
	
	
	
	
	public String getStop_pay_time() {
		return stop_pay_time;
	}
	public void setStop_pay_time(String stopPayTime) {
		stop_pay_time = stopPayTime;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String orderTime) {
		order_time = orderTime;
	}
	public String getRefund_fail_reason() {
		return refund_fail_reason;
	}
	public void setRefund_fail_reason(String refundFailReason) {
		refund_fail_reason = refundFailReason;
	}
	public String getSub_time() {
		return sub_time;
	}
	public void setSub_time(String subTime) {
		sub_time = subTime;
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
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String payMoney) {
		pay_money = payMoney;
	}
	public String getTicket_pay_money() {
		return ticket_pay_money;
	}
	public void setTicket_pay_money(String ticketPayMoney) {
		ticket_pay_money = ticketPayMoney;
	}
	public String getBuy_money() {
		return buy_money;
	}
	public void setBuy_money(String buyMoney) {
		buy_money = buyMoney;
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
	public String getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(String finishTime) {
		finish_time = finishTime;
	}
	public String getOpt_time() {
		return opt_time;
	}
	public void setOpt_time(String optTime) {
		opt_time = optTime;
	}
	public String getOpt_ren() {
		return opt_ren;
	}
	public void setOpt_ren(String optRen) {
		opt_ren = optRen;
	}
	public String getRefund_deadline_ignore() {
		return refund_deadline_ignore;
	}
	public void setRefund_deadline_ignore(String refundDeadlineIgnore) {
		refund_deadline_ignore = refundDeadlineIgnore;
	}
	public String getRefund_total() {
		return refund_total;
	}
	public void setRefund_total(String refundTotal) {
		refund_total = refundTotal;
	}
	public String getCan_refund() {
		return can_refund;
	}
	public void setCan_refund(String canRefund) {
		can_refund = canRefund;
	}
	public String getSms_notify() {
		return sms_notify;
	}
	public void setSms_notify(String smsNotify) {
		sms_notify = smsNotify;
	}
	public String getExt_seat() {
		return ext_seat;
	}
	public void setExt_seat(String extSeat) {
		ext_seat = extSeat;
	}
	public String getOrder_level() {
		return order_level;
	}
	public void setOrder_level(String orderLevel) {
		order_level = orderLevel;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public String getPay_order_id() {
		return pay_order_id;
	}
	public void setPay_order_id(String payOrderId) {
		pay_order_id = payOrderId;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String productType) {
		product_type = productType;
	}
	public String getGps_info() {
		return gps_info;
	}
	public void setGps_info(String gpsInfo) {
		gps_info = gpsInfo;
	}
	public String getZfb_trade_no() {
		return zfb_trade_no;
	}
	public void setZfb_trade_no(String zfbTradeNo) {
		zfb_trade_no = zfbTradeNo;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String payType) {
		pay_type = payType;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notifyId) {
		notify_id = notifyId;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transactionId) {
		transaction_id = transactionId;
	}
	public String getReturn_fee() {
		return return_fee;
	}
	public void setReturn_fee(String returnFee) {
		return_fee = returnFee;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getTicket_num() {
		return ticket_num;
	}
	public void setTicket_num(String ticketNum) {
		ticket_num = ticketNum;
	}
	
	
	
	
	
	private String cp_pay_money;//车票单价
	private String user_pay_money;//用户支付金额
	private String ps_pay_money;//配送总额
	private String server_pay_money;//SVIP服务费总额
	private String dealer_name;//代理商姓名
	private String dealer_id;//代理商id
	private String out_ticket_time;
	private String out_ticket_type;//出票方式11、电子票 22、配送票
	private String out_ticket_billno;//12306单号
	private String train_no;//车次
	private String from_city;//出发城市
	private String to_city;//到达城市
	private String from_time;//出发时间
	private String to_time;//到达时间
	private String travel_time;//乘车日期

	private String seat_type;//座位类型
	private String pay_time_start;//支付时间年月日
	private String pay_time_end;//支付时间小时分钟
	private String out_ticket_time_start;//出票时间年月日
	private String out_ticket_time_end;//出票时间小时分钟
	private String refund_time_start;//退款时间年月日
	private String refund_time_end;//退款时间小时分钟
	
	private String refund_time;//退款时间
	private String refund_money;//退款金额
	private String refund_status;//退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款
	private String refund_type;//退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款 6、主动退款
	private String is_deadline;//是否为退款截止日期（0：不是 1：是 列车发车12小时之后视为退款截止日期）
	private String deadline_ignore;//是否无视退款截止日期 （1：是）
	private String is_repay;//是否重新支付（0：不是 1：是 ）
	
	private String user_name;//乘客姓名
	private String ticket_type;//车票类型
	private String ids_type;//证件类型
	private String user_ids;//证件号码
	private String telephone;//联系电话
	private String train_box;//车厢
	private String seat_no;//座位号
	private String cpCount;//票数
	private String deffer_time;//现在距离出票时间的秒数
	
	
	public String getDeffer_time() {
		return deffer_time;
	}
	public void setDeffer_time(String defferTime) {
		deffer_time = defferTime;
	}
	public String getCp_pay_money() {
		return cp_pay_money;
	}
	public void setCp_pay_money(String cpPayMoney) {
		cp_pay_money = cpPayMoney;
	}
	public String getUser_pay_money() {
		return user_pay_money;
	}
	public void setUser_pay_money(String userPayMoney) {
		user_pay_money = userPayMoney;
	}
	public String getPs_pay_money() {
		return ps_pay_money;
	}
	public void setPs_pay_money(String psPayMoney) {
		ps_pay_money = psPayMoney;
	}
	public String getServer_pay_money() {
		return server_pay_money;
	}
	public void setServer_pay_money(String serverPayMoney) {
		server_pay_money = serverPayMoney;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealerName) {
		dealer_name = dealerName;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealerId) {
		dealer_id = dealerId;
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
	public String getPay_time_start() {
		return pay_time_start;
	}
	public void setPay_time_start(String payTimeStart) {
		pay_time_start = payTimeStart;
	}
	public String getPay_time_end() {
		return pay_time_end;
	}
	public void setPay_time_end(String payTimeEnd) {
		pay_time_end = payTimeEnd;
	}
	public String getOut_ticket_time_start() {
		return out_ticket_time_start;
	}
	public void setOut_ticket_time_start(String outTicketTimeStart) {
		out_ticket_time_start = outTicketTimeStart;
	}
	public String getOut_ticket_time_end() {
		return out_ticket_time_end;
	}
	public void setOut_ticket_time_end(String outTicketTimeEnd) {
		out_ticket_time_end = outTicketTimeEnd;
	}
	public String getRefund_time_start() {
		return refund_time_start;
	}
	public void setRefund_time_start(String refundTimeStart) {
		refund_time_start = refundTimeStart;
	}
	public String getRefund_time_end() {
		return refund_time_end;
	}
	public void setRefund_time_end(String refundTimeEnd) {
		refund_time_end = refundTimeEnd;
	}
	public String getRefund_time() {
		return refund_time;
	}
	public void setRefund_time(String refundTime) {
		refund_time = refundTime;
	}
	public String getRefund_money() {
		return refund_money;
	}
	public void setRefund_money(String refundMoney) {
		refund_money = refundMoney;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refundStatus) {
		refund_status = refundStatus;
	}
	public String getRefund_type() {
		return refund_type;
	}
	public void setRefund_type(String refundType) {
		refund_type = refundType;
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
	public String getIs_repay() {
		return is_repay;
	}
	public void setIs_repay(String isRepay) {
		is_repay = isRepay;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getTicket_type() {
		return ticket_type;
	}
	public void setTicket_type(String ticketType) {
		ticket_type = ticketType;
	}
	public String getIds_type() {
		return ids_type;
	}
	public void setIds_type(String idsType) {
		ids_type = idsType;
	}
	public String getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(String userIds) {
		user_ids = userIds;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getTrain_box() {
		return train_box;
	}
	public void setTrain_box(String trainBox) {
		train_box = trainBox;
	}
	public String getSeat_no() {
		return seat_no;
	}
	public void setSeat_no(String seatNo) {
		seat_no = seatNo;
	}
	public String getCpCount() {
		return cpCount;
	}
	public void setCpCount(String cpCount) {
		this.cpCount = cpCount;
	}
	
	
	
}
