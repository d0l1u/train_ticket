package com.l9e.vo;

public class TcTicketPriceVo {
	private String sale_date_time;	//车票开售时间
	private String can_buy_now	;	//当前是否可以接受预定（Y:可以，N:不可以）
	private String arrive_days;	//	列车从出发站到达目的站的运行天数	0：当日到达，1：
	//次日到达，2：三日到达，3：四日到达，依此类推
	private String train_start_date;	//	列车从始发站出发的日期
	private String train_code	;	//车次
	private String access_byidcard	;	//是否可凭二代身份证直接进出站
	private String train_no	;	//列车号
	private String train_type	;	//列车类型，附件5.3
	private String from_station_name;	//	出发车站名
	private String from_station_code;	//	出发车站简码
	private String to_station_name	;	//到达车站名
	private String to_station_code;	//	到达车站简码
	private String start_station_name;	//	列车始发站名
	private String end_station_name;	//	列车终到站名
	private String start_time;	//	出发时刻
	private String arrive_time;	//	到达时刻
	private String run_time;	//	历时（从出发站到目的站的列车运行时间）
	private String run_time_minute;	//	历时分钟合计
	 

	private String gjrw_num	;	//高级软卧余票数量
	private double gjrw_price;	//	高级软卧票价
	private String qtxb_num;	//	其他席别余票数量
	private double qtxb_price;	//	其他席别对应的票价
	private String rws_num;	//	软卧(上)余票数量
	private double rws_price;	//	软卧(上)票价
	private String rw_num;	//	软卧(下)余票数量
	private double rw_price	;	//软卧(下)票价
	private String rz_num;	//	软座的余票数量
	private double rz_price;	//	软座的票价
	private String swz_num;	//	商务座的余票数据
	private double swz_price;	//	商务座票价
	private String tdz_num;	//	特等座的余票数量
	private double tdz_price;	//	特等座票价
	private String wz_num;	//	无座的余票数量
	private double wz_price	;	//无座票价
	private String yws_num;	//	硬卧(上)余票数量
	private double yws_price;	//	硬卧(上)票价
	private String yw_num;	//	硬卧(中)的余票数量
	private double yw_price;	//	硬卧(中)票价
	private String ywx_num;	//	硬卧(下)余票数量
	private double ywx_price;	//	硬卧(下)票价
	private String yz_num;	//	硬座的余票数量
	private double yz_price;	//	硬座票价
	private String edz_num;	//	二等座的余票数量
	private double edz_price;	//	二等座票价
	private String ydz_num;	//	一等座的余票数量
	private double ydz_price;	//	一等座票价
	private String note;	//	备注（起售时间）
	
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

	public double getGjrw_price() {
		return gjrw_price;
	}

	public void setGjrw_price(double gjrwPrice) {
		gjrw_price = gjrwPrice;
	}

	public String getQtxb_num() {
		return qtxb_num;
	}

	public void setQtxb_num(String qtxbNum) {
		qtxb_num = qtxbNum;
	}

	public double getQtxb_price() {
		return qtxb_price;
	}

	public void setQtxb_price(double qtxbPrice) {
		qtxb_price = qtxbPrice;
	}

	public String getRws_num() {
		return rws_num;
	}

	public void setRws_num(String rwsNum) {
		rws_num = rwsNum;
	}

	public double getRws_price() {
		return rws_price;
	}

	public void setRws_price(double rwsPrice) {
		rws_price = rwsPrice;
	}

	public String getRw_num() {
		return rw_num;
	}

	public void setRw_num(String rwNum) {
		rw_num = rwNum;
	}

	public double getRw_price() {
		return rw_price;
	}

	public void setRw_price(double rwPrice) {
		rw_price = rwPrice;
	}

	public String getRz_num() {
		return rz_num;
	}

	public void setRz_num(String rzNum) {
		rz_num = rzNum;
	}

	public double getRz_price() {
		return rz_price;
	}

	public void setRz_price(double rzPrice) {
		rz_price = rzPrice;
	}

	public String getSwz_num() {
		return swz_num;
	}

	public void setSwz_num(String swzNum) {
		swz_num = swzNum;
	}

	public double getSwz_price() {
		return swz_price;
	}

	public void setSwz_price(double swzPrice) {
		swz_price = swzPrice;
	}

	public String getTdz_num() {
		return tdz_num;
	}

	public void setTdz_num(String tdzNum) {
		tdz_num = tdzNum;
	}

	public double getTdz_price() {
		return tdz_price;
	}

	public void setTdz_price(double tdzPrice) {
		tdz_price = tdzPrice;
	}

	public String getWz_num() {
		return wz_num;
	}

	public void setWz_num(String wzNum) {
		wz_num = wzNum;
	}

	public double getWz_price() {
		return wz_price;
	}

	public void setWz_price(double wzPrice) {
		wz_price = wzPrice;
	}

	public String getYws_num() {
		return yws_num;
	}

	public void setYws_num(String ywsNum) {
		yws_num = ywsNum;
	}

	public double getYws_price() {
		return yws_price;
	}

	public void setYws_price(double ywsPrice) {
		yws_price = ywsPrice;
	}

	public String getYw_num() {
		return yw_num;
	}

	public void setYw_num(String ywNum) {
		yw_num = ywNum;
	}

	public double getYw_price() {
		return yw_price;
	}

	public void setYw_price(double ywPrice) {
		yw_price = ywPrice;
	}

	public String getYwx_num() {
		return ywx_num;
	}

	public void setYwx_num(String ywxNum) {
		ywx_num = ywxNum;
	}

	public double getYwx_price() {
		return ywx_price;
	}

	public void setYwx_price(double ywxPrice) {
		ywx_price = ywxPrice;
	}

	public String getYz_num() {
		return yz_num;
	}

	public void setYz_num(String yzNum) {
		yz_num = yzNum;
	}

	public double getYz_price() {
		return yz_price;
	}

	public void setYz_price(double yzPrice) {
		yz_price = yzPrice;
	}

	public String getEdz_num() {
		return edz_num;
	}

	public void setEdz_num(String edzNum) {
		edz_num = edzNum;
	}

	public double getEdz_price() {
		return edz_price;
	}

	public void setEdz_price(double edzPrice) {
		edz_price = edzPrice;
	}

	public String getYdz_num() {
		return ydz_num;
	}

	public void setYdz_num(String ydzNum) {
		ydz_num = ydzNum;
	}

	public double getYdz_price() {
		return ydz_price;
	}

	public void setYdz_price(double ydzPrice) {
		ydz_price = ydzPrice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void tcInitPrice() {
		this.gjrw_price=0;//	高级软卧票价
		this.qtxb_price	=0;//其他席别对应的票价
		this.rws_price=0;//	软卧(上)票价
		this.rw_price=0;//	软卧(下)票价
		this.rz_price=0;//	软座的票价
		this.swz_price=0;//	商务座票价
		this.tdz_price=0;//	特等座票价
		this.wz_price=0;//	无座票价
		this.yws_price=0;//	硬卧(上)票价
		this.yw_price=0;//	硬卧(中)票价
		this.ywx_price=0;//	硬卧(下)票价
		this.yz_price=0;//	硬座票价
		this.edz_price=0;//	二等座票价
		this.ydz_price=0;//	一等座票价
	}

	
	
}
