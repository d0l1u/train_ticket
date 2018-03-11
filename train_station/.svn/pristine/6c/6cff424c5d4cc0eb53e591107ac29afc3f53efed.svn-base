package com.l9e.transaction.vo;

import java.io.Serializable;

public class Train implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//火车状态：00、已存在不需更新，01、已存在火车停驶，02、不存在火车需要更新
	public static String EXIST = "00";
	public static String INEXISTENCE = "01";
	public static String NEEDUPDATE = "02";
	
	private int trainId;
	private String stationTrainCode;
	private String startStation;//始发站
	private String endStation;//终点站
	private String trainNo;//train_no：24000000D10R
	private String trainCode; //车次:D1
	private String level;//车次等级C|Z|K|D|G|T|L
	private String status;//火车状态：00、已存在不需更新，01、已存在火车停驶，02、不存在火车需要更新
	private String startStationCode;
	private String endStationCode;
	private String startDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartStationCode() {
		return startStationCode;
	}

	public void setStartStationCode(String startStationCode) {
		this.startStationCode = startStationCode;
	}

	public String getEndStationCode() {
		return endStationCode;
	}

	public void setEndStationCode(String endStationCode) {
		this.endStationCode = endStationCode;
	}

	public void setStationTrainCode(String stationTrainCode) {
		this.stationTrainCode = stationTrainCode;
	}

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public String getEndStation() {
		return endStation;
	}

	public void seEendStation(String endStation) {
		this.endStation = endStation;
	}

	public Train(String startStation, String endStation, String trainNo,
			String trainCode, String level) {
		super();
		this.startStation = startStation;
		this.endStation = endStation;
		this.trainNo = trainNo;
		this.trainCode = trainCode;
		this.level = level;
	}

	public Train(String stationTrainCode, String startStation,
			String endStation, String trainNo, String trainCode, String level) {
		super();
		this.stationTrainCode = stationTrainCode;
		this.level = level;
		this.trainCode = trainCode;
		this.startStation = startStation;
		this.endStation = endStation;
		this.trainNo = trainNo;
	}

	public Train(int trainId, String stationTrainCode, String startStation,
			String endStation, String trainNo, String trainCode, String level,
			String status, String startStationCode, String endStationCode) {
		super();
		this.trainId = trainId;
		this.stationTrainCode = stationTrainCode;
		this.startStation = startStation;
		this.endStation = endStation;
		this.trainNo = trainNo;
		this.trainCode = trainCode;
		this.level = level;
		this.status = status;
		this.startStationCode = startStationCode;
		this.endStationCode = endStationCode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Train() {
		super();
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getStation_train_code() {
		return trainCode + "(" + startStation + "-" + endStation + ")";
	}

	public String getTrainCode() {
		return trainCode;
	}

	public String getStationTrainCode() {
		return stationTrainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}
}
