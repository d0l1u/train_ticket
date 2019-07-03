package com.l9e.train.supplier.po;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 抢票订单实体类
 * @author wangsf01
 *
 */
public class JlOrder {




private Logger logger=Logger.getLogger(this.getClass());   
	
	
	public static String ORDER_ERROR = "1001"; 
	public static String ORDER_SUCCESS = "0000"; 
	
	public static String STATUS_ORDER_START = "00";//开始出票
	public static String STATUS_ORDER_RESEND = "01";//重发出票
	public static String OUT_TICKET_FAIL= "10";//出票失败
	public static String STATUS_ORDER_ING = "11";//正在预定
	public static String STATUS_RESERVE_SEAT_FAILURE= "22";//扣位失败
	public static String STATUS_VIEFOR_TICKET_SUCCESS= "33";//请求抢票成功(预定成功)
	public static String STATUS_ORDER_MANUAL= "44";//预定人工
	public static String STATUS_PAY_START= "55";//扣位成功（开始支付）
	public static String STATUS_PAY_RESEND= "56";//重新支付
	public static String STATUS_PAY_MANUAL= "61";//人工支付	
	public static String STATUS_PAY_ING= "66";//正在支付
	public static String STATUS_PAY_FAILURE= "77";//支付失败
	public static String STATUS_PAY_SUCCESS= "88";//支付成功
	
	public static String STATUS_CANCEL_ING= "83";//正在取消
	public static String STATUS_CANCEL_START= "85";//开始取消
	public static String STATUS_CANCEL_MANUAL = "86";//取消人工
	public static String STATUS_CANCEL_RESEND = "84";//取消重发
	public static String STATUS_OUT_TICKET_SUCCESS = "99";//出票成功
	
	/**退款成功*/
	public static String STATUS_REFUND_SUCCESS = "71";
	/**退款人工*/
	public static String STATUS_REFUND_MENUAL = "72";
	
	
	
	public String orderId;//抢票订单ID
	public String fromToZh;//订单中出发到达站中文
	public String payMoney;//客户支付金额
	public String buyMoney;//官网票面总额
	public String orderStatus;//出票状态
	public String createTime;//创建时间
	public String outTicketTime;//出票时间
	public String trainNo;//车次
	public String trainNoAccept;//备选车次(k179|k180..)
	public String fromCity;//出发城市
	public String toCity;//到达城市
	public String fromTime;//车次出发时间
	public String toTime;//车次到达时间
	public String travelTime;//乘车日期
	public String seatType;//座席类型
	public String seatTypeAccept;//备选座席类型（硬卧|硬座..)
	public String outTicketType;//出票方式
	public String channel;//渠道
	public String level;//订单级别
	public String isPay;//支付状态
	public String fromCity3c; //订单中出发城市三字码
	public String toCity3c;   //订单中到达城市三字码
	
	public String manualOrder;//手工出票，默认不手工
	public String waitForOrder;//12306异常是否等待   11继续等待 00不等待      默认继续等待
	
	public String payMoneyExt;//咱们把抢票订单传给携程出票时，携程收取咱们的单人抢票服务费（咱们给携程的服务费，单人的）
	public String buyMoneyExt;//抢票时咱们收取客户的单人抢票服务费（客户给咱们的服务费，单人的）
	public String finalTrainNo;//最终抢到的车次
	public String finalSeatType;//最终抢到的座席类型
	public String paySerialNumber;//支付抢票订单时的支付序列号（要求唯一）
	public String leakCutOffTime;//捡漏截止日期
	public String ctripOrderId;//咱们传给携程的订单号，自己生成
	private List<JlOrderCP> orderCps;
	
	public List<JlOrderCP> getOrderCps() {
		return orderCps;
	}

	public void setOrderCps(List<JlOrderCP> orderCps) {
		this.orderCps = orderCps;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFromToZh() {
		return fromToZh;
	}

	public void setFromToZh(String fromToZh) {
		this.fromToZh = fromToZh;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(String outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainNoAccept() {
		return trainNoAccept;
	}

	public void setTrainNoAccept(String trainNoAccept) {
		this.trainNoAccept = trainNoAccept;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getSeatTypeAccept() {
		return seatTypeAccept;
	}

	public void setSeatTypeAccept(String seatTypeAccept) {
		this.seatTypeAccept = seatTypeAccept;
	}

	public String getOutTicketType() {
		return outTicketType;
	}

	public void setOutTicketType(String outTicketType) {
		this.outTicketType = outTicketType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
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

	public String getManualOrder() {
		return manualOrder;
	}

	public void setManualOrder(String manualOrder) {
		this.manualOrder = manualOrder;
	}

	public String getWaitForOrder() {
		return waitForOrder;
	}

	public void setWaitForOrder(String waitForOrder) {
		this.waitForOrder = waitForOrder;
	}

	public String getPayMoneyExt() {
		return payMoneyExt;
	}

	public void setPayMoneyExt(String payMoneyExt) {
		this.payMoneyExt = payMoneyExt;
	}

	public String getBuyMoneyExt() {
		return buyMoneyExt;
	}

	public void setBuyMoneyExt(String buyMoneyExt) {
		this.buyMoneyExt = buyMoneyExt;
	}

	public String getLeakCutOffTime() {
		return leakCutOffTime;
	}

	public void setLeakCutOffTime(String leakCutOffTime) {
		this.leakCutOffTime = leakCutOffTime;
	}
	

	public String getFinalTrainNo() {
		return finalTrainNo;
	}

	public void setFinalTrainNo(String finalTrainNo) {
		this.finalTrainNo = finalTrainNo;
	}

	public String getFinalSeatType() {
		return finalSeatType;
	}

	public void setFinalSeatType(String finalSeatType) {
		this.finalSeatType = finalSeatType;
	}

	public String getPaySerialNumber() {
		return paySerialNumber;
	}

	public void setPaySerialNumber(String paySerialNumber) {
		this.paySerialNumber = paySerialNumber;
	}
	
	public String getCtripOrderId() {
		return ctripOrderId;
	}

	public void setCtripOrderId(String ctripOrderId) {
		this.ctripOrderId = ctripOrderId;
	}



	public String seatTrains; //订单车票信息
	

	public String getSeatTrains() {
		return seatTrains;
	}

	public void setSeatTrains(String seatTrains) {
		this.seatTrains = seatTrains;
	}

	public List<JlOrderCP> getOrderCPs(){
		//订单号|用户名|车票类型|证件类型|证件号码|坐席$用户名|车票类型|证件类型|证件号码|坐席$用户名|车票类型|证件类型|证件号码|坐席
		return orderCps;
	}

	/**
	 * 充值下单返回结果
	 * @param userbillNo
	 * @param downBillTime
	 * @param phoneNo
	 * @param facevalue
	 * @param billno
	 * @param codeNo
	 * @return
	 */
	public String responstValue(JlOrder supOrder, String codeNo){
		
		if(StringUtils.equals(codeNo, ORDER_SUCCESS)){
			return "jl_order success|"+this.orderId;
			
		}else if(StringUtils.equals(codeNo, ORDER_ERROR)){
			return "jl_order failure|"+this.orderId;
		}else{
			return "jl_order exception|"+this.orderId;
		}
	}
}
