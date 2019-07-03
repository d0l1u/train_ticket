package com.l9e.train.po;

/**
 * 京东账号实体类
 * @author wangsf01
 *
 */
public class JdAcc {
	public static String STATUS_FREE = "00";//空闲
	public static String STATUS_WORKING = "11";//使用中
	public static String STATUS_STOP = "99";//停用
	
	public Integer JdId;//京东账号ID
	public String accountName;//京东账号名
	public String accountPwd;//京东账号登录密码
	public String accountPaypwd;//京东账号支付密码
	public String accountEmail;//京东账号邮箱
	public String accountEmailpwd;//京东账号邮箱密码
	public String accountStatus;//京东账号状态
	
	public Integer autoId;//京东优惠券ID
	public String couponNo;//京东优惠券编号
	
	

	public Integer getJdId() {
		return JdId;
	}
	public void setJdId(Integer jdId) {
		JdId = jdId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountPwd() {
		return accountPwd;
	}
	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}
	public String getAccountPaypwd() {
		return accountPaypwd;
	}
	public void setAccountPaypwd(String accountPaypwd) {
		this.accountPaypwd = accountPaypwd;
	}
	public String getAccountEmail() {
		return accountEmail;
	}
	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}
	public String getAccountEmailpwd() {
		return accountEmailpwd;
	}
	public void setAccountEmailpwd(String accountEmailpwd) {
		this.accountEmailpwd = accountEmailpwd;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Integer getAutoId() {
		return autoId;
	}
	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}
	public String getCouponNo() {
		return couponNo;
	}
	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}
	
	
}
