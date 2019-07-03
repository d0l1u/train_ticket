package com.l9e.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author zhangjun
 *
 */
@XStreamAlias("Data")
public class TrainNewData implements Serializable {

	private static final long serialVersionUID = 1L;
	private String train_no; // 编号

	@XStreamAlias("TrainCode")
	private String station_train_code; // 车次

	private String start_station_telecode; // 始发站电话编号
	private String start_station_name; // 始发站
	private String end_station_telecode; // 终点站电话编号
	private String end_station_name; // 终点站

	private String from_station_telecode; // 出发站电话编号

	@XStreamAlias("StartCity")
	private String from_station_name; // 出发站
	private String to_station_telecode; // 目的站电话编号

	@XStreamAlias("EndCity")
	private String to_station_name; // 目的站

	@XStreamAlias("StartTime")
	private String start_time; // 开车时间

	@XStreamAlias("EndTime")
	private String arrive_time; // 到达时间
	private String day_difference; // 第几天

	@XStreamAlias("Limit")
	private String train_class_name;

	@XStreamAlias("LimitTime")
	private String lishi; // 历时(小时)
	private String canWebBuy;
	private String lishiValue; // 历时(分钟)
	private String yp_info;

	private String num_info;
	private String control_train_day;
	private String start_train_date; // 开车日期
	private String seat_feature;
	private String yp_ex;

	private String train_seat_feature;
	private String seat_types;
	private String location_code;
	private String from_station_no;

	private String to_station_no;
	private String control_day;
	private String sale_time;
	private String is_support_card;
	private String note;

	@XStreamAlias("YZ")
	private String yz;// 硬座票价

	@XStreamAlias("RZ")
	private String rz;// 软座票价

	@XStreamAlias("ZE")
	private String ze;// 二等软座票价

	@XStreamAlias("ZY")
	private String zy;// 一等软座票价

	@XStreamAlias("YWS")
	private String yws;// 硬卧上铺票价

	@XStreamAlias("YWZ")
	private String ywz;// 硬卧中铺票价

	@XStreamAlias("YWX")
	private String ywx;// 硬卧下铺票价

	@XStreamAlias("RWS")
	private String rws;// 软卧上铺票价

	@XStreamAlias("RWX")
	private String rwx;// 软卧下铺票价

	@XStreamAlias("GWS")
	private String gws = "-";// 高级软卧上铺票价

	@XStreamAlias("GWX")
	private String gwx = "-"; // 高级软卧下铺票价

	private String tdz = "-"; // 特等座
	private String swz = "-"; // 商务座
	private String dws = "-"; // 动卧上铺票价
	private String dwx = "-"; // 动卧下铺票价

	private String gg_num;
	private String gr_num; // 高级软卧剩余
	private String qt_num; // 其它
	private String rw_num; // 软卧剩余
	private String rz_num; // 软座剩余
	private String tz_num; // 特等座剩余
	private String wz_num; // 无座剩余
	private String yb_num;
	private String yw_num; // 硬卧剩余
	private String yz_num; // 硬座剩余
	private String ze_num; // 二等座剩余
	private String zy_num; // 一等座剩余
	private String swz_num; // 商务座剩余
	private String gw_num;
	private String tdz_num; // 特等座剩余
	private String dw_num; // 动卧剩余

	private String replaceVal(String str) {
		if (!StringUtils.isEmpty(str)) {
			if ("0".equals(str)) {
				str = "-";
			}
			return str.replace("无", "-").replace("*", "-");
		} else {
			return "-";
		}
	}

	public void initYupiao() {

		this.setGg_num(this.gg_num);
		this.setGr_num(this.gr_num);
		this.setGw_num(this.gw_num);
		this.setTdz_num(this.tdz_num);
		this.setTz_num(this.tz_num);
		this.setSwz_num(this.swz_num);
		this.setYz_num(this.yz_num);
		this.setRz_num(this.rz_num);
		this.setZy_num(this.zy_num);
		this.setZe_num(this.ze_num);
		this.setYw_num(this.yw_num);
		this.setYz_num(this.yz_num);
		this.setRw_num(this.rw_num);
		this.setRw_num(this.rw_num);
		this.setDw_num(this.dw_num);
		this.setQt_num(this.qt_num);
		this.setWz_num(this.wz_num);

	}

	public void initPrice() {
		setGwx(this.gwx);
		setGws(this.gws);
		setTdz(this.tdz);
		setSwz(this.swz);
		setYz(this.yz);
		setRz(this.rz);
		setZy(this.zy);
		setZe(this.ze);
		setYws(this.yws);
		setYwz(this.ywz);
		setYwx(this.ywx);
		setRws(this.rws);
		setRwx(this.rwx);
		setDws(this.dws);
		setDwx(this.dwx);
	}

