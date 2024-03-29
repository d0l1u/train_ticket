package com.l9e.train.supplier.po;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class Order {

	private Logger logger = Logger.getLogger(this.getClass());

	public static String ORDER_ERROR = "1001";
	public static String ORDER_SUCCESS = "0000";

	public static String STATUS_ORDER_START = "00";// 开始出票
	public static String STATUS_ORDER_INMQ = "05";// 已经插入到消息队列 预订消息队列
	public static String STATUS_ORDER_ING = "11";// 正在预定
	public static String STATUS_ORDER_FAILURE = "22";// 预定失败
	public static String STATUS_ORDER_SUCCESS = "33";// 预定成功
	public static String STATUS_ORDER_MANUAL = "44";// 预定人工
	public static String WAIT_PAY_START = "45";// 等待支付
	public static String WAIT_PAY_MSQ = "46";// 已经插入到消息队列 支付消息队列

	public static String STATUS_PAY_START = "55";// 开始支付
	public static String STATUS_PAY_ING = "66";// 正在支付
	public static String STATUS_PAY_FAILURE = "77";// 支付失败
	public static String STATUS_PAY_SUCCESS = "88";// 支付成功
	public static String STATUS_ORDER_RESEND = "01";// 重发订单
	public static String STATUS_CANCEL_START = "85";// 开始取消
	public static String STATUS_CANCEL_PRE = "84";// 准备取消
	public static String STATUS_CANCEL_ING = "83";// 正在取消
	public static String STATUS_CANCEL_FAIL = "77";// 取消失败
	public static String OUT_TICKET_FAIL = "10";// 出票失败
	public static String STATUS_ORDER_MANUAL_OUTTICKET = "MM";// 人工出票

	public String orderid;
	public String myOrderId;
	public String ordername;
	public String paymoney;
	public String orderstatus;
	public String trainno;
	public String fromcity;
	public String tocity;
	public String fromtime;
	public String totime;
	public String traveltime;
	public String seattype;
	public String outtickettype;
	public String channel;
	public String extseattype;
	public String backurl;
	public String ext;
	public String level;
	public String ispay;
	public String fromCity3c; // 订单中出发城市三字码
	public String toCity3c; // 订单中到达城市三字码

	public String manualorder;// 手工出票
	public String waitfororder;// 12306异常是否等待 11继续等待 00不等待

	public String username;// 12306账号
	public String password;// 12306账号密码
	public Integer accountId; // 账号Id
	public Integer accountFromWay; // 账号来源： 0：公司自有账号 ； 1：12306自带账号

	// 同程支持预选座位号改造
	public String seatDetailType; // 客户预选的卧铺位置
	public String choose_seats; // 客户预选的座位号
	public String chooseSeatType; // 可以选座的坐席类型

	public String getMyOrderId() {
		return this.myOrderId;
	}

	public void setMyOrderId(String myOrderId) {
		this.myOrderId = myOrderId;
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

	public String getFromCity3c() {
		return fromCity3c;
	}

	public void setFromCity3c(String fromCity3c) {
		this.fromCity3c = fromCity3c;
	}

	public String getToCity3c() {
		return toCity3c;
	}

	public void setToCity3c(String toCity3c) {
		this.toCity3c = toCity3c;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getAccountFromWay() {
		return accountFromWay;
	}

	public void setAccountFromWay(Integer accountFromWay) {
		this.accountFromWay = accountFromWay;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWaitfororder() {
		return waitfororder;
	}

	public void setWaitfororder(String waitfororder) {
		this.waitfororder = waitfororder;
	}

	public String getManualorder() {
		return manualorder;
	}

	public void setManualorder(String manualorder) {
		this.manualorder = manualorder;
	}

	public String getIspay() {
		return ispay;
	}

	public void setIspay(String ispay) {
		this.ispay = ispay;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {

		String[] exts = ext.split("#|\\|");

		if (ext.contains("level")) {
			this.level = exts[1];
		}

		this.ext = ext;
	}

	public String getExtseattype() {
		return extseattype;
	}

	public void setExtseattype(String extseattype) {
		this.extseattype = extseattype;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOuttickettype() {
		return outtickettype;
	}

	public void setOuttickettype(String outtickettype) {
		this.outtickettype = outtickettype;
	}

	public String getSeattype() {
		return seattype;
	}

	public void setSeattype(String seattype) {
		this.seattype = seattype;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrdername() {
		return ordername;
	}

	public void setOrdername(String ordername) {
		this.ordername = ordername;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}

	public String getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

	public String getTrainno() {
		return trainno;
	}

	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}

	public String getFromcity() {
		return fromcity;
	}

	public void setFromcity(String fromcity) {
		this.fromcity = fromcity;
	}

	public String getTocity() {
		return tocity;
	}

	public void setTocity(String tocity) {
		this.tocity = tocity;
	}

	public String getFromtime() {
		return fromtime;
	}

	public void setFromtime(String fromtime) {
		this.fromtime = fromtime;
	}

	public String getTotime() {
		return totime;
	}

	public void setTotime(String totime) {
		this.totime = totime;
	}

	public String getTraveltime() {
		return traveltime;
	}

	public void setTraveltime(String traveltime) {
		this.traveltime = traveltime;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}

	public String getSeattrains() {
		return seattrains;
	}

	public void setSeattrains(String seattrains) {
		this.seattrains = seattrains;
	}

	public String seattrains;

	public List<OrderCP> getOrderCPs() {
		// 订单号|用户名|车票类型|证件类型|证件号码|坐席$用户名|车票类型|证件类型|证件号码|坐席$用户名|车票类型|证件类型|证件号码|坐席
		List<OrderCP> list = new ArrayList<OrderCP>();

		String[] str = seattrains.split("#");

		logger.info("length:" + str.length);
		for (int i = 0; i < str.length; i++) {
			OrderCP ordercp = new OrderCP();
			String[] val = str[i].split("\\|");
			System.out.println(val[0]);
			ordercp.setCpId(val[0]);
			ordercp.setUsername(val[1]);
			ordercp.setTrainType(new Integer(val[2]));
			ordercp.setCertType(new Integer(val[3]));
			ordercp.setCertNo(val[4]);
			ordercp.setSeatType(new Integer(val[5]));
			ordercp.setPaymoney(val[6]);

			list.add(ordercp);
		}

		return list;
	}

	/**
	 * 充值下单返回结果
	 * 
	 * @param userbillNo
	 * @param downBillTime
	 * @param phoneNo
	 * @param facevalue
	 * @param billno
	 * @param codeNo
	 * @return
	 */
	public String responstValue(Order supOrder, String codeNo) {

		if (StringUtils.equals(codeNo, ORDER_SUCCESS)) {
			return "success|" + this.orderid;

		} else if (StringUtils.equals(codeNo, ORDER_ERROR)) {
			return "failure|" + this.orderid;
		} else {
			return "exception|" + this.orderid;
		}
	}

}
