package com.l9e.train.po;
/**携程账号信息*/
public class CtripAcc {
	public static String STATUS_WORKING = "00";//启用
	public static String STATUS_STOP = "11";//停用
	
	//0-空闲 1- 使用中
	public static int OPT_STATUS_0 = 0;
	public static int OPT_STATUS_1 = 1;
	
	
	public String  ctrip_name;
	public String  ctrip_password;
	public String  ctrip_status;
	public String  pay_password;
	public String  ctrip_username;
	public String  ctrip_phone;
	public String ctrip_id;
	
	public String cookie;//携程wap端帐号cookie
	public String cid;//携程wap端帐号cid
	public String auth;//携程wap端帐号auth
	public String sauth;//携程wap端帐号sauth
	
	public double balance;//余额
	public int acc_degree;//帐号等级,根据充值确认1:充值100 2:充值500 3:充值1000 4:充值2000 5:充值5000
	public int order_succ_time;//下单成功次数
	public int opt_status;//0-空闲 1- 使用中
	
	
	public String getCtrip_name() {
		return ctrip_name;
	}
	public void setCtrip_name(String ctripName) {
		ctrip_name = ctripName;
	}
	public String getCtrip_password() {
		return ctrip_password;
	}
	public void setCtrip_password(String ctripPassword) {
		ctrip_password = ctripPassword;
	}
	public String getCtrip_status() {
		return ctrip_status;
	}
	public void setCtrip_status(String ctripStatus) {
		ctrip_status = ctripStatus;
	}
	public String getPay_password() {
		return pay_password;
	}
	public void setPay_password(String payPassword) {
		pay_password = payPassword;
	}
	public String getCtrip_username() {
		return ctrip_username;
	}
	public void setCtrip_username(String ctripUsername) {
		ctrip_username = ctripUsername;
	}
	public String getCtrip_phone() {
		return ctrip_phone;
	}
	public void setCtrip_phone(String ctripPhone) {
		ctrip_phone = ctripPhone;
	}
	public String getCtrip_id() {
		return ctrip_id;
	}
	public void setCtrip_id(String ctripId) {
		ctrip_id = ctripId;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getSauth() {
		return sauth;
	}
	public void setSauth(String sauth) {
		this.sauth = sauth;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getAcc_degree() {
		return acc_degree;
	}
	public void setAcc_degree(int accDegree) {
		acc_degree = accDegree;
	}
	public int getOrder_succ_time() {
		return order_succ_time;
	}
	public void setOrder_succ_time(int orderSuccTime) {
		order_succ_time = orderSuccTime;
	}
	public int getOpt_status() {
		return opt_status;
	}
	public void setOpt_status(int optStatus) {
		opt_status = optStatus;
	}
	
}
