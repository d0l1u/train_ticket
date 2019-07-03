package com.l9e.train.po;

public class WorkerVo {
	public static final String STATUS_FREE = "00";
	public static final String STATUS_WORKING = "11";
	public static final String STATUS_QUEUE = "01";
	private Integer workerId;
	private String workerName;
	private Integer workerType;
	private String workerStatus;
	private String workerExt;
	private String workTime;
	private Integer workNum;
	private String pay_device_type;

	public Integer getWorkerId() {
		return this.workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return this.workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Integer getWorkerType() {
		return this.workerType;
	}

	public void setWorkerType(Integer workerType) {
		this.workerType = workerType;
	}

	public String getWorkerStatus() {
		return this.workerStatus;
	}

	public void setWorkerStatus(String workerStatus) {
		this.workerStatus = workerStatus;
	}

	public String getWorkerExt() {
		return this.workerExt;
	}

	public void setWorkerExt(String workerExt) {
		this.workerExt = workerExt;
	}

	public String getWorkTime() {
		return this.workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public Integer getWorkNum() {
		return this.workNum;
	}

	public void setWorkNum(Integer workNum) {
		this.workNum = workNum;
	}

	public String getPay_device_type() {
		return this.pay_device_type;
	}

	public void setPay_device_type(String pay_device_type) {
		this.pay_device_type = pay_device_type;
	}
}
