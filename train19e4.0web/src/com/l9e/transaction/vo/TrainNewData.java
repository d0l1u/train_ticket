package com.l9e.transaction.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.l9e.util.MemcachedUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * DGTrain 票价余票信息
 * @author zhangjun
 *
 */
@XStreamAlias("Data")
public class TrainNewData implements Serializable {

	private static final long serialVersionUID = 1L;
	private String train_no;				//编号
	private String id;
	
	@XStreamAlias("TrainCode")
	private String station_train_code;		//车次	

	private String	start_station_telecode;	//始发站电话编号
	private String	start_station_name;		//始发站
	private String	end_station_telecode;	//终点站电话编号
	private String	end_station_name;		//终点站
	
	private String	from_station_telecode;	//出发站电话编号
	
	@XStreamAlias("StartCity")
	private String	from_station_name;		//出发站
	private String	to_station_telecode;	//目的站电话编号
	
	@XStreamAlias("EndCity")
	private String	to_station_name;		//目的站
	
	@XStreamAlias("StartTime")
	private String	start_time;				//开车时间
	
	@XStreamAlias("EndTime")
	private String	arrive_time;			//到达时间
	private String	day_difference;			//第几天
	
	@XStreamAlias("Limit")
	private String	train_class_name;
	
	@XStreamAlias("LimitTime")
	private String	lishi;					//历时(小时)
	private String	canWebBuy;
	private String	lishiValue;				//历时(分钟)
	private String 	yp_info;
	
	private String	num_info;
	private String	control_train_day;
	private String	start_train_date;		//开车日期
	private String	seat_feature;
	private String	yp_ex;
	
	private String	train_seat_feature;
	private String	seat_types;
	private String	location_code;
	private String	from_station_no;
	
	private String	to_station_no;
	private String	control_day;
	private String	sale_time;
	private String	is_support_card;
	private String	note;
	
	@XStreamAlias("WZ")
	private String wz;//无座票价
	
	@XStreamAlias("YZ")
	private String yz;//硬座票价
	
	@XStreamAlias("RZ")
	private String rz;//软座票价
	
	@XStreamAlias("ZE")
	private String ze;//二等软座票价 
	
	@XStreamAlias("ZY")
	private String zy;//一等软座票价
	
	@XStreamAlias("YWS")
	private String yws;//硬卧上铺票价
	
	@XStreamAlias("YWZ")
	private String ywz;//硬卧中铺票价
	
	@XStreamAlias("YWX")
	private String ywx;//硬卧下铺票价
	
	@XStreamAlias("RWS")
	private String rws;//软卧上铺票价
	
	@XStreamAlias("RWX")
	private String rwx;//软卧下铺票价
	
	@XStreamAlias("GWS")
	private String gws ="-";//高级软卧上铺票价 
	
	@XStreamAlias("GWX")
	private String gwx ="-";						//高级软卧下铺票价
	
	private String tdz="-";					//特等座
	private String swz="-";					//商务座
	private String dws="-";					//动卧上铺票价
	private String dwx="-";					//动卧下铺票价
	
	private String gg_num;
	private String gr_num;					//高级软卧剩余
	private String gw_num;					//高级软卧剩余 ~~必须留着
	private String qt_num;					//其它
	private String rw_num;					//软卧剩余
	private String rz_num;					//软座剩余
	private String tz_num;					//特等座剩余
	private String wz_num;					//无座剩余
	private String yb_num;
	private String yw_num;					//硬卧剩余
	private String yz_num;					//硬座剩余
	private String ze_num;					//二等座剩余
	private String zy_num;					//一等座剩余
	private String swz_num;					//商务座剩余
	private String dw_num;					//动卧剩余
	
	//页面展示余票 有/无
	private String wz_num_show="0";
	private String yz_num_show="0";
	private String rz_num_show="0";
	private String zy_num_show="0";	
	private String ze_num_show="0";	
	private String yw_num_show="0";
	private String rw_num_show="0";
	private String gr_num_show="0";
	private String gw_num_show="0";
	private String tz_num_show="0";
	private String swz_num_show="0";
	private String dw_num_show="0";
	
	private String canBook="0";//是否可以预定 1/0 能/否
	

	public String getArrive_time() {
		return arrive_time;
	}
	@JsonIgnore
	public String getCanBook() {
		return canBook;
	}
	
	public String getCanWebBuy() {
		return canWebBuy;
	}
	
