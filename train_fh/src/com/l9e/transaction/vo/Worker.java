package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * 机器人
 * 
 * @author licheng
 * 
 */
public class Worker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5279588438405002705L;
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
	 * 机器人状态：预登录
	 */
	public static final String STATUS_PREPARED_SIGN = "02";
	/**
	 * 机器人状态：停用
	 */
	public static final String STATUS_STOP = "22";
	/**
	 * 机器人状态：备用
	 */
	public static final String STATUS_PREPARED = "33";
	/**
	 * 机器人类型：预订
	 */
	public static final Integer TYPE_BOOK = 1;
	/**
	 * 机器人类型：人工
	 */
	public static final Integer TYPE_MANUAL = 2;
	/**
	 * 机器人类型：支付
	 */
	public static final Integer TYPE_PAY = 3;
	/**
	 * 机器人类型：核验
	 */
	public static final Integer TYPE_VERIFY = 15;
	/**
	 * 机器人类型：携程出票
	 */
	public static final Integer TYPE_CTRIP = 21;

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
	private String workerStatus;
	/**
	 * 扩展字段
	 */
	private String workerExt;
	/**
	 * 停用原因
	 */
	private String stopReason;
	/**
	 * 工作时间
	 */
	private String workTime;
	/**
	 * 工作次数
	 */
	private Integer workNum;
	/**
	 * 单进程标识
	 */
	private Integer singleOrder;

	/**
	 * 机器脚本语言类型
	 */
	private Integer workerLangType;

	private String publicIp;

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public Worker() {
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

	public String getStopReason() {
		return stopReason;
	}

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
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

	public Integer getSingleOrder() {
		return singleOrder;
	}

	public void setSingleOrder(Integer singleOrder) {
		this.singleOrder = singleOrder;
	}

	public Integer getWorkerLangType() {
		return workerLangType;
	}

	public void setWorkerLangType(Integer workerLangType) {
		this.workerLangType = workerLangType;
	}

	@Override
	public String toString() {
		return "Worker [singleOrder=" + singleOrder + ", stopReason=" + stopReason + ", workNum=" + workNum + ", workTime=" + workTime + ", workerExt="
				+ workerExt + ", workerId=" + workerId + ", workerName=" + workerName + ", workerStatus=" + workerStatus + ", workerType=" + workerType
				+ ", workerLangType=" + workerLangType + "]";
	}

}
