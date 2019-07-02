package com.l9e.train.po;

import java.util.List;

public class RefundResultEntity {
	private String orderId;
	private String outTicketBillno;
	private String contactsnum;             
	private String summoney;                 
	private String from;                            
	private String to;
	private String seattime;
	private String trainno;
	private String retInfo;
	private String retValue;
	private List<ReturnRefundPasEntity> cps;
	
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
	public List<ReturnRefundPasEntity> getCps() {
		return cps;
	}
	public void setCps(List<ReturnRefundPasEntity> cps) {
		this.cps = cps;
	}
}
