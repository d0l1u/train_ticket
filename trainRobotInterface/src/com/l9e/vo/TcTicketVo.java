package com.l9e.vo;

public class TcTicketVo {
	private String sale_date_time;//	车票开售时间
	private String can_buy_now	;//	当前是否可以接受预定（Y:可以，N:不可以）
	private String arrive_days;//		列车从出发站到达目的站的运行天数	0：当日到
	//达，1：次日到达，2：三日到达，3：四日到达，依此 类推
	private String train_start_date	;//	列车从始发站出发的日期
	private String train_code	;//	车次
	private String access_byidcard;//		是否可凭二代身份证直接进出站
	private String train_no	;//	列车号
	private String train_type;//		列车类型，附件5.3
	private String from_station_name;//		出发车站名
	private String from_station_code;//		出发车站简码
	private String to_station_name	;//	到达车站名
	private String to_station_code;//		到达车站简码
	private String start_station_name;//		列车始发站名
	private String end_station_name	;//	列车终到站名
	private String start_time;//		出发时刻
	private String arrive_time;//		到达时刻
	private String run_time;//		历时（从出发站到目的站的列车运行时间）
	private String run_time_minute;//		历时分钟合计
	private String gjrw_num;//		高级软卧余票数量
	private String qtxb_num;//		其他席别余票数量
	private String rw_num;//		软卧余票数量
	 

	private String rz_num;//		软座的余票数量
	private String swz_num;//		商务座的余票数据
	private String tdz_num;//		特等座的余票数量
	private String wz_num;//		无座的余票数量
	private String yw_num;//		硬卧的余票数量
	private String yz_num;//		;硬座的余票数量
	private String edz_num;	//二等座的余票数量
	private String ydz_num	;//一等座的余票数量
	private String note	;//备注（起售时间）
	public String getSale_date_time() {
		return sale_date_time;
	}
	public void setSale_date_time(String saleDateTime) {
		sale_date_time = saleDateTime;
	}
	public String getCan_buy_now() {
		return can_buy_now;
	}
	public void setCan_buy_now(String canBuyNow) {
		can_buy_now = canBuyNow;
	}
	public String getArrive_days() {
		return arrive_days;
	}
	public void setArrive_days(String arriveDays) {
		arrive_days = arriveDays;
	}
	public String getTrain_start_date() {
		return train_start_date;
	}
	public void setTrain_start_date(String trainStartDate) {
		train_start_date = trainStartDate;
	}
	public String getTrain_code() {
		return train_code;
	}
	public void setTrain_code(String trainCode) {
		train_code = trainCode;
	}
	public String getAccess_byidcard() {
		return access_byidcard;
	}
	public void setAccess_byidcard(String accessByidcard) {
		access_byidcard = accessByidcard;
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
	public String getFrom_station_name() {
		return from_station_name;
	}
	public void setFrom_station_name(String fromStationName) {
		from_station_name = fromStationName;
	}
	public String getFrom_station_code() {
		return from_station_code;
	}
	public void setFrom_station_code(String fromStationCode) {
		from_station_code = fromStationCode;
	}
	public String getTo_station_name() {
		return to_station_name;
	}
	public void setTo_station_name(String toStationName) {
		to_station_name = toStationName;
	}
	public String getTo_station_code() {
		return to_station_code;
	}
	public void setTo_station_code(String toStationCode) {
		to_station_code = toStationCode;
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
	public String getRun_time() {
		return run_time;
	}
	public void setRun_time(String runTime) {
		run_time = runTime;
	}
	public String getRun_time_minute() {
		return run_time_minute;
	}
	public void setRun_time_minute(String runTimeMinute) {
		run_time_minute = runTimeMinute;
	}
	public String getGjrw_num() {
		return gjrw_num;
	}
	public void setGjrw_num(String gjrwNum) {
		gjrw_num = gjrwNum;
	}
	public String getQtxb_num() {
		return qtxb_num;
	}
	public void setQtxb_num(String qtxbNum) {
		qtxb_num = qtxbNum;
	}
	public String getRw_num() {
		return rw_num;
	}
	public void setRw_num(String rwNum) {
		rw_num = rwNum;
	}
	public String getRz_num() {
		return rz_num;
	}
	public void setRz_num(String rzNum) {
		rz_num = rzNum;
	}
	public String getSwz_num() {
		return swz_num;
	}
	public void setSwz_num(String swzNum) {
		swz_num = swzNum;
	}
	public String getTdz_num() {
		return tdz_num;
	}
	public void setTdz_num(String tdzNum) {
		tdz_num = tdzNum;
	}
	public String getWz_num() {
		return wz_num;
	}
	public void setWz_num(String wzNum) {
		wz_num = wzNum;
	}
	public String getYw_num() {
		return yw_num;
	}
	public void setYw_num(String ywNum) {
		yw_num = ywNum;
	}
	public String getYz_num() {
		return yz_num;
	}
	public void setYz_num(String yzNum) {
		yz_num = yzNum;
	}
	public String getEdz_num() {
		return edz_num;
	}
	public void setEdz_num(String edzNum) {
		edz_num = edzNum;
	}
	public String getYdz_num() {
		return ydz_num;
	}
	public void setYdz_num(String ydzNum) {
		ydz_num = ydzNum;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
