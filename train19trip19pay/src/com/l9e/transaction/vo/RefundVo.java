package com.l9e.transaction.vo;

import java.io.Serializable;

public class RefundVo implements Serializable {

	String refund_memo;
	String order_id;
	String refund_money;//退款金额
	String refund_purl;
	String refund_status;
	
	private String refund_seq;//退款流水号
	private String plat_refund_seq;	//19pay退款流水号，可以去19pay查询退款状态
	private String actual_refund_money;		//退款成功金额
	private String refund_time;//退款完成时间
	private String remark;			//备注信息
	
	
	
	public String getPlat_refund_seq() {
		return plat_refund_seq;
	}
	public void setPlat_refund_seq(String platRefundSeq) {
		plat_refund_seq = platRefundSeq;
	}
	public String getActual_refund_money() {
		return actual_refund_money;
	}
	public void setActual_refund_money(String actualRefundMoney) {
		actual_refund_money = actualRefundMoney;
	}
	public String getRefund_time() {
		return refund_time;
	}
	public void setRefund_time(String refundTime) {
		refund_time = refundTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRefund_seq() {
		return refund_seq;
	}
	public void setRefund_seq(String refundSeq) {
		refund_seq = refundSeq;
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
