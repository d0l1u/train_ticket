package com.l9e.transaction.vo;

public class RefundVo {

	String refund_memo;
	String order_id;
	String refund_money;
	String refund_purl;
	String refund_status;
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	public String getRefund_memo() {
		return refund_memo;
	}
	public void setRefund_memo(String refund_memo) {
		this.refund_memo = refund_memo;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getRefund_money() {
		return refund_money;
	}
	public void setRefund_money(String refund_money) {
		this.refund_money = refund_money;
	}
	public String getRefund_purl() {
		return refund_purl;
	}
	public void setRefund_purl(String refund_purl) {
		this.refund_purl = refund_purl;
	}
	
}
