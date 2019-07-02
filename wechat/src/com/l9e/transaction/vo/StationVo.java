package com.l9e.transaction.vo;

import java.io.Serializable;

public class StationVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String arrtime;
	private String costtime;
	private String distance;
	private String interval;
	private String name;
	private String starttime;
	private String stationno;

	public String getArrtime() {
		return arrtime;
	}

	public void setArrtime(String arrtime) {
		this.arrtime = arrtime;
	}

	public String getCosttime() {
		return costtime;
	}

	public void setCosttime(String costtime) {
		this.costtime = costtime;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getStationno() {
		return stationno;
	}

	public void setStationno(String stationno) {
		this.stationno = stationno;
	}

	public StationVo(String arrtime, String costtime, String distance,
			String interval, String name, String starttime, String stationno) {
		super();
		this.arrtime = arrtime;
		this.costtime = costtime;
		this.distance = distance;
		this.interval = interval;
		this.name = name;
		this.starttime = starttime;
		this.stationno = stationno;
	}

	public StationVo() {
		super();
	}

}
