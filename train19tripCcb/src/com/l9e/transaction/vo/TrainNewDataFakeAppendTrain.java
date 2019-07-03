package com.l9e.transaction.vo;

import java.io.Serializable;

import com.l9e.util.DateUtil;

/**
 * @author zuoyuxing
 *
 */
public class TrainNewDataFakeAppendTrain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String xh;//排序编号
	
	private String cc;//车次
	
	private String fz;//发车站
	
	private String dz;//出发车站
	
	private String yz;//硬座票价
	
	private String rz;//软座票价
	
	private String yws;//硬卧上票价
	
	private String ywz;//硬卧中票价
	
	private String ywx;//硬卧下票价
	
	private String rws;//软卧上票价
	
	private String rwx;//软卧下票价
	
	private String rz2;//二等座票价
	
	private String rz1;//一等软座票价 
	
	private String start_station_name;
	
	private String end_station_name;
	
	private String start_time;
	
	private String arrive_time;
	
	private String lishi;

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getFz() {
		return fz;
	}

	public void setFz(String fz) {
		this.fz = fz;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getYz() {
		return yz;
	}

	public void setYz(String yz) {
		this.yz = yz;
	}

	public String getRz() {
		return rz;
	}

	public void setRz(String rz) {
		this.rz = rz;
	}

	public String getYws() {
		return yws;
	}

	public void setYws(String yws) {
		this.yws = yws;
	}

	public String getYwz() {
		return ywz;
	}

	public void setYwz(String ywz) {
		this.ywz = ywz;
	}

	public String getYwx() {
		return ywx;
	}

	public void setYwx(String ywx) {
		this.ywx = ywx;
	}

	public String getRws() {
		return rws;
	}

	public void setRws(String rws) {
		this.rws = rws;
	}

	public String getRwx() {
		return rwx;
	}

	public void setRwx(String rwx) {
		this.rwx = rwx;
	}

	public String getRz2() {
		return rz2;
	}

	public void setRz2(String rz2) {
		this.rz2 = rz2;
	}

	public String getRz1() {
		return rz1;
	}

	public void setRz1(String rz1) {
		this.rz1 = rz1;
	}

	public String getStart_station_name() {
		return start_station_name;
	}

	public void setStart_station_name(String startStationName) {
		start_station_name = startStationName;
	}

	public String getEnd_station_name() {
		return end_station_name;
	}

	public void setEnd_station_name(String endStationName) {
		end_station_name = endStationName;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String startTime) {
		start_time = startTime;
	}

	public String getArrive_time() {
		return arrive_time;
	}

	public void setArrive_time(String arriveTime) {
		arrive_time = arriveTime;
	}

	public String getLishi() {
		return lishi;
	}

	public void setLishi(String lishi) {
		this.lishi = lishi;
	}
	
	public void changeData(){
		if(this.lishi!=null){
			this.lishi = DateUtil.minuteFormat(this.lishi);
		}
	}
}
