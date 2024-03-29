package com.l9e.transaction.vo;

public class TuniuUrgeRefund {
	//主键
	private int urge_refund_id;
	//订单号
	private String order_id;
	//车票号
	private String cp_id;
	//12306单号
	private String out_ticket_billno;
	//催退款状态
	private String urge_status;
	//退款金额
	private String refund_money;
	//备注
	private String remark;
	//失败原因
	private String fail_reason;
	private String create_time;
	private String opt_time;
	private String opt_person;
	public int getUrge_refund_id() {
		return urge_refund_id;
	}
	public void setUrge_refund_id(int urgeRefundId) {
		urge_refund_id = urgeRefundId;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cpId) {
		cp_id = cpId;
	}
	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}
	public void setOut_ticket_billno(String outTicketBillno) {
		out_ticket_billno = outTicketBillno;
	}
	public String getUrge_status() {
		return urge_status;
	}
	public void setUrge_status(String urgeStatus) {
		urge_status = urgeStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFail_reason() {
		return fail_reason;
	}
	public void setFail_reason(String failReason) {
		fail_reason = failReason;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getOpt_time() {
		return opt_time;
	}
	public void setOpt_time(String optTime) {
		opt_time = optTime;
	}
	public String getOpt_person() {
		return opt_person;
	}
	public void setOpt_person(String optPerson) {
		opt_person = optPerson;
	}
	public String getRefund_money() {
		return refund_money;
	}
	public void setRefund_money(String refundMoney) {
		refund_money = refundMoney;
	}
	
	
}
