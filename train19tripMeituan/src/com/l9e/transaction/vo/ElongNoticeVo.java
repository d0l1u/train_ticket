package com.l9e.transaction.vo;
/**
 * 艺龙出票结果通知实体类
 * @author liuyi02
 * */
public class ElongNoticeVo {
	private String notify_id;
	private String  order_id;
	private String  create_time;
	private String  notify_status;
	private String  notify_time;
	private String  notify_num;
	private String  notify_finish_time;
	private String  order_status;
	private String  beiyong1;
	private String  beiyong2;
	public String getBeiyong1() {
		return beiyong1;
	}
	public void setBeiyong1(String beiyong1) {
		this.beiyong1 = beiyong1;
	}
	public String getBeiyong2() {
		return beiyong2;
	}
	public void setBeiyong2(String beiyong2) {
		this.beiyong2 = beiyong2;
	}
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notifyId) {
		notify_id = notifyId;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getNotify_status() {
		return notify_status;
	}
	public void setNotify_status(String notifyStatus) {
		notify_status = notifyStatus;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notifyTime) {
		notify_time = notifyTime;
	}
	public String getNotify_num() {
		return notify_num;
	}
	public void setNotify_num(String notifyNum) {
		notify_num = notifyNum;
	}
	public String getNotify_finish_time() {
		return notify_finish_time;
	}
	public void setNotify_finish_time(String notifyFinishTime) {
		notify_finish_time = notifyFinishTime;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String orderStatus) {
		order_status = orderStatus;
	}
}
