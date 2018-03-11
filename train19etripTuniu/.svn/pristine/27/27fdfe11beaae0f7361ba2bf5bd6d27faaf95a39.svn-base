package com.l9e.transaction.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 车站
 * 
 * @author licheng
 * 
 */
public class Station {

	/**
	 * 主键
	 */
	@JsonProperty("stationId")
	private Integer id;
	/**
	 * 车站编号
	 */
	@JsonProperty("stationNum")
	private Integer stationNo;
	/**
	 * 车次
	 */
	@JsonIgnore
	private String trainCode;
	/**
	 * 车站名称
	 */
	@JsonProperty("stationName")
	private String name;
	/**
	 * 到达时间
	 */
	@JsonProperty("arriveTime")
	private String arriveTime;
	/**
	 * 出发时间
	 */
	@JsonProperty("departTime")
	private String startTime;
	/**
	 * 停留时间
	 */
	private String stayTime;

	public Station() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStationNo() {
		return stationNo;
	}

	public void setStationNo(Integer stationNo) {
		this.stationNo = stationNo;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStayTime() {
		return stayTime;
	}

	public void setStayTime(String stayTime) {
		this.stayTime = stayTime;
	}

}
