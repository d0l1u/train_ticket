package com.l9e.train.po;

import java.util.List;

public class AlterResultEntity {
	private String retInfo;
	private String retValue;
	private String orderId;
	private String outTicketBillno;
	private String contactsnum;             
	private String summoney;                 
	private String from;                            
	private String to;
	private String seattime;
	private String trainno;
	private String arrivetime;
	private List<ReturnAlterPasEntity> cps;
	private Boolean insertCaptcha;//给途牛多出的字段
	private String  pay_limit_time;//支付截止时间
	private String old_ticket_price;//改签前的总价格balance
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOutTicketBillno() {
		return outTicketBillno;
	}
	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}
	public String getContactsnum() {
		return contactsnum;
	}
	public void setContactsnum(String contactsnum) {
		this.contactsnum = contactsnum;
	}
	public String getSummoney() {
		return summoney;
	}
	public void setSummoney(String summoney) {
		this.summoney = summoney;
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
	public String getTrainno() {
		return trainno;
	}
	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}
	public List<ReturnAlterPasEntity> getCps() {
		return cps;
	}
	public void setCps(List<ReturnAlterPasEntity> cps) {
		this.cps = cps;
	}
	public String getArrivetime() {
		return arrivetime;
	}
	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}
	public String getOld_ticket_price() {
		return old_ticket_price;
	}
	public void setOld_ticket_price(String oldTicketPrice) {
		old_ticket_price = oldTicketPrice;
	}
	public String getRetInfo() {
		return retInfo;
	}
	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}
	public String getRetValue() {
		return retValue;
	}
	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	public Boolean getInsertCaptcha() {
		return insertCaptcha;
	}
	public void setInsertCaptcha(Boolean insertCaptcha) {
		this.insertCaptcha = insertCaptcha;
	}
	public String getPay_limit_time() {
		return pay_limit_time;
	}
	public void setPay_limit_time(String payLimitTime) {
		pay_limit_time = payLimitTime;
	}
	
}
