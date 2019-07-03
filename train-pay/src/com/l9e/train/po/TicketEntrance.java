package com.l9e.train.po;

import org.codehaus.jackson.annotate.JsonProperty;

public class TicketEntrance {
	
	/**
	 * 订单检票口表主键
	 */
	public Integer autoId;
	
	/**
	 * 订单ID
	 */
	public String orderId;
	
	/**
	 * 车次
	 */
	@JsonProperty("trainNum")  
	public String trainNum;
	
	/**
	 * 检票站名
	 */
	@JsonProperty("stationName")  
	public String stationName;
	
	/**
	 * 检票口
	 */
	@JsonProperty("entrance")  
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
		return trainNum;
	}

	public void setTrainNum(String trainNum) {
		this.trainNum = trainNum;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getEntrance() {
		return entrance;
	}

	public void setEntrance(String entrance) {
		this.entrance = entrance;
	}
	
	

}