	public String getControl_day() {
		return control_day;
	}
	public String getControl_train_day() {
		return control_train_day;
	}
	public String getDay_difference() {
		return day_difference;
	}
	public String getDw_num() {
		return dw_num;
	}
	@JsonIgnore
	public String getDw_num_show() {
		return dw_num_show;
	}
	public String getDws() {
		return dws;
	}

	public String getDwx() {
		return dwx;
	}

	public String getEnd_station_name() {
		return end_station_name;
	}

	public String getEnd_station_telecode() {
		return end_station_telecode;
	}

	public String getFrom_station_name() {
		return from_station_name;
	}

	public String getFrom_station_no() {
		return from_station_no;
	}

	public String getFrom_station_telecode() {
		return from_station_telecode;
	}

	public String getGg_num() {
		return gg_num;
	}

	public String getGr_num() {
		return gr_num;
	}

	@JsonIgnore
	public String getGr_num_show() {
		return gr_num_show;
	}

	public String getGw_num() {
		return gw_num;
	}

	public String getGw_num_show() {
		return gw_num_show;
	}

	
	public String getGws() {
		return gws;
	}

	public String getGwx() {
		return gwx;
	}

	public String getId() {
		return id;
	}

	public String getIs_support_card() {
		return is_support_card;
	}

	public String getLishi() {
		return lishi;
	}

	public String getLishiValue() {
		return lishiValue;
	}

	public String getLocation_code() {
		return location_code;
	}

	public String getNote() {
		return note;
	}
	public String getNum_info() {
		return num_info;
	}
	public String getQt_num() {
		return qt_num;
	}
	public String getRw_num() {
		return rw_num;
	}
	@JsonIgnore
	public String getRw_num_show() {
		return rw_num_show;
	}
	public String getRws() {
		return rws;
	}

	public String getRwx() {
		return rwx;
	}

	public String getRz() {
		return rz;
	}

	public String getRz_num() {
		return rz_num;
	}

	@JsonIgnore
	public String getRz_num_show() {
		return rz_num_show;
	}

	@JsonIgnore
	public String getRz1_num_show() {
		return zy_num_show;
	}

	@JsonIgnore
	public String getRz2_num_show() {
		return ze_num_show;
	}

	public String getSale_time() {
		return sale_time;
	}

	public String getSeat_feature() {
		return seat_feature;
	}
	public String getSeat_types() {
		return seat_types;
	}
	public String getStart_station_name() {
		return start_station_name;
	}
	public String getStart_station_telecode() {
		return start_station_telecode;
	}

	public String getStart_time() {
		return start_time;
	}

	public String getStart_train_date() {
		return start_train_date;
	}

	public String getStation_train_code() {
		return station_train_code;
	}

	public String getSwz() {
		return swz;
	}

	public String getSwz_num() {
		return swz_num;
	}

	@JsonIgnore
	public String getSwz_num_show() {
		return swz_num_show;
	}

	public String getTdz() {
		return tdz;
	}

	@JsonIgnore
	public String getTdz_num_show() {
		return tz_num_show;
	}

	public String getTo_station_name() {
		return to_station_name;
	}

	public String getTo_station_no() {
		return to_station_no;
	}

	public String getTo_station_telecode() {
		return to_station_telecode;
	}

	public String getTrain_class_name() {
		return train_class_name;
	}

	public String getTrain_no() {
		return train_no;
	}	
	public String getTrain_seat_feature() {
		return train_seat_feature;
	}
	public String getTz_num() {
		return tz_num;
	}
	public String getTz_num_show() {
		return tz_num_show;
	}
	public String getWz() {
		return wz;
	}
	public String getWz_num() {
		return wz_num;
	}
	@JsonIgnore
	public String getWz_num_show() {
		return wz_num_show;
	}
	public String getYb_num() {
		return yb_num;
	}
	public String getYp_ex() {
		return yp_ex;
	}
	public String getYp_info() {
		return yp_info;
	}
	public String getYw_num() {
		return yw_num;
	}

	@JsonIgnore
	public String getYw_num_show() {
		return yw_num_show;
	}

	public String getYws() {
		return yws;
	}

	public String getYwx() {
		return ywx;
	}

	public String getYwz() {
		return ywz;
	}

	public String getYz() {
		return yz;
	}

	public String getYz_num() {
		return yz_num;
	}

	@JsonIgnore
	public String getYz_num_show() {
		return yz_num_show;
	}

	public String getZe() {
		return ze;
	}

	public String getZe_num() {
		return ze_num;
	}

	public String getZe_num_show() {
		return ze_num_show;
	}

