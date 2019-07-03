package com.l9e.train.po;

public class Worker {
	public String workerExt;
	public Integer workerType;
	public String workerId;
	public String workerName;
	private String publicIp;
	private String scriptType;
	private String osType;

	public static Integer Manual = new Integer(0);// 支付人工

	public static Integer Robot_12306 = new Integer(1);// 机器人
	public static Integer ORDER_Manual = new Integer(2);// 订购人工

	public static Integer CANCEL_ROBOT = new Integer(6); // 取消机器人
	public static Integer PAY_Manual = new Integer(4);// 支付人工

	public static Integer FIND_Manual = new Integer(6);// 取消机器人

	public static String STATUS_WORKING = "00";
	public static String STATUS_STOP = "22";

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

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}
}
