package com.l9e.transaction.vo;

public class Worker {
	private String workerExt;
	private Integer workerType;// 机器人类型
	private String workerId;
	private String workerName;// 机器人的名字
	private String workerStatus; // 工作状态
	private int spareThread;
	private String robotId;
	private String publicIp;

	private String proxyStatus;// 代理状态

	public static String STATUS_WORKING = "00";
	public static String STATUS_STOP = "22";
	public static final String STATUS_SPARE = "33";
	public static final int BOOK_ROBOT = 1;

	public String getProxyStatus() {
		return proxyStatus;
	}

	public void setProxyStatus(String proxyStatus) {
		this.proxyStatus = proxyStatus;
	}

	public String getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(String workerStatus) {
		this.workerStatus = workerStatus;
	}

	public int getSpareThread() {
		return spareThread;
	}

	public void setSpareThread(int spareThread) {
		this.spareThread = spareThread;
	}

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

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
}
