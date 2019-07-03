package com.l9e.entity;

import java.util.List;

/**
 * @author meizs
 * 携程站站查询数据实体
 */
public class CtripZhanZhanData {
	
	private String TrainNo;//车次编号，K1453
	private String Train12306No;//车次代码，24000K14530K
	private String FromStationName;//出发站，北京西
	private String ToStationName;//到达站，九江
	private String StartTime;//出发时间
	private String ArriveTime;//到达时间
	private String ToTelcode;//到达站三字码
	private String FromTelcode;//出发站三字码
	private String FromStationTypeName;//起点
	private String ToStationTypeName;//途经
	private int DayDiff;//历时天数
	private int DurationMinutes; //历时分钟数
	private String SaleTime; //售卖时间
	private int ControlDay;//售卖天数
	private String CanWebBuy;//是否可预订
	private String IsSupportCard;//是否支持积分
	private String note;//提示
	
	private List<CtripSeatData> Seats; //坐席数据(余票数,票价)
	
	public String getTrainNo() {
		return TrainNo;
	}
	public void setTrainNo(String trainNo) {
		TrainNo = trainNo;
	}
	public String getTrain12306No() {
		return Train12306No;
	}
	public void setTrain12306No(String train12306No) {
		Train12306No = train12306No;
	}
	public String getFromStationName() {
		return FromStationName;
	}
	public void setFromStationName(String fromStationName) {
		FromStationName = fromStationName;
	}
	public String getToStationName() {
		return ToStationName;
	}
	public void setToStationName(String toStationName) {
		ToStationName = toStationName;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getArriveTime() {
		return ArriveTime;
	}
	public void setArriveTime(String arriveTime) {
		ArriveTime = arriveTime;
	}
	public String getToTelcode() {
		return ToTelcode;
	}
	public void setToTelcode(String toTelcode) {
		ToTelcode = toTelcode;
	}
	public String getFromTelcode() {
		return FromTelcode;
	}
	public void setFromTelcode(String fromTelcode) {
		FromTelcode = fromTelcode;
	}
	public String getFromStationTypeName() {
		return FromStationTypeName;
	}
	public void setFromStationTypeName(String fromStationTypeName) {
		FromStationTypeName = fromStationTypeName;
	}
	public String getToStationTypeName() {
		return ToStationTypeName;
	}
	public void setToStationTypeName(String toStationTypeName) {
		ToStationTypeName = toStationTypeName;
	}
	
	public String getSaleTime() {
		return SaleTime;
	}
	public void setSaleTime(String saleTime) {
		SaleTime = saleTime;
	}
	public int getControlDay() {
		return ControlDay;
	}
	public void setControlDay(int controlDay) {
		ControlDay = controlDay;
	}
	public String getCanWebBuy() {
		return CanWebBuy;
	}
	public void setCanWebBuy(String canWebBuy) {
		CanWebBuy = canWebBuy;
	}
	public String getIsSupportCard() {
		return IsSupportCard;
	}
	public void setIsSupportCard(String isSupportCard) {
		IsSupportCard = isSupportCard;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<CtripSeatData> getSeats() {
		return Seats;
	}
	public void setSeats(List<CtripSeatData> seats) {
		Seats = seats;
	}
	public int getDayDiff() {
		return DayDiff;
	}
	public void setDayDiff(int dayDiff) {
		DayDiff = dayDiff;
	}
	public int getDurationMinutes() {
		return DurationMinutes;
	}
	public void setDurationMinutes(int durationMinutes) {
		DurationMinutes = durationMinutes;
	}
}
