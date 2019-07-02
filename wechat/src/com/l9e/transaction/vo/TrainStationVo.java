package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * 
 * @author zuoyx
 * 途经车站信息
 */
public class TrainStationVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String train_no;
	
	private String station;
	
	private String arrive_time;
	
	private String start_time;
	
	private int stop_time;
	
	private int station_index;
	
	private double distance;
	
	private int days;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}

	public String getTrain_name() {
		return station;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getArrive_time() {
		return arrive_time;
	}

	public void setArrive_time(String arriveTime) {
		arrive_time = arriveTime;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String startTime) {
		start_time = startTime;
	}

	public int getStop_time() {
		return stop_time;
	}

	public void setStop_time(int stopTime) {
		stop_time = stopTime;
	}

	public int getStation_index() {
		return station_index;
	}

	public void setStation_index(int stationIndex) {
		station_index = stationIndex;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
}
