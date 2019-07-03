package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * hc_orderinfo
 * @author zhangjun
 *
 */
public class OrderInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String order_id;//订单号;
	
	private String eop_order_id;//EOP订单号;
	private String order_name;
	private String at_province_id;//所属省
	private String at_city_id;//所属城市
	private String pay_money;//支付价格
	private String cp_pay_money;//车票单价
	private String user_pay_money;//用户支付金额
	private String buy_money;//成本价格
	private String ticket_pay_money;//票价总额
	private String bx_pay_money;//保险总额
	private String ps_pay_money;//配送总额
	private String server_pay_money;//SVIP服务费总额
	private String order_status;//订单状态 00、预下单 11、支付成功 12、EOP发货 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支付失败
	private String create_time;
	private String pay_time;
	private String dealer_name;//代理商姓名
	private String dealer_id;//代理商id
	private String out_ticket_time;
	private String out_ticket_type;//出票方式11、电子票 22、配送票
	private String opt_ren;//最后操作人
	private String out_ticket_billno;//12306单号
	private String train_no;//车次
	private String from_city;//出发城市
	private String to_city;//到达城市
	private String from_time;//出发时间
	private String to_time;//到达时间
	private String travel_time;//乘车日期
	private String ticket_num;//车票数量

	private String seat_type;//座位类型
	private String pay_type;//支付类型
	private String pay_time_start;//支付时间年月日
	private String pay_time_end;//支付时间小时分钟
	private String out_ticket_time_start;//出票时间年月日
	private String out_ticket_time_end;//出票时间小时分钟
	private String refund_time_start;//退款时间年月日
	private String refund_time_end;//退款时间小时分钟
	
	//private String refund_create_time;
	private String refund_time;//退款时间
	private String refund_money;//退款金额
	//private String refund_memo;//退款备注
	//private String refund_purl;//退款小票地址
	//private String refund_12306_seq;//12306退款流水单号
	private String refund_status;//退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款
	private String refund_type;//退款类型：1、用户退款 2、差额退款 3、出票失败退款 4、改签差额退款 5、改签单退款 6、主动退款
	private String is_before;//是否为发车前特定时间内（0：不是 1：是 ）
	private String is_deadline;//是否为退款截止日期（0：不是 1：是 列车发车12小时之后视为退款截止日期）
	private String deadline_ignore;//是否无视退款截止日期 （1：是）
	private String can_refund;//是否可以退款（默认可以退 0:不能退 1:能退）
	private String is_repay;//是否重新支付（0：不是 1：是 ）
	private String ext_seat;//扩展坐席
	
	private String user_name;//乘客姓名
	private String ticket_type;//车票类型
	private String ids_type;//证件类型
	private String user_ids;//证件号码
	private String telephone;//联系电话
	private String train_box;//车厢
	private String seat_no;//座位号
	private String cpCount;//票数
	
	private String ticket_ps_type;//是否选择送票上门（00、正常电子票 11、合作商送票上门）
	
	
	
	public String getTicket_ps_type() {
		return ticket_ps_type;
	}
	public void setTicket_ps_type(String ticketPsType) {
		ticket_ps_type = ticketPsType;
	}
	public String getCp_pay_money() {
		return cp_pay_money;
	}
	public void setCp_pay_money(String cpPayMoney) {
		cp_pay_money = cpPayMoney;
	}
	public String getCpCount() {
		return cpCount;
	}
	public void setCpCount(String cpCount) {
		this.cpCount = cpCount;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String payType) {
		pay_type = payType;
	}
	public String getRefund_money() {
		return refund_money;
	}
	public void setRefund_money(String refundMoney) {
		refund_money = refundMoney;
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
	public String getAt_province_id() {
		return at_province_id;
	}
	public void setAt_province_id(String at_province_id) {
		this.at_province_id = at_province_id;
	}
	public String getAt_city_id() {
		return at_city_id;
	}
	public void setAt_city_id(String at_city_id) {
		this.at_city_id = at_city_id;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}
	public String getUser_pay_money() {
		return user_pay_money;
	}
	public void setUser_pay_money(String userPayMoney) {
		user_pay_money = userPayMoney;
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
	public String getPs_pay_money() {
		return ps_pay_money;
	}
	public void setPs_pay_money(String ps_pay_money) {
		this.ps_pay_money = ps_pay_money;
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
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getOut_ticket_time() {
		return out_ticket_time;
	}
	public void setOut_ticket_time(String out_ticket_time) {
		this.out_ticket_time = out_ticket_time;
	}
	public String getOut_ticket_type() {
		return out_ticket_type;
	}
	public void setOut_ticket_type(String out_ticket_type) {
		this.out_ticket_type = out_ticket_type;
	}
	public String getOpt_ren() {
		return opt_ren;
	}
	public void setOpt_ren(String opt_ren) {
		this.opt_ren = opt_ren;
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
	public String getTravel_time() {
		return travel_time;
	}
	public void setTravel_time(String travel_time) {
		this.travel_time = travel_time;
	}
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seat_type) {
		this.seat_type = seat_type;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	
	public String getRefund_type() {
		return refund_type;
	}
	public void setRefund_type(String refundType) {
		refund_type = refundType;
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
	public String getIs_repay() {
		return is_repay;
	}
	public void setIs_repay(String isRepay) {
		is_repay = isRepay;
	}
	public String getRefund_time() {
		return refund_time;
	}
	public void setRefund_time(String refundTime) {
		refund_time = refundTime;
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
	public String getEop_order_id() {
		return eop_order_id;
	}
	public void setEop_order_id(String eopOrderId) {
		eop_order_id = eopOrderId;
	}
	public String getServer_pay_money() {
		return server_pay_money;
	}
	public void setServer_pay_money(String serverPayMoney) {
		server_pay_money = serverPayMoney;
	}
	public String getTicket_num() {
		return ticket_num;
	}
	public void setTicket_num(String ticketNum) {
		ticket_num = ticketNum;
	}
	public String getIs_before() {
		return is_before;
	}
	public void setIs_before(String isBefore) {
		is_before = isBefore;
	}
	
}
