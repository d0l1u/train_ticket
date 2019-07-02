package com.l9e.transaction.vo;

public class ElongOrderInfoCp {
	private static final long serialVersionUID = 1L;
	private String cp_id;
	private String order_id;
	private String user_name;
	private String elong_ticket_type;
	private String ticket_type;
	private String elong_ids_type;
	private String ids_type;
	private String user_ids;
	private String telephone;
	private String create_time;
	private String pay_money;
	private String buy_money;
	private String modify_time;
	private String elong_seat_type;
	private String seat_type;
	private String train_box;
	private String seat_no;
	private String refund_status;
	private String refund_fail_reason;
	private String out_ticket_billno;
	public String getElong_ticket_type() {
		return elong_ticket_type;
	}
	public void setElong_ticket_type(String elongTicketType) {
		elong_ticket_type = elongTicketType;
	}
	public String getElong_ids_type() {
		return elong_ids_type;
	}
	public void setElong_ids_type(String elongIdsType) {
		elong_ids_type = elongIdsType;
	}
	public String getElong_seat_type() {
		return elong_seat_type;
	}
	public void setElong_seat_type(String elongSeatType) {
		elong_seat_type = elongSeatType;
	}
	public String getTrain_box() {
		return train_box;
	}
	public void setTrain_box(String trainBox) {
		train_box = trainBox;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refundStatus) {
		refund_status = refundStatus;
	}
	public String getRefund_fail_reason() {
		return refund_fail_reason;
	}
	public void setRefund_fail_reason(String refundFailReason) {
		refund_fail_reason = refundFailReason;
	}
	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}
	public void setOut_ticket_billno(String outTicketBillno) {
		out_ticket_billno = outTicketBillno;
	}
	public String getSeat_no() {
		return seat_no;
	}
	public void setSeat_no(String seatNo) {
		seat_no = seatNo;
	}
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cp_id) {
		this.cp_id = cp_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getTicket_type() {
		return ticket_type;
	}
	public void setTicket_type(String ticket_type) {
		this.ticket_type = ticket_type;
	}
	public String getIds_type() {
		return ids_type;
	}
	public void setIds_type(String ids_type) {
		this.ids_type = ids_type;
	}
	public String getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seat_type) {
		this.seat_type = seat_type;
	}
}
