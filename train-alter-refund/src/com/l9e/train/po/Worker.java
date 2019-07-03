package com.l9e.train.po;

//机器人实体类
public class Worker {
	public String workerExt;
	public Integer workerType;
	public String workerId;
	public String workerName;
	public String robotId;

	private String publicIp;
	private String script;

	// 账号类型 1、预定机器人 2、人工 3、支付机器人 5、核验机器人6、取消机器人7、改签机器人、8退票机器人9、查询机器人 10、实名认证
	// 11、查询票价机器人 12、保险
	public static Integer Robot_12306 = new Integer(1);// 订票处理机器人
	public static Integer ORDER_Manual = new Integer(2);// 订购人工
	public static Integer Robot_pay = new Integer(3);// 支付机器人

	public static Integer Robot_alert = new Integer(7);// 改签机器人
	public static Integer Robot_refund = new Integer(8);// 退票机器人

	// 工作状态：00、工作中 11、空闲 22、停用 33、备用 44、占座中
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

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
