package com.l9e.transaction.vo;

import java.util.HashMap;
import java.util.Map;

public class RefundVo {

	String refund_memo;
	String order_id;
	String refund_money;
	String refund_purl;
	String refund_status;
	
	public static final Map<String, String> REFUND_STATUS_MAP = new HashMap<String, String>();
	static{
		REFUND_STATUS_MAP.put("1", "用户申请退款");
		REFUND_STATUS_MAP.put("2", "出票差额退款");
		REFUND_STATUS_MAP.put("4", "改签差额退款");
		REFUND_STATUS_MAP.put("3", "出票失败退款");
		REFUND_STATUS_MAP.put("5", "改签单退款");
	}
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
