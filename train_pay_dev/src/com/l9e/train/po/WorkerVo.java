package com.l9e.train.po;

/**
 * 机器人
 * 
 * @author licheng
 * 
 */
public class WorkerVo {
	
	/**
	 * 机器人状态：空闲
	 */
	public static final String STATUS_FREE = "00";
	/**
	 * 机器人状态：工作中
	 */
	public static final String STATUS_WORKING = "11";
	/**
	 * 机器人状态：队列
	 */
	public static final String STATUS_QUEUE = "01";

	/**
	 * 机器人id
	 */
	private Integer workerId;
	/**
	 * 机器人名称
	 */
	private String workerName;
	/**
	 * 机器人类型
	 */
	private Integer workerType;
	/**
	 * 机器人状态
	 */
	//{"success":true,"msg":"操作成功","data":{"workerId":474,"workerName":"机器人112.222","workerType":1,
	//"workerStatus":"11","workerExt":"http://112.74.210.222:8091/RunScript","workTime":"workTime","workNum":1291}}
	private String workerStatus;
	/**
	 * 扩展字段
	 */
	private String workerExt;
	/**
	 * 工作时间
	 */
	private String workTime;
	/**
	 * 工作次数
	 */
	private Integer workNum;
	
	private String pay_device_type;

	public WorkerVo() {
		super();
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Integer getWorkerType() {
		return workerType;
	}

	public void setWorkerType(Integer workerType) {
		this.workerType = workerType;
	}

	public String getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(String workerStatus) {
		this.workerStatus = workerStatus;
	}

	public String getWorkerExt() {
		return workerExt;
	}

	public void setWorkerExt(String workerExt) {
		this.workerExt = workerExt;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public Integer getWorkNum() {
		return workNum;
	}

	public void setWorkNum(Integer workNum) {
		this.workNum = workNum;
	}

	public String getPay_device_type() {
		return pay_device_type;
	}

	public void setPay_device_type(String pay_device_type) {
		this.pay_device_type = pay_device_type;
	}
	
}
