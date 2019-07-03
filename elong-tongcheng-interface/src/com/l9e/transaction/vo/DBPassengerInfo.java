package com.l9e.transaction.vo;

public class DBPassengerInfo {
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
	private String alter_seat_type;
	private String alter_train_box;
	private String alter_seat_no;
	private String alter_buy_money;
	private String alter_train_no;
	private String alter_money;
	private String refund_12306_money;

	/* 同程扩展业务参数 */
	/**
	 * 身份状态标识id
	 */
	private String identityStatusId;
	/**
	 * 身份状态标识描述
	 */
	private String identityStatusMsg;

	/** 同城扩展字段 */
	private String passengerid;
	private String out_passengerid;

	public String getOut_passengerid() {
		return out_passengerid;
	}

	public void setOut_passengerid(String outPassengerid) {
		out_passengerid = outPassengerid;
	}

	public String getCp_id() {
		return cp_id;
	}

	public void setCp_id(String cpId) {
		cp_id = cpId;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String orderId) {
		order_id = orderId;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String userName) {
		user_name = userName;
	}

	public String getElong_ticket_type() {
		return elong_ticket_type;
	}

	public void setElong_ticket_type(String elongTicketType) {
		elong_ticket_type = elongTicketType;
	}

	public String getTicket_type() {
		return ticket_type;
	}

	public void setTicket_type(String ticketType) {
		ticket_type = ticketType;
	}

	public String getElong_ids_type() {
		return elong_ids_type;
	}

	public void setElong_ids_type(String elongIdsType) {
		elong_ids_type = elongIdsType;
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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
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

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modifyTime) {
		modify_time = modifyTime;
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

	public String getAlter_seat_type() {
		return alter_seat_type;
	}

	public void setAlter_seat_type(String alterSeatType) {
		alter_seat_type = alterSeatType;
	}

	public String getAlter_train_box() {
		return alter_train_box;
	}

	public void setAlter_train_box(String alterTrainBox) {
		alter_train_box = alterTrainBox;
	}

	public String getAlter_seat_no() {
		return alter_seat_no;
	}

	public void setAlter_seat_no(String alterSeatNo) {
		alter_seat_no = alterSeatNo;
	}

	public String getAlter_buy_money() {
		return alter_buy_money;
	}

	public void setAlter_buy_money(String alterBuyMoney) {
		alter_buy_money = alterBuyMoney;
	}

	public String getAlter_train_no() {
		return alter_train_no;
	}

	public void setAlter_train_no(String alterTrainNo) {
		alter_train_no = alterTrainNo;
	}

	public String getAlter_money() {
		return alter_money;
	}

	public void setAlter_money(String alterMoney) {
		alter_money = alterMoney;
	}

	public String getRefund_12306_money() {
		return refund_12306_money;
	}

	public void setRefund_12306_money(String refund_12306Money) {
		refund_12306_money = refund_12306Money;
	}

	public void setPassengerid(String passengerid) {
		this.passengerid = passengerid;
	}

	public String getPassengerid() {
		return passengerid;
	}

	public String getIdentityStatusId() {
		return identityStatusId;
	}

	public void setIdentityStatusId(String identityStatusId) {
		this.identityStatusId = identityStatusId;
	}

	public String getIdentityStatusMsg() {
		return identityStatusMsg;
	}

	public void setIdentityStatusMsg(String identityStatusMsg) {
		this.identityStatusMsg = identityStatusMsg;
	}

}
