package com.l9e.transaction.vo;

public class TuniuQueueOrder {
	//订单号
	private String order_id;
	//排队人数
	private Integer queue_number;
	//排队时间
	private String wait_time;
	//备注信息
	private String msg;
	//创建时间
	private String create_time;
	//操作时间
	private String option_time;
	//通知状态
	private Integer notify_status;
	//通知次数
	private Integer notify_num;
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public Integer getQueue_number() {
		return queue_number;
	}
	public void setQueue_number(Integer queueNumber) {
		queue_number = queueNumber;
	}
	public String getWait_time() {
		return wait_time;
	}
	public void setWait_time(String waitTime) {
		wait_time = waitTime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getOption_time() {
		return option_time;
	}
	public void setOption_time(String optionTime) {
		option_time = optionTime;
	}
	public Integer getNotify_status() {
		return notify_status;
	}
	public void setNotify_status(Integer notifyStatus) {
		notify_status = notifyStatus;
	}
	public Integer getNotify_num() {
		return notify_num;
	}
	public void setNotify_num(Integer notifyNum) {
		notify_num = notifyNum;
	}
	

}
