package com.l9e.transaction.vo;

import java.io.Serializable;

public class Station implements Serializable {
	private int stationId;					//车站名id
	private String stationCode;				//车站名code编码
	private String stationName;				//车站名称
	private String stationPinyin;			//车站名拼音
	private String abbreviatedStation;		//车站名简写
	private String firstLettersOfStation;	//车站名首字母缩写
	private String level;					//车站级别
	private String city;					//车站所属城市
	private String status;				//车站状态：00营运车站 11停运车站
	
	public static String STATIONUSING = "00";		//00营运车站
	public static String STATIONSTOP = "11";	//11停运车站

	public Station() {
		super();
	}

	public Station(int stationId, String stationCode, String stationName,
			String stationPinyin, String abbreviatedStation,
			String firstLettersOfStation, String status) {
		super();
		this.stationId = stationId;
		this.stationCode = stationCode;
		this.stationName = stationName;
		this.stationPinyin = stationPinyin;
		this.abbreviatedStation = abbreviatedStation;
		this.firstLettersOfStation = firstLettersOfStation;
		this.status = status;
	}

	public Station(int stationId, String stationCode, String stationName,
			String stationPinyin, String abbreviatedStation,
			String firstLettersOfStation, String level, String city, String status) {
		super();
		this.stationId = stationId;
		this.level = level;
		this.city = city;
		this.status = status;
		this.stationCode = stationCode;
		this.stationName = stationName;
		this.stationPinyin = stationPinyin;
		this.abbreviatedStation = abbreviatedStation;
		this.firstLettersOfStation = firstLettersOfStation;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationPinyin() {
		return stationPinyin;
	}

	public void setStationPinyin(String stationPinyin) {
		this.stationPinyin = stationPinyin;
	}

	public String getAbbreviatedStation() {
		return abbreviatedStation;
	}

	public void setAbbreviatedStation(String abbreviatedStation) {
		this.abbreviatedStation = abbreviatedStation;
	}

	public String getfirstLettersOfStation() {
		return firstLettersOfStation;
	}

	public void setfirstLettersOfStation(String firstLettersOfStation) {
		this.firstLettersOfStation = firstLettersOfStation;
	}

	public String getStationDetail() {
		return stationId + ", " + stationName + ", " + stationCode + ", "
				+ stationPinyin + ", " + firstLettersOfStation + ", "
				+ abbreviatedStation;
	}
}
