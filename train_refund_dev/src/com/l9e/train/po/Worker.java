package com.l9e.train.po;

public class Worker {
	public String workerExt;
	public Integer workerType;
	public String workerId;
	public String workerName;
	public String robotId;
	
	public static Integer Robot_refund = new Integer(8);//退票机器人
	
	
	public static String STATUS_WORKING = "00";
	public static String STATUS_STOP = "22";
	public static String STATUS_SPARE = "33";
	
	
	public String getRobotId() {
		return robotId;
	}
	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}
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
}
