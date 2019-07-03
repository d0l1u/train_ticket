package com.l9e.transaction.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketEntrance {
	
	/**
	 * 订单检票口表主键
	 */
	public Integer autoId;
	
	/**
	 * 订单ID
	 */
	@JsonIgnore
	public String orderId;
	
	/**
	 * 车次
	 */
	//@JsonProperty("trainNum")
	public String trainNum;
	
	/**
	 * 检票站名
	 */
	//@JsonProperty("stationName")  
	public String stationName;
	
	/**
	 * 检票口
	 */
	//@JsonProperty("entrance")  
	public String entrance;
	
	

	public Integer getAutoId() {
		return autoId;
	}

	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTrainNum() {
		if (null!=trainNum) {
			return trainNum;
		}else{
			return "";
		}
		
	}

	public void setTrainNum(String trainNum) {
		this.trainNum = trainNum;
	}

	public String getStationName() {
		if (null!=stationName) {
			return stationName;
		}else{
			return "";
		}

	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getEntrance() {
		if (null!=entrance) {
			return entrance;
		}else{
			return "";
		}
	}

	public void setEntrance(String entrance) {
		this.entrance = entrance;
	}
}
	
