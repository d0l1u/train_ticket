package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

/**
 * hc_orderinfo
 * 
 * @author zhangjun
 * 
 */
public class OrderInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String order_id;// 订单号;
	private String order_name;
	private String pay_money;// 支付价格
	private String buy_money;// 成本价格
	private String ticket_pay_money;// 票价总额
	private String bx_pay_money;// 保险总额
	private String order_status;// 订单状态 00、预下单 11、支付成功 12、EOP发货 22、正在预订 33、预订成功
	// 44、出票成功 45、出票失败 88、订单完成 99、支付失败
	private String create_time;
	private String pay_time;
	private String link_phone;
	private String user_id;
	private String link_name;
	private String finish_time;
	private String opt_ren;// 最后操作人
	private String sms_notify;
	private String order_level;
	private String product_type;
	private String channel;
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLink_name() {
		return link_name;
	}

	public void setLink_name(String link_name) {
		this.link_name = link_name;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getFrom_to_time() {
		return from_to_time;
	}

	public void setFrom_to_time(String fromToTime) {
		from_to_time = fromToTime;
	}

	private String from_to_time;
	// private String refund_create_time;
	// private String refund_time;//退款时间
	// private String refund_money;//退款金额
	// private String refund_memo;//退款备注
	// private String refund_purl;//退款小票地址
	// private String refund_12306_seq;//12306退款流水单号
	private String refund_status;// 退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款
	// 5、改签单退款
	private String is_deadline;// 是否为退款截止日期（0：不是 1：是 列车发车12小时之后视为退款截止日期）
	private String deadline_ignore;// 是否无视退款截止日期 （1：是）
	private String can_refund;// 是否可以退款（默认可以退 0:不能退 1:能退）

	public List<String> getPassengerList() {
		return passengerList;
	}

	public void setPassengerList(List<String> passengerList) {
		this.passengerList = passengerList;
	}

	private String ext_seat;// 扩展坐席
	private List<String> passengerList;// 乘客姓名信息

	public String getExt_seat() {
		return ext_seat;
	}

	public void setExt_seat(String ext_seat) {
		this.ext_seat = ext_seat;
	}

	public String getCan_refund() {
		return can_refund;
	}

	public void setCan_refund(String can_refund) {
		this.can_refund = can_refund;
	}

	public String getIs_deadline() {
		return is_deadline;
	}

	public void setIs_deadline(String is_deadline) {
		this.is_deadline = is_deadline;
	}

	public String getDeadline_ignore() {
		return deadline_ignore;
	}

	public void setDeadline_ignore(String deadline_ignore) {
		this.deadline_ignore = deadline_ignore;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_name() {
		return order_name;
	}

	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}

	public String getPay_money() {
		return pay_money;
	}

	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}

	public String getBuy_money() {
		return buy_money;
	}

	public void setBuy_money(String buy_money) {
		this.buy_money = buy_money;
	}

	public String getTicket_pay_money() {
		return ticket_pay_money;
	}

	public void setTicket_pay_money(String ticket_pay_money) {
		this.ticket_pay_money = ticket_pay_money;
	}

	public String getBx_pay_money() {
		return bx_pay_money;
	}

	public void setBx_pay_money(String bx_pay_money) {
		this.bx_pay_money = bx_pay_money;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getOpt_ren() {
		return opt_ren;
	}

	public void setOpt_ren(String opt_ren) {
		this.opt_ren = opt_ren;
	}

	public String getLink_phone() {
		return link_phone;
	}

	public void setLink_phone(String link_phone) {
		this.link_phone = link_phone;
	}

	public String getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(String finish_time) {
		this.finish_time = finish_time;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public String getSms_notify() {
		return sms_notify;
	}

	public void setSms_notify(String sms_notify) {
		this.sms_notify = sms_notify;
	}

	public String getOrder_level() {
		return order_level;
	}

	public void setOrder_level(String order_level) {
		this.order_level = order_level;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

}
