package com.l9e.train.po;

public class Worker {
	private String workerExt;
	private Integer workerType;
	private String workerId;
	private String workerName;
	private String publicIp;
	private String language;
	public static final Integer Robot_12306 = new Integer(1);
	public static final Integer ORDER_Manual = new Integer(2);
	public static final Integer Robot_pay = new Integer(3);
	public static final String STATUS_WORKING = "00";
	public static final String STATUS_STOP = "22";
	public static final String STATUS_SPARE = "33";
	private String pay_device_type;

	public String getWorkerName() {
		return this.workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerExt() {
		return this.workerExt;
	}

	public void setWorkerExt(String workerExt) {
		this.workerExt = workerExt;
	}

	public Integer getWorkerType() {
		return this.workerType;
	}

	public void setWorkerType(Integer workerType) {
		this.workerType = workerType;
	}

	public String getWorkerId() {
		return this.workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getPay_device_type() {
		return this.pay_device_type;
	}

	public void setPay_device_type(String pay_device_type) {
		this.pay_device_type = pay_device_type;
	}

	public String getPublicIp() {
		return this.publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