	public String getYz() {
		return yz;
	}

	public String getRz() {
		return rz;
	}

	public String getZe() {
		return ze;
	}

	public String getZy() {
		return zy;
	}

	public String getYws() {
		return yws;
	}

	public String getYwz() {
		return ywz;
	}

	public String getYwx() {
		return ywx;
	}

	public String getRws() {
		return rws;
	}

	public String getRwx() {
		return rwx;
	}

	public String getGws() {
		return gws;
	}

	public String getGwx() {
		return gwx;
	}

	public String getTdz() {
		return tdz;
	}

	public String getSwz() {
		return swz;
	}

	public void setSwz(String swz) {
		this.swz = replaceVal(swz);
	}

	public void setTdz(String tdz) {
		this.tdz = replaceVal(tdz);
	}

	public void setYz(String yz) {
		this.yz = replaceVal(yz);
	}

	public void setRz(String rz) {
		this.rz = replaceVal(rz);
	}

	public void setZe(String ze) {
		this.ze = replaceVal(ze);
	}

	public void setZy(String zy) {
		this.zy = replaceVal(zy);
	}

	public void setYws(String yws) {
		this.yws = replaceVal(yws);
	}

	public void setYwz(String ywz) {
		this.ywz = replaceVal(ywz);
	}

	public void setYwx(String ywx) {
		this.ywx = replaceVal(ywx);
	}

	public void setRws(String rws) {
		this.rws = replaceVal(rws);
	}

	public void setRwx(String rwx) {
		this.rwx = replaceVal(rwx);
	}

	public void setGws(String gws) {
		this.gws = replaceVal(gws);
	}

	public void setGwx(String gwx) {
		this.gwx = replaceVal(gwx);
	}

	public void setDwx(String dwx) {
		this.dwx = replaceVal(dwx);
	}

	public void setDws(String dws) {
		this.dws = replaceVal(dws);
	}

	public String getDws() {
		return dws;
	}

	public String getDwx() {
		return dwx;
	}

	public void setDw_num(String dw_num) {
		this.dw_num = replaceVal(dw_num);
	}

	public void setWz_num(String wz_num) {
		this.wz_num = replaceVal(wz_num);
	}

	public void setYz_num(String yz_num) {
		this.yz_num = replaceVal(yz_num);
	}

	public void setRz_num(String rz_num) {
		this.rz_num = replaceVal(rz_num);
	}

	public void setZy_num(String zy_num) {
		this.zy_num = replaceVal(zy_num);
	}

	public void setZe_num(String ze_num) {
		this.ze_num = replaceVal(ze_num);
	}

	public void setYw_num(String yw_num) {
		this.yw_num = replaceVal(yw_num);
	}

	public void setRw_num(String rw_num) {
		this.rw_num = replaceVal(rw_num);
	}

	public void setGw_num(String gr_num) {
		this.gw_num = replaceVal(gr_num);
	}

	public void setTdz_num(String tdz_num) {
		this.tdz_num = replaceVal(tdz_num);
	}

	public void setSwz_num(String swz_num) {
		this.swz_num = replaceVal(swz_num);
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}

	public String getStation_train_code() {
		return station_train_code;
	}

	public void setStation_train_code(String stationTrainCode) {
		station_train_code = stationTrainCode;
	}

	public String getStart_station_telecode() {
		return start_station_telecode;
	}

	public void setStart_station_telecode(String startStationTelecode) {
		start_station_telecode = startStationTelecode;
	}

	public String getStart_station_name() {
		return start_station_name;
	}

	public void setStart_station_name(String startStationName) {
		start_station_name = startStationName;
	}

	public String getEnd_station_telecode() {
		return end_station_telecode;
	}

	public void setEnd_station_telecode(String endStationTelecode) {
		end_station_telecode = endStationTelecode;
	}

	public String getEnd_station_name() {
		return end_station_name;
	}

	public void setEnd_station_name(String endStationName) {
		end_station_name = endStationName;
	}

	public String getFrom_station_telecode() {
		return from_station_telecode;
	}

	public void setFrom_station_telecode(String fromStationTelecode) {
		from_station_telecode = fromStationTelecode;
	}

	public String getFrom_station_name() {
		return from_station_name;
	}

	public void setFrom_station_name(String fromStationName) {
		from_station_name = fromStationName;
	}

