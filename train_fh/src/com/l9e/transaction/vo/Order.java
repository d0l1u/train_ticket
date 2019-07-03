package com.l9e.transaction.vo;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Order {

	/**
	 * 同程、美团、去哪
	 */
	public static final String CHANNEL_GROUP_1 = "group-1";// 同程、去哪、美团
	/**
	 * 其他
	 */
	public static final String CHANNEL_GROUP_2 = "group-2";// 其他

	public static String ORDER_ERROR = "1001";
	public static String ORDER_SUCCESS = "0000";

	public static String STATUS_ORDER_START = "00";// 开始出票
	public static String STATUS_ORDER_RESEND = "01";// 重新预定
	public static String STATUS_ORDER_INMQ = "05";// 已经插入到消息队列
	public static String STATUS_ORDER_ING = "11";// 正在预定
	public static String STATUS_ORDER_QUEUE = "15";// 12306排队
	public static String STATUS_ORDER_FAILURE = "22";// 预定失败
	public static String STATUS_ORDER_SUCCESS = "33";// 预定成功
	public static String STATUS_ORDER_MANUAL = "44";// 预定人工

	public static String STATUS_ORDER_WAIT = "05";// 排队订单

	public static String STATUS_PAY_WAIT = "45";// 等待支付
	public static String STATUS_PAY_START = "55";// 开始支付
	public static String STATUS_PAY_ING = "66";// 正在支付
	public static String STATUS_PAY_FAILURE = "77";// 支付失败
	public static String STATUS_PAY_SUCCESS = "88";// 支付成功
	public static String STATUS_PAY_MANUAL = "61";// 支付人工

	public static String STATUS_FIND_MANUAL = "82";// 查询人工
	public static String STATUS_BILL_FAILURE = "10";// 订单失败

	public static String STATUS_CANCEL_PRE = "84";// 准备取消
	public static String STATUS_CANCEL_START = "85";// 开始取消

	public static final String CHANNEL_TC = "tongcheng"; // 同程
	public static final String CHANNEL_QUNAR = "qunar"; // qunar
	public static final String CHANNEL_ELONG = "elong"; // qunar
	public static final String CHANNEL_MEITUAN = "meituan"; // 美团
	public static final String CHANNEL_GT = "301030";// 高铁管家
	public static final String CHANNEL_19E = "19e";// 19e

	@JsonProperty("orerId")
	public String orderId;

	public String orderstr;

	public String orderStatus;

	@JsonProperty("retValue")
	public String retValue;// 返回值

	@JsonProperty("retInfo")
	public String retInfo;// 返回信息

	@JsonProperty("outTicketBillno")
	public String outTicketBillNo;

	public Integer accountId;

	public List<String> orderCps;

	@JsonProperty("cps")
	public List<OrderCP> cps;

	@JsonProperty("summoney")
	public String buymoney;

	public String paymoney;

	@JsonProperty("from")
	public String from;

	@JsonProperty("to")
	public String to;

	@JsonProperty("seattime")
	public String seattime;

	@JsonProperty("trainno")
	public String trainno;

	@JsonProperty("arrivetime")
	public String arrivetime;

	public String seatType;

	public String channel;

	public String channelGroup;

	public String extSeatType;

	public String travel_time;

	public String level;

	public String robotNum;

	public boolean wea_wz;

	public boolean wea_price; // 带儿童不更新票价

	public String losetime;

	public int acc_id;

	public int worker_id;// 预登入 锁定机器人id

	public String fromCity_3c; // 订单中的出发城市三字码

	public String toCity_3c; // 订单中的到达城市三字码

	public String zmhz; // 对应t_zm表中的zmhz字段--火车站点名称

	public String lh; // 对应t_zm表中的lh字段--火车站点名称三字码

	public Integer accountFromWay; // 账号来源： 0：公司自有账号 ； 1：12306自带账号

	// 同程支持预选座位号改造
	public String seatDetailType; // 卧铺位置
	public String choose_seats; // 座位号
	public String chooseSeatType; // 可以选座的坐席类型

	// 途牛排队时间改造
	public Integer queueNumber;// 当前排队人数
	public String waitTime; // 预计排队时间

	@JsonProperty("refundOnline")
	public String refundOnline;

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRefundOnline() {
		return refundOnline;
	}

	public void setRefundOnline(String refundOnline) {
		this.refundOnline = refundOnline;
	}

	public int getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(int workerId) {
		worker_id = workerId;
	}

	public int getAcc_id() {
		return acc_id;
	}

	public void setAcc_id(int accId) {
		acc_id = accId;
	}

	public String getLoseTime() {
		return losetime;
	}

	public void setLoseTime(String loseTime) {
		losetime = loseTime;
	}

	public String getTravel_time() {
		return travel_time;
	}

	public void setTravel_time(String travelTime) {
		travel_time = travelTime;
	}

	public boolean isWea_price() {
		return wea_price;
	}

	public void setWea_price(boolean weaPrice) {
		wea_price = weaPrice;
	}

	public boolean isWea_wz() {
		return wea_wz;
	}

	public void setWea_wz(boolean weaWz) {
		wea_wz = weaWz;
	}

	public String getRobotNum() {
		return robotNum;
	}

	public void setRobotNum(String robotNum) {
		this.robotNum = robotNum;
	}

	@JsonProperty("contactsnum")
	public String contactsnum;// 常用联系人数据

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelGroup() {
		return channelGroup;
	}

	public void setChannelGroup(String channelGroup) {
		this.channelGroup = channelGroup;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getBuymoney() {
		return buymoney;
	}

	public void setBuymoney(String buymoney) {
		this.buymoney = buymoney;
	}

	public String getContactsnum() {
		return contactsnum;
	}

	public void setContactsnum(String contactsnum) {
		this.contactsnum = contactsnum;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSeattime() {
		return seattime;
	}

	public void setSeattime(String seattime) {
		this.seattime = seattime;
	}

	public String getArrivetime() {
		return arrivetime;
	}

	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}

	public String getTrainno() {
		return trainno;
	}

	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}

	public String getRetInfo() {
		return retInfo;
	}

	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}

	public List<OrderCP> getCps() {
		return cps;
	}

	public void setCps(List<OrderCP> cps) {
		this.cps = cps;
	}

	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public String getOutTicketBillNo() {
		return outTicketBillNo;
	}

	public void setOutTicketBillNo(String outTicketBillNo) {
		this.outTicketBillNo = outTicketBillNo;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderstr() {
		return orderstr;
	}

	public void setOrderstr(String orderstr) {
		this.orderstr = orderstr;
	}

	public List<String> getOrderCps() {
		return orderCps;
	}

	public void addOrderCp(String str) {
		if (orderCps == null) {
			orderCps = new ArrayList<String>();
		}

		orderCps.add(str);
	}

	public String getExtSeatType() {
		return extSeatType;
	}

	public void setExtSeatType(String extSeatType) {
		this.extSeatType = extSeatType;
	}

	public String getFromCity_3c() {
		return fromCity_3c;
	}

	public void setFromCity_3c(String fromCity_3c) {
		this.fromCity_3c = fromCity_3c;
	}

	public String getToCity_3c() {
		return toCity_3c;
	}

	public void setToCity_3c(String toCity_3c) {
		this.toCity_3c = toCity_3c;
	}

	public String getZmhz() {
		return zmhz;
	}

	public void setZmhz(String zmhz) {
		this.zmhz = zmhz;
	}

	public String getLh() {
		return lh;
	}

	public void setLh(String lh) {
		this.lh = lh;
	}

	public Integer getAccountFromWay() {
		return accountFromWay;
	}

	public void setAccountFromWay(Integer accountFromWay) {
		this.accountFromWay = accountFromWay;
	}

	public String getSeatDetailType() {
		return seatDetailType;
	}

	public void setSeatDetailType(String seatDetailType) {
		this.seatDetailType = seatDetailType;
	}

	public String getChoose_seats() {
		return choose_seats;
	}

	public void setChoose_seats(String chooseSeats) {
		choose_seats = chooseSeats;
	}

	public String getChooseSeatType() {
		return chooseSeatType;
	}

	public void setChooseSeatType(String chooseSeatType) {
		this.chooseSeatType = chooseSeatType;
	}

	public Integer getQueueNumber() {
		return queueNumber;
	}

	public void setQueueNumber(Integer queueNumber) {
		this.queueNumber = queueNumber;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

}
