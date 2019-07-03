package com.l9e.transaction.vo;

import java.io.Serializable;

//人工 用户信息
public class UserInfo implements Serializable {
	private String username;
	private String pwd;
	private String department; //部门
	private int login_status;//登入状态
	private String picName;//负责处理图片名
	private String resultStr;//返回结果
	private Picture picture;//处理的图片
	private String last_login_time;
	private String get_code_time_limit;//用户打码器拉码时间间隔（毫秒ms）
	private String user_code_version;//用户所使用的打码器版本号
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Picture getPicture() {
		return picture;
	}
	public void setPicture(Picture picture) {
		this.picture = picture;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getLogin_status() {
		return login_status;
	}
	public void setLogin_status(int loginStatus) {
		login_status = loginStatus;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getResultStr() {
		return resultStr;
	}
	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String lastLoginTime) {
		last_login_time = lastLoginTime;
	}
	public String getGet_code_time_limit() {
		return get_code_time_limit;
	}
	public void setGet_code_time_limit(String getCodeTimeLimit) {
		get_code_time_limit = getCodeTimeLimit;
	}
	public String getUser_code_version() {
		return user_code_version;
	}
	public void setUser_code_version(String userCodeVersion) {
		user_code_version = userCodeVersion;
	}
	
}
