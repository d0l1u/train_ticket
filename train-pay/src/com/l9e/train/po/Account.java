package com.l9e.train.po;


public class Account {

	public String accId;
	public String accUsername;
	public String accPassword;

	
	public static String DOWNING = "00"; //正在下单
	public static String QUEUE = "01"; //队列中
	public static String STOP = "22"; //账号停用
	public static String FREE = "33"; //账号空闲
	public static String TEMP_STOP = "44"; //临时停用
	
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getAccUsername() {
		return accUsername;
	}
	public void setAccUsername(String accUsername) {
		this.accUsername = accUsername;
	}
	public String getAccPassword() {
		return accPassword;
	}
	
	public void setAccPassword(String accPassword) {
		this.accPassword = accPassword;
	}
	
}
