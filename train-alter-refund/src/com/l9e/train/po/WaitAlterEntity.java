package com.l9e.train.po;

public class WaitAlterEntity {
	private String train_no;	//车次
	private String seat_price;	//坐席票价
	private String seat_type;	//坐席类型
	private String travel_time;	//发车日期
	private String from_time;	//发车时间
	
	private boolean wea_48;
	private double hand_price;	//手续费
	
	public boolean isWea_48() {
		return wea_48;
	}
	public void setWea_48(boolean wea_48) {
		this.wea_48 = wea_48;
	}
	public double getHand_price() {
		return hand_price;
	}
	public void setHand_price(double handPrice) {
		hand_price = handPrice;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String fromTime) {
		from_time = fromTime;
	}
	public String getTrain_no() {
		return train_no;
	}
	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}
	public String getSeat_price() {
		return seat_price;
	}
	public void setSeat_price(String seatPrice) {
		seat_price = seatPrice;
	}
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seatType) {
		seat_type = seatType;
	}
	public String getTravel_time() {
		return travel_time;
	}
	public void setTravel_time(String travelTime) {
		travel_time = travelTime;
	}
}