	public String getZy() {
		return zy;
	}

	public String getZy_num() {
		return zy_num;
	}

	public String getZy_num_show() {
		return zy_num_show;
	}

	private String replacenumShow(String str){
//		System.out.println(this.station_train_code + "---:"+  str);
		if(StringUtils.isEmpty(str)){
			return "0";
		}else if("-".equals(str)){
			return "0";
		}else if(Integer.parseInt(str)<=MemcachedUtil.spareTicket){
			return "0";
		}else{
			this.canBook = "1";
			//return "有";
			return (Integer.parseInt(str)-MemcachedUtil.spareTicket)+"";
		}
	}

	private String replaceNumVal(String str){
		if(!StringUtils.isEmpty(str)){
			str = str.replace("无", "-").replace("*", "-");
			if(!"-".equals(str) && Integer.parseInt(str)<=MemcachedUtil.spareTicket){
				str = "-";
			}
		}else{
			str = "-";
		}
		return str;
	}

	private String replaceVal(String str){
		if(!StringUtils.isEmpty(str)){
			return str.replace("无", "-").replace("*", "-");
		}else{
			return "-";
		}
	}

	public void setArrive_time(String arriveTime) {
		arrive_time = arriveTime;
	}

	public void setCanBook(String canBook) {
		this.canBook = canBook;
	}

	public void setCanWebBuy(String canWebBuy) {
		this.canWebBuy = canWebBuy;
	}

	public void setControl_day(String controlDay) {
		control_day = controlDay;
	}

	public void setControl_train_day(String controlTrainDay) {
		control_train_day = controlTrainDay;
	}

	public void setDay_difference(String dayDifference) {
		day_difference = dayDifference;
	}

	public void setDw_num(String dw_num) {
		this.dw_num = replaceNumVal(dw_num);
		this.dw_num_show = replacenumShow(this.dw_num);
	}

	public void setDw_num_show(String dw_num_show) {
		this.dw_num_show = dw_num_show;
	}

	public void setDws(String dws) {
		this.dws = replaceVal(dws);
	}

	public void setDwx(String dwx) {
		this.dwx = replaceVal(dwx);
	}

	public void setEnd_station_name(String endStationName) {
		end_station_name = endStationName;
	}

	public void setEnd_station_telecode(String endStationTelecode) {
		end_station_telecode = endStationTelecode;
	}

	public void setFrom_station_name(String fromStationName) {
		from_station_name = fromStationName;
	}

	public void setFrom_station_no(String fromStationNo) {
		from_station_no = fromStationNo;
	}

	public void setFrom_station_telecode(String fromStationTelecode) {
		from_station_telecode = fromStationTelecode;
	}

	public void setGg_num(String ggNum) {
		gg_num = ggNum;
	}

	public void setGr_num(String gr_num) {
		this.gr_num = replaceNumVal(gr_num);
		this.gr_num_show = replacenumShow(this.gr_num);
	}

	public void setGr_num_show(String gr_num_show) {
		this.gr_num_show = gr_num_show;
	}

	public void setGw_num(String gw_num) {
		this.gw_num = replaceNumVal(gw_num);
		this.gw_num_show = replacenumShow(this.gw_num);
	}

	public void setGw_num_show(String gw_num_show) {
		this.gw_num_show = gw_num_show;
	}

	public void setGws(String gws) {
		this.gws = replaceVal(gws);
	}

