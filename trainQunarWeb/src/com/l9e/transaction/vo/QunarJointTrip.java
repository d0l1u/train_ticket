package com.l9e.transaction.vo;

import java.io.Serializable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class QunarJointTrip implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String trainNo;
	private String seq;	
	private String trainStartTime;	
	private String trainEndTime;	
	private String dptStation;	
	private String arrStation;
	private JSONObject seat;//{"4":651}
	private JSONArray extSeat;//[{"4":651}]
	
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getTrainStartTime() {
		return trainStartTime;
	}
	public void setTrainStartTime(String trainStartTime) {
		this.trainStartTime = trainStartTime;
	}
	public String getTrainEndTime() {
		return trainEndTime;
	}
	public void setTrainEndTime(String trainEndTime) {
		this.trainEndTime = trainEndTime;
	}
	public String getDptStation() {
		return dptStation;
	}
	public void setDptStation(String dptStation) {
		this.dptStation = dptStation;
	}
	public String getArrStation() {
		return arrStation;
	}
	public void setArrStation(String arrStation) {
		this.arrStation = arrStation;
	}
	public JSONObject getSeat() {
		return seat;
	}
	public void setSeat(JSONObject seat) {
		this.seat = seat;
	}
	public JSONArray getExtSeat() {
		return extSeat;
	}
	public void setExtSeat(JSONArray extSeat) {
		this.extSeat = extSeat;
	}
	
}
