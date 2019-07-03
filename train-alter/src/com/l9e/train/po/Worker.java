package com.l9e.train.po;

//机器人实体类
public class Worker {
	private String workerExt;
	private Integer workerType;
	private String workerId;
	private String workerName;
	private String robotId;
	private Integer pay_device_type;
	private String publicIp;
	private String script;
	
	//账号类型 1、预定机器人 2、人工 3、支付机器人 5、核验机器人6、取消机器人7、改签机器人、8退票机器人9、查询机器人 10、实名认证 11、查询票价机器人 12、保险
	public static Integer Robot_12306 = new Integer(1);//订票处理机器人
	public static Integer ORDER_Manual = new Integer(2);//订购人工
	public static Integer Robot_pay = new Integer(3);//支付机器人
	
	public static Integer Robot_alert = new Integer(7);//改签机器人
	public static Integer Robot_refund = new Integer(8);//退票机器人
	
	//工作状态：00、空闲  11、工作中  22、停用  33 备用   99 需要人工处理  01 在队列中 02 预登录
	public static String STATUS_FREE = "00";
	public static String STATUS_STOP = "22";
	public static String STATUS_SPARE = "33";
	
	//支付终端类型   0--PC端支付  1-APP端支付
	public static Integer STATUS_PC = 0;
	public static Integer STATUS_APP = 1;
	
	
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
	public Integer getPay_device_type() {
		return pay_device_type;
	}
	public void setPay_device_type(Integer payDeviceType) {
		pay_device_type = payDeviceType;
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
