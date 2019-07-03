package com.l9e.transaction.vo;

/**
 * 车站信息
 * 
 * @author licheng
 * 
 */
public class SInfo {

	/**
	 * 车次
	 */
	private String checi;
	/**
	 * 车站名称
	 */
	private String name;
	/**
	 * 发车时间
	 */
	private String startTime;
	/**
	 * 到达时间
	 */
	private String arriveTime;
	/**
	 * 经过天数
	 */
	private Integer cost;

	public SInfo() {
		super();
	}

	public String getCheci() {
		return checi;
	}

	public void setCheci(String checi) {
		this.checi = checi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

}