	public void setGwx(String gwx) {
		this.gwx = replaceVal(gwx);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIs_support_card(String isSupportCard) {
		is_support_card = isSupportCard;
	}

	public void setLishi(String lishi) {
		this.lishi = lishi;
	}

	public void setLishiValue(String lishiValue) {
		this.lishiValue = lishiValue;
	}

	public void setLocation_code(String locationCode) {
		location_code = locationCode;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setNum_info(String numInfo) {
		num_info = numInfo;
	}

	public void setQt_num(String qtNum) {
		qt_num = qtNum;
	}

	public void setRw_num(String rw_num) {
		this.rw_num = replaceNumVal(rw_num);
		this.rw_num_show = replacenumShow(this.rw_num);
	}

	public void setRw_num_show(String rw_num_show) {
		this.rw_num_show = rw_num_show;
	}

	public void setRws(String rws) {
		this.rws = replaceVal(rws);
	}

	public void setRwx(String rwx) {
		this.rwx = replaceVal(rwx);
	}

	public void setRz(String rz) {
		this.rz = replaceVal(rz);
	}

	public void setRz_num(String rz_num) {
		this.rz_num = replaceNumVal(rz_num);
		this.rz_num_show = replacenumShow(this.rz_num);
	}

	public void setRz_num_show(String rz_num_show) {
		this.rz_num_show = rz_num_show;
	}

	public void setSale_time(String saleTime) {
		sale_time = saleTime;
	}

	public void setSeat_feature(String seatFeature) {
		seat_feature = seatFeature;
	}

	public void setSeat_types(String seatTypes) {
		seat_types = seatTypes;
	}

	public void setStart_station_name(String startStationName) {
		start_station_name = startStationName;
	}

	public void setStart_station_telecode(String startStationTelecode) {
		start_station_telecode = startStationTelecode;
	}

	public void setStart_time(String startTime) {
		start_time = startTime;
	}

	public void setStart_train_date(String startTrainDate) {
		start_train_date = startTrainDate;
	}

	public void setStation_train_code(String stationTrainCode) {
		station_train_code = stationTrainCode;
	}

	public void setSwz(String swz) {
		this.swz = replaceVal(swz);
	}

	public void setSwz_num(String swz_num) {
		this.swz_num = replaceNumVal(swz_num);
		this.swz_num_show = replacenumShow(this.swz_num);
	}

	public void setSwz_num_show(String swz_num_show) {
		this.swz_num_show = swz_num_show;
	}

	public void setTdz(String tdz) {
		this.tdz = replaceVal(tdz);
	}

	public void setTdz_num(String tdz_num) {
		this.tz_num = replaceNumVal(tdz_num);
		this.tz_num_show = replacenumShow(this.tz_num);
	}

	public void setTo_station_name(String toStationName) {
		to_station_name = toStationName;
	}

	public void setTo_station_no(String toStationNo) {
		to_station_no = toStationNo;
	}

	public void setTo_station_telecode(String toStationTelecode) {
		to_station_telecode = toStationTelecode;
	}

	public void setTrain_class_name(String trainClassName) {
		train_class_name = trainClassName;
	}

	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}

	public void setTrain_seat_feature(String trainSeatFeature) {
		train_seat_feature = trainSeatFeature;
	}

	public void setTz_num(String tzNum) {
		tz_num = tzNum;
	}

	public void setTz_num_show(String tz_num_show) {
		this.tz_num_show = tz_num_show;
	}

	public void setWz(String wz) {
		this.wz = wz;
	}

	public void setWz_num(String wz_num) {
		this.wz_num = replaceNumVal(wz_num);
		this.wz_num_show = replacenumShow(this.wz_num);
	}

	public void setWz_num_show(String wz_num_show) {
		this.wz_num_show = wz_num_show;
	}

	public void setYb_num(String ybNum) {
		yb_num = ybNum;
	}


	public void setYp_ex(String ypEx) {
		yp_ex = ypEx;
	}

	public void setYp_info(String ypInfo) {
		yp_info = ypInfo;
	}

	public void setYw_num(String yw_num) {
		this.yw_num = replaceNumVal(yw_num);
		this.yw_num_show = replacenumShow(this.yw_num);
	}

	public void setYw_num_show(String yw_num_show) {
		this.yw_num_show = yw_num_show;
	}

	public void setYws(String yws) {
		this.yws = replaceVal(yws);
	}

	public void setYwx(String ywx) {
		this.ywx = replaceVal(ywx);
	}

	public void setYwz(String ywz) {
		this.ywz = replaceVal(ywz);
	}

	public void setYz(String yz) {
		this.yz = replaceVal(yz);
	}

	public void setYz_num(String yz_num) {
		this.yz_num = replaceNumVal(yz_num);
		this.yz_num_show = replacenumShow(this.yz_num);
	}

	public void setYz_num_show(String yz_num_show) {
		this.yz_num_show = yz_num_show;
	}

	public void setZe(String ze) {
		this.ze = replaceVal(ze);
	}

	public void setZe_num(String ze_num) {
		this.ze_num = replaceNumVal(ze_num);
		this.ze_num_show = replacenumShow(this.ze_num);
	}

	public void setZe_num_show(String ze_num_show) {
		this.ze_num_show = ze_num_show;
	}

	public void setZy(String zy) {
		this.zy = replaceVal(zy);
	}
	public void setZy_num(String zy_num) {
		this.zy_num = replaceNumVal(zy_num);
		this.zy_num_show = replacenumShow(this.zy_num);
	}
	public void setZy_num_show(String zy_num_show) {
		this.zy_num_show = zy_num_show;
	}

}
