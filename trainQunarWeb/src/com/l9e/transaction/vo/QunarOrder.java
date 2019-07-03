package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class QunarOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String orderNo;
	private String orderType;//0、普通订单 1、联程订单
	private String orderDate;	
	private String trainNo;
	private String trainStartTime;
	private String trainEndTime;
	private String dptStation;//出发站
	private String arrStation;//到达站
	private String isPaper;
	private JSONObject seat;//{"4":651}
	private JSONArray extSeat;//[{"4":651}]
	private double ticketPay;
	private String secondRefund;//:0非极速退款订单，1极速退款订单
	private List<QunarPassenger> passengers;
	private List<QunarJointTrip> jointTrip;
	private JSONArray extSeatMap;
	private JSONObject seatMap;
	
	
	public String getSecondRefund() {
		return secondRefund;
	}
	public void setSecondRefund(String secondRefund) {
		this.secondRefund = secondRefund;
	}
	public String getIsPaper() {
		return isPaper;
	}
	public void setIsPaper(String isPaper) {
		this.isPaper = isPaper;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getTrainEndTime() {
		return trainEndTime;
	}
	public void setTrainEndTime(String trainEndTime) {
		this.trainEndTime = trainEndTime;
	}
	public List<QunarJointTrip> getJointTrip() {
		return jointTrip;
	}
	public void setJointTrip(List<QunarJointTrip> jointTrip) {
		this.jointTrip = jointTrip;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getTrainStartTime() {
		return trainStartTime;
	}
	public void setTrainStartTime(String trainStartTime) {
		this.trainStartTime = trainStartTime;
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
	public double getTicketPay() {
		return ticketPay;
	}
	public void setTicketPay(double ticketPay) {
		this.ticketPay = ticketPay;
	}
	public List<QunarPassenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<QunarPassenger> passengers) {
		this.passengers = passengers;
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
	public JSONArray getExtSeatMap() {
		return extSeatMap;
	}
	public void setExtSeatMap(JSONArray extSeatMap) {
		this.extSeatMap = extSeatMap;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public JSONObject getSeatMap() {
		return seatMap;
	}
	public void setSeatMap(JSONObject seatMap) {
		this.seatMap = seatMap;
	}
	
	
}
