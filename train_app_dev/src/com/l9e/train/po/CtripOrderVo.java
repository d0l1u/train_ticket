package com.l9e.train.po;

import org.codehaus.jackson.annotate.JsonProperty;

public class CtripOrderVo {
	/*"payMoney":0,
	"arrivetime":"",
	"robotNum":8,
	"outTicketBillno":"",
	"retInfo":"没有进入携程会员信息页面",
	"orderId":"HC1506182151557086",
	"ctrip_id":"",
	"retValue":"failure"*/
	@JsonProperty("payMoney")  
	String payMoney;
	
	//保险价格
	@JsonProperty("bxPrice")  
	String bxPrice;
	
	@JsonProperty("robotNum")  
	String robotNum;
	
	@JsonProperty("outTicketBillno")  
	String outTicketBillno;
	
	@JsonProperty("retInfo")  
	String retInfo;
	
	@JsonProperty("orderId")  
	String orderId;
	
	@JsonProperty("retValue")  
	String retValue;
	
	@JsonProperty("ctrip_id")  
	String ctrip_id;
	
	@JsonProperty("arrivetime")  
	String arrivetime;
	
	public String getArrivetime() {
		return arrivetime;
	}

	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}

	public String getCtrip_id() {
		return ctrip_id;
	}

	public void setCtrip_id(String ctripId) {
		ctrip_id = ctripId;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getRobotNum() {
		return robotNum;
	}

	public void setRobotNum(String robotNum) {
		this.robotNum = robotNum;
	}

	public String getOutTicketBillno() {
		return outTicketBillno;
	}

	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}

	public String getRetInfo() {
		return retInfo;
	}

	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public String getBxPrice() {
		return bxPrice;
	}

	public void setBxPrice(String bxPrice) {
		this.bxPrice = bxPrice;
	}

	
	
}
