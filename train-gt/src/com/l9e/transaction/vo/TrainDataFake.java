package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * @author zuoyuxing
 *
 */
public class TrainDataFake implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;//排序编号
	
	private String train_no;//车次
	
	private String train_type;//列车等级
	
	private String from_city;//出发车站
	
	private String from_time;//出发时间
	
	private String to_city;//到达车站
	
	private String to_time;//到达时间
	
	private String begin_city;//始发站
	
	private String end_city;//终点站
	
	private String time_cost;//运行时间
	
	private String yz_price;//硬座票价
	
	private String rz_price;//软座票价
	
	private String rz2_price;//二等软座票价 
	
	private String rz1_price;//一等软座票价
	
	private String yws_price;//硬卧上铺票价
	
	private String ywz_price;//硬卧中铺票价
	
	private String ywx_price;//硬卧下铺票价
	
	private String rws_price;//软卧上铺票价
	
	private String rwx_price;//软卧下铺票价
	
	private String gws_price;//高级软卧上铺票价 
	
	private String gwx_price;//高级软卧下铺票价 
	
	private String swz_price;//商务座票价
	
	private String tdz_price;//特等座票价

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

	public String getTrain_type() {
		return train_type;
	}

	public void setTrain_type(String trainType) {
		train_type = trainType;
	}

	public String getFrom_city() {
		return from_city;
	}

	public void setFrom_city(String fromCity) {
		from_city = fromCity;
	}

	public String getFrom_time() {
		return from_time;
	}

	public void setFrom_time(String fromTime) {
		from_time = fromTime;
	}

	public String getTo_city() {
		return to_city;
	}

	public void setTo_city(String toCity) {
		to_city = toCity;
	}

	public String getTo_time() {
		return to_time;
	}

	public void setTo_time(String toTime) {
		to_time = toTime;
	}

	public String getBegin_city() {
		return begin_city;
	}

	public void setBegin_city(String beginCity) {
		begin_city = beginCity;
	}

	public String getEnd_city() {
		return end_city;
	}

	public void setEnd_city(String endCity) {
		end_city = endCity;
	}

	public String getTime_cost() {
		return time_cost;
	}

	public void setTime_cost(String timeCost) {
		time_cost = timeCost;
	}

	public String getYz_price() {
		return yz_price;
	}

	public void setYz_price(String yzPrice) {
		yz_price = yzPrice;
	}

	public String getRz_price() {
		return rz_price;
	}

	public void setRz_price(String rzPrice) {
		rz_price = rzPrice;
	}

	public String getRz2_price() {
		return rz2_price;
	}

	public void setRz2_price(String rz2Price) {
		rz2_price = rz2Price;
	}

	public String getRz1_price() {
		return rz1_price;
	}

	public void setRz1_price(String rz1Price) {
		rz1_price = rz1Price;
	}

	public String getYws_price() {
		return yws_price;
	}

	public void setYws_price(String ywsPrice) {
		yws_price = ywsPrice;
	}

	public String getYwz_price() {
		return ywz_price;
	}

	public void setYwz_price(String ywzPrice) {
		ywz_price = ywzPrice;
	}

	public String getYwx_price() {
		return ywx_price;
	}

	public void setYwx_price(String ywxPrice) {
		ywx_price = ywxPrice;
	}

	public String getRws_price() {
		return rws_price;
	}

	public void setRws_price(String rwsPrice) {
		rws_price = rwsPrice;
	}

	public String getRwx_price() {
		return rwx_price;
	}

	public void setRwx_price(String rwxPrice) {
		rwx_price = rwxPrice;
	}

	public String getGws_price() {
		return gws_price;
	}

	public void setGws_price(String gwsPrice) {
		gws_price = gwsPrice;
	}

	public String getGwx_price() {
		return gwx_price;
	}

	public void setGwx_price(String gwxPrice) {
		gwx_price = gwxPrice;
	}

	public String getSwz_price() {
		return swz_price;
	}

	public void setSwz_price(String swzPrice) {
		swz_price = swzPrice;
	}

	public String getTdz_price() {
		return tdz_price;
	}

	public void setTdz_price(String tdzPrice) {
		tdz_price = tdzPrice;
	}
	
}
