package com.l9e.transaction.vo;

public class DBRemedyNoticeVo {
	private String id;
	private String order_id;
	private String notify_status;
	private Integer notify_num;
	private String notify_time;
	private String notify_finish_time;
	private String create_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getNotify_status() {
		return notify_status;
	}
	public void setNotify_status(String notifyStatus) {
		notify_status = notifyStatus;
	}
	public Integer getNotify_num() {
		return notify_num;
	}
	public void setNotify_num(Integer notifyNum) {
		notify_num = notifyNum;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notifyTime) {
		notify_time = notifyTime;
	}
	public String getNotify_finish_time() {
		return notify_finish_time;
	}
	public void setNotify_finish_time(String notifyFinishTime) {
		notify_finish_time = notifyFinishTime;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	
	
}