	public String getTo_station_telecode() {
		return to_station_telecode;
	}

	public void setTo_station_telecode(String toStationTelecode) {
		to_station_telecode = toStationTelecode;
	}

	public String getTo_station_name() {
		return to_station_name;
	}

	public void setTo_station_name(String toStationName) {
		to_station_name = toStationName;
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

	public String getDay_difference() {
		return day_difference;
	}

	public void setDay_difference(String dayDifference) {
		day_difference = dayDifference;
	}

	public String getTrain_class_name() {
		return train_class_name;
	}

	public void setTrain_class_name(String trainClassName) {
		train_class_name = trainClassName;
	}

	public String getLishi() {
		return lishi;
	}

	public void setLishi(String lishi) {
		this.lishi = lishi;
	}

	public String getCanWebBuy() {
		return canWebBuy;
	}

	public void setCanWebBuy(String canWebBuy) {
		this.canWebBuy = canWebBuy;
	}

	public String getLishiValue() {
		return lishiValue;
	}

	public void setLishiValue(String lishiValue) {
		this.lishiValue = lishiValue;
	}

	public String getYp_info() {
		return yp_info;
	}

	public void setYp_info(String ypInfo) {
		yp_info = ypInfo;
	}

	public String getNum_info() {
		return num_info;
	}

	public void setNum_info(String numInfo) {
		num_info = numInfo;
	}

	public String getControl_train_day() {
		return control_train_day;
	}

	public void setControl_train_day(String controlTrainDay) {
		control_train_day = controlTrainDay;
	}

	public String getStart_train_date() {
		return start_train_date;
	}

	public void setStart_train_date(String startTrainDate) {
		start_train_date = startTrainDate;
	}

	public String getSeat_feature() {
		return seat_feature;
	}

	public void setSeat_feature(String seatFeature) {
		seat_feature = seatFeature;
	}

	public String getYp_ex() {
		return yp_ex;
	}

	public void setYp_ex(String ypEx) {
		yp_ex = ypEx;
	}

	public String getTrain_seat_feature() {
		return train_seat_feature;
	}

	public void setTrain_seat_feature(String trainSeatFeature) {
		train_seat_feature = trainSeatFeature;
	}

	public String getSeat_types() {
		return seat_types;
	}

	public void setSeat_types(String seatTypes) {
		seat_types = seatTypes;
	}

	public String getLocation_code() {
		return location_code;
	}

	public void setLocation_code(String locationCode) {
		location_code = locationCode;
	}

	public String getFrom_station_no() {
		return from_station_no;
	}

	public void setFrom_station_no(String fromStationNo) {
		from_station_no = fromStationNo;
	}

	public String getTo_station_no() {
		return to_station_no;
	}

	public void setTo_station_no(String toStationNo) {
		to_station_no = toStationNo;
	}

	public String getControl_day() {
		return control_day;
	}

	public void setControl_day(String controlDay) {
		control_day = controlDay;
	}

	public String getSale_time() {
		return sale_time;
	}

	public void setSale_time(String saleTime) {
		sale_time = saleTime;
	}

	public String getIs_support_card() {
		return is_support_card;
	}

	public void setIs_support_card(String isSupportCard) {
		is_support_card = isSupportCard;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getGg_num() {
		return gg_num;
	}

	public void setGg_num(String ggNum) {
		gg_num = replaceVal(ggNum);
	}

	public String getGr_num() {
		return gr_num;
	}

	public void setGr_num(String grNum) {
		this.gr_num = replaceVal(grNum);
	}

	public String getQt_num() {
		return qt_num;
	}

	public void setQt_num(String qtNum) {
		qt_num = replaceVal(qtNum);
	}

	public String getTz_num() {
		return tz_num;
	}

	public void setTz_num(String tzNum) {
		this.tz_num = replaceVal(tzNum);
	}

	public String getYb_num() {
		return yb_num;
	}

	public void setYb_num(String ybNum) {
		yb_num = replaceVal(ybNum);
	}

	public String getZe_num() {
		return ze_num;
	}

	public String getZy_num() {
		return zy_num;
	}

	public String getRw_num() {
		return rw_num;
	}

	public String getRz_num() {
		return rz_num;
	}

	public String getWz_num() {
		return wz_num;
	}

	public String getYw_num() {
		return yw_num;
	}

	public String getYz_num() {
		return yz_num;
	}

	public String getSwz_num() {
		return swz_num;
	}

	public String getGw_num() {
		return gw_num;
	}

	public String getTdz_num() {
		return tdz_num;
	}

	public String getDw_num() {
		return dw_num;
	}

}