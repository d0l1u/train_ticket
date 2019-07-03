package com.l9e.train.po;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

public class Order {
	
	
	
	

	public static String STATUS_ORDER_START = "00";//开始出票
	public static String STATUS_ORDER_ING = "11";//正在预定
	public static String STATUS_ORDER_FAILURE= "22";//预定失败
	public static String STATUS_ORDER_SUCCESS= "33";//预定成功
	public static String STATUS_ORDER_MANUAL= "44";//预定人工
	
	public static String STATUS_PAY_START= "55";//开始支付
	public static String STATUS_PAY_ING= "66";//正在支付
	public static String STATUS_PAY_SUCCESS= "88";//支付成功

	public static String STATUS_ORDER_RESEND = "01";//重发订单
	
	public static String STATUS_BILL_SUCCESS= "99";//订单成功
	public static String STATUS_BILL_FAILURE= "10";//订单失败
	
	public static String STATUS_FINDING= "81";//正在查询
	public static String STATUS_FIND_MANUAL= "82";//查询人工
	
	public static String STATUS_CANCELING= "83";//正在取消
	
	public static String STATUS_CANCEL_START= "85";//开始取消
	public static String STATUS_CANCEL_FAILURE= "77";//取消失败
	
	@JsonProperty("orderId")  
	public String orderId;
	
	public String orderstr;
	
	public String orderStatus;
	
	@JsonProperty("outTicketBillno")  
	public String outTicketBillNo;
	
	public Integer accountId;
	
	public List<String> orderCps;
	
	@JsonProperty("retValue")  
	public String retValue;//返回值
	
	@JsonProperty("retInfo")  
	public String retInfo;//返回信息
	
	@JsonProperty("robotNum")
	public String robotNum;
	
	
	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public String getRetInfo() {
		return retInfo;
	}

	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
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

	public void addOrderCp(String str){
		if(orderCps==null){
			orderCps = new ArrayList<String>();
		}
		
		orderCps.add(str);
	}

	public String getRobotNum() {
		return robotNum;
	}

	public void setRobotNum(String robotNum) {
		this.robotNum = robotNum;
	}
	
	
}
