package com.l9e.transaction.vo;

import java.io.Serializable;

public class Midway implements Serializable {

	private int id;
	private String trainCode;//车次
	private String stationName;//车站名
	private String stationCode;//车站简码
	private String arriveTime;//到达时间
	private String startTime;//开始时间
	private int stationNo;//途经站no
	private String stopoverTime;//停留时间
	private String distance;//里程
	private String costtime;//第几日到达
	private String costday;//耗时时间
	
	public Midway() {
		super();
	}

	public Midway(String trainCode, String stationName, String startTime,
			int stationNo, String stopoverTime, String distance) {
		super();
		this.trainCode = trainCode;
		this.stationName = stationName;
		this.startTime = startTime;
		this.stationNo = stationNo;
		this.stopoverTime = stopoverTime;
		this.distance = distance;
	}

	public Midway(int id, String trainCode, String stationName,
			String stationCode, String arriveTime, String startTime, int stationNo,
			String stopoverTime, String distance) {
		super();
		this.id = id;
		this.trainCode = trainCode;
		this.stationName = stationName;
		this.stationCode = stationCode;
		this.arriveTime = arriveTime;
		this.startTime = startTime;
		this.stationNo = stationNo;
		this.stopoverTime = stopoverTime;
		this.distance = distance;
	}

	public String getCostday() {
		return costday;
	}

	public void setCostday(String costday) {
		this.costday = costday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
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

	public int getStationNo() {
		return stationNo;
	}

	public void setStationNo(int stationNo) {
		this.stationNo = stationNo;
	}

	public String getStopoverTime() {
		return stopoverTime;
	}

	public void setStopoverTime(String stopoverTime) {
		this.stopoverTime = stopoverTime;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCosttime() {
		return costtime;
	}

	public void setCosttime(String costtime) {
		this.costtime = costtime;
	}
	@Override
	public String toString() {
		return "Midway [id=" + id + ", trainCode=" + trainCode
				+ ", stationName=" + stationName + ", stationCode="
				+ stationCode + ", arriveTime=" + arriveTime + ", startTime="
				+ startTime + ", stationNo=" + stationNo + ", stopoverTime="
				+ stopoverTime + ", distance=" + distance + ", costtime="
				+ costtime + "]";
	}
}
