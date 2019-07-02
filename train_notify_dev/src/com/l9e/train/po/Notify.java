package com.l9e.train.po;

public class Notify {
	private String hfOrderid;
	private String sellerOrderid;
	private String supMoney;
	private String faceValue;
	private String notifyUrl;
	private String status;
	

	
	public static int NOTIFY_NOT = 0; //未通知
	public static int NOTIFY_WAIT = 1;  //等待通知
	public static int NOTIFY_ING = 2;   //正在通知
	public static int NOTIFY_SUCCESS = 3;  //通知成功
	public static int NOTIFY_FAILURE = 4;  //通知失败
	public static int NOTIFY_RESTORE = 5; //重新通知
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHfOrderid() {
		return hfOrderid;
	}
	public void setHfOrderid(String hfOrderid) {
		this.hfOrderid = hfOrderid;
	}
	public String getSellerOrderid() {
		return sellerOrderid;
	}
	public void setSellerOrderid(String sellerOrderid) {
		this.sellerOrderid = sellerOrderid;
	}
	public String getSupMoney() {
		return supMoney;
	}
	public void setSupMoney(String supMoney) {
		this.supMoney = supMoney;
	}
	public String getFaceValue() {
		return faceValue;
	}
	public void setFaceValue(String faceValue) {
		this.faceValue = faceValue;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	
}
