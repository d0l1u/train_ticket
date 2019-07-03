package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class DifferVo {
	private String differ_id ;//主键ID
	private String refund_seq;//差额退款流水号
	private String order_id;//订单id
	private String eop_order_id;//eop订单id
	private String eop_refund_seq;//eop退款流水号
	private String refund_money;//退款金额
	private String create_time;//创建时间
	private String differ_status;//状态
	private String notify_time;//通知开始时间
	private String refund_time;//退款时间
	private String notify_num;//通知次数
	private String modfiy_time;//修改时间
	
	
	private static final String FITOUT = "00";//准备退款
	private static final String WAIT = "11";//等待退款
	private static final String START = "22";//开始退款
	private static final String EOPING = "33";//EOP退款中
	private static final String ACHIEVE = "44";//退款完成
	
	static Map<String,String>differStatus = new LinkedHashMap<String,String>();
	
	public static Map<String, String> getStatus(){
		if(differStatus.isEmpty()){
			differStatus.put(FITOUT, "准备退款");
			differStatus.put(WAIT, "等待退款");
			differStatus.put(START, "开始退款");
			differStatus.put(EOPING, "EOP退款中");
			differStatus.put(ACHIEVE, "退款完成");
		}
		return differStatus ;
	}
	
	public String getDiffer_id() {
		return differ_id;
	}
	public void setDiffer_id(String differ_id) {
		this.differ_id = differ_id;
	}
	public String getRefund_seq() {
		return refund_seq;
	}
	public void setRefund_seq(String refund_seq) {
		this.refund_seq = refund_seq;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getEop_order_id() {
		return eop_order_id;
	}
	public void setEop_order_id(String eop_order_id) {
		this.eop_order_id = eop_order_id;
	}
	public String getEop_refund_seq() {
		return eop_refund_seq;
	}
	public void setEop_refund_seq(String eop_refund_seq) {
		this.eop_refund_seq = eop_refund_seq;
	}
	public String getRefund_money() {
		return refund_money;
	}
	public void setRefund_money(String refund_money) {
		this.refund_money = refund_money;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDiffer_status() {
		return differ_status;
	}
	public void setDiffer_status(String differ_status) {
		this.differ_status = differ_status;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}
	public String getRefund_time() {
		return refund_time;
	}
	public void setRefund_time(String refund_time) {
		this.refund_time = refund_time;
	}
	public String getNotify_num() {
		return notify_num;
	}
	public void setNotify_num(String notify_num) {
		this.notify_num = notify_num;
	}
	public String getModfiy_time() {
		return modfiy_time;
	}
	public void setModfiy_time(String modfiy_time) {
		this.modfiy_time = modfiy_time;
	}
	
	
}
