package com.l9e.train.po;

public class Worker {
	public String workerExt;
	public Integer workerType;
	public String workerId;
	public String workerName;
	
	public static Integer Robot_12306 = new Integer(1);//订票处理机器人
	public static Integer ORDER_Manual = new Integer(2);//订购人工
	public static Integer Robot_pay = new Integer(3);//支付机器人
	
	
	public static String STATUS_WORKING = "00";
	public static String STATUS_STOP = "22";
	public static String STATUS_SPARE = "33";
	
	private String pay_device_type;
	
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	
	public String getWorkerExt() {
		return workerExt;
	}
	public void setWorkerExt(String workerExt) {
		this.workerExt = workerExt;
	}
	public Integer getWorkerType() {
		return workerType;
	}
	public void setWorkerType(Integer workerType) {
		this.workerType = workerType;
	}
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	public String getPay_device_type() {
		return pay_device_type;
	}
	public void setPay_device_type(String pay_device_type) {
		this.pay_device_type = pay_device_type;
	}
	
}
