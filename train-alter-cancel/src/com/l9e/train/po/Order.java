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
	
	public static String STATUS_TCC_CANCEL_FAILURE = "24";//同程改签取消失败
	public static String STATUS_TCC_CHANGE_FAILURE = "15";//同程改签预订失败
	public static String STATUS_TCC_CANCEL_SUCCESS = "23";//同程改签取消成功
	public static String STATUS_TCC_CANCEL_START = "21";//同程改签取消
	public static String STATUS_TCC_CANCEL_ING = "22";//同程正在取消改签
	
	/**
	 * 错误码   324 : 已确认改签，不可取消
	 */
	public static String FAILCODE_VERIFYALTER = "324"; //已确认改签，不可取消
	
	public static final int ORDER_SOURCE_CP = 9527;//cp_orderinfo订单
	public static final int ORDER_SOURCE_TCC = 9528;//elong_orderinfo_change同程改签订单
	
	
	@JsonProperty("orderId")  
	public String orderId;
	
	/**
	 * 改签id
	 */
	public Integer changeId;
	
	public String orderstr;
	
	public String orderStatus;
	
	private String fromCity;//出发站
	private String toCity;//到达站
	public String fromCity_3c; //订单中的出发城市三字码	
	public String toCity_3c; //订单中的到达城市三字码
	
	@JsonProperty("outTicketBillno")  
	public String outTicketBillNo;
	
	public Integer accountId;
	
	public List<String> orderCps;
	
	@JsonProperty("retValue")  
	public String retValue;//返回值
	
	@JsonProperty("retInfo")  
	public String retInfo;//返回信息
	
	@JsonProperty("robotNum")  
	public Integer robotNum;
	
	
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

	public Integer getChangeId() {
		return changeId;
	}

	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
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

	public Integer getRobotNum() {
		return robotNum;
	}

	public void setRobotNum(Integer robotNum) {
		this.robotNum = robotNum;
	}
	
	
}
