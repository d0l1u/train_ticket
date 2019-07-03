package com.l9e.transaction.vo;
/**
 * 携程账号信息
 * @author wangsf
 *
 */

public class CtripAcc {
	public static String STATUS_WORKING = "00";//启用
	public static String STATUS_STOP = "11";//停用
	
	//0-空闲 1- 使用中
	public static int OPT_STATUS_0 = 0;
	public static int OPT_STATUS_1 = 1;
	
	
	public String  ctripName;
	public String  ctripPassword;
	public String  ctripStatus;
	public String  payPassword;
	public String  ctripUsername;
	public String  ctripPhone;
	public Integer ctripId;
	
	public String cookie;//携程wap端帐号cookie
	public String cid;//携程wap端帐号cid
	public String auth;//携程wap端帐号auth
	public String sauth;//携程wap端帐号sauth
	
	public double balance;//余额
	public int accDegree;//帐号等级,根据充值确认1:充值100 2:充值500 3:充值1000 4:充值2000 5:充值5000
	public int order_succ_time;//下单成功次数
	public int optStatus;//0-空闲 1- 使用中
	public int resultType;//查询返回结果类型：0：正常   1：账号异常    2：查询超时   
	
	public String getCtripName() {
		return ctripName;
	}
	public void setCtripName(String ctripName) {
		this.ctripName = ctripName;
	}
	public String getCtripPassword() {
		return ctripPassword;
	}
	public void setCtripPassword(String ctripPassword) {
		this.ctripPassword = ctripPassword;
	}
	public String getCtripStatus() {
		return ctripStatus;
	}
	public void setCtripStatus(String ctripStatus) {
		this.ctripStatus = ctripStatus;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getCtripUsername() {
		return ctripUsername;
	}
	public void setCtripUsername(String ctripUsername) {
		this.ctripUsername = ctripUsername;
	}
	public String getCtripPhone() {
		return ctripPhone;
	}
	public void setCtripPhone(String ctripPhone) {
		this.ctripPhone = ctripPhone;
	}
	public Integer getCtripId() {
		return ctripId;
	}
	public void setCtripId(Integer ctripId) {
		this.ctripId = ctripId;
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
	public int getAccDegree() {
		return accDegree;
	}
	public void setAccDegree(int accDegree) {
		this.accDegree = accDegree;
	}
	public int getOrder_succ_time() {
		return order_succ_time;
	}
	public void setOrder_succ_time(int orderSuccTime) {
		order_succ_time = orderSuccTime;
	}
	public int getOptStatus() {
		return optStatus;
	}
	public void setOptStatus(int optStatus) {
		this.optStatus = optStatus;
	}
	public int getResultType() {
		return resultType;
	}
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}
	
	
	
	
}
