package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginUserVo implements Serializable {
	private Integer user_id;
	private String username;
	private String pwd;
	private String real_name;
	private Integer login_status;  //登入状态： 0 离线 1 在线 2 暂停
	private Integer code_total_number;
	private Integer code_error_number;
	private Date last_login_time;
	private String telephone;
	private String email;
	private String user_level;
	private String department;
	private String monthMoney;
	private String logined;


	public static Map<String,String> user_type = new LinkedHashMap<String,String>();//用户等级：1普通用户 2部门管理员 3公司管理员 4超级管理员
	public static final String SUPERADMIN = "4";//超级管理员
	//public static final String COMPANY = "3";//公司管理员
	public static final String ADMIN = "2";//部门管理员
	public static final String USER = "1";//普通用户 
	public static Map<String,String> getUser_type(){
		if(user_type.isEmpty()){
			user_type.put(USER, "普通用户 ");
			user_type.put(ADMIN, "部门管理员");
			//user_type.put(COMPANY, "公司管理员");
			user_type.put(SUPERADMIN, "超级管理员");
		}
		return user_type;
	}
	
	public static Map<String,String> user_department = new LinkedHashMap<String,String>();//所在部门：1客服部、2运营部、3研发部
	public static final String CUSTOMER = "1";//客服部
	public static final String OPERATION = "2";//运营部
	public static final String REARCHER = "3";//研发部
	public static final String OTHER = "4";//其他部门
	public static final String DELETED = null;//被删除用户，显示部门为其他
	public static final String EXT = "5";//对外部门
	public static final String EXT02 = "00";//对外部门02
	public static final String TONGCHENG = "6";//同程部门
	public static final String AIR = "7";//机票部门
	public static final String DAILI = "8";//代理商部门
	public static final String ELONG = "9";//艺龙部门
	public static final String ALL = "";//全部
	public static Map<String,String> getUser_department(){
		if(user_department.isEmpty()){
			user_department.put(ALL, "全部");
			user_department.put(CUSTOMER, "客服部");
			user_department.put(OPERATION, "运营部");
			user_department.put(REARCHER, "研发部");
			user_department.put(EXT, "对外");
			user_department.put(EXT02, "对外02");
			user_department.put(TONGCHENG, "同程");
			user_department.put(ELONG, "艺龙");
			user_department.put(AIR, "机票");
			user_department.put(DAILI, "代理商");
			user_department.put(OTHER, "其他");
//			user_department.put(DELETED, "其他");
		}
		return user_department;
	}
	
	public static Map<Integer,String> loginStatus = new LinkedHashMap<Integer,String>();//登入状态： 0 离线 1 在线 2 暂停
	public static final int LOGINED = 1;// 在线
	public static final int NOlOGIN = 0;// 离线
	public static final int PAUSE = 3;// 暂停
	public static Map<Integer,String> getLoginStatus(){
		if(loginStatus.isEmpty()){
			loginStatus.put(LOGINED, "在线");
			loginStatus.put(NOlOGIN, "离线");
			loginStatus.put(PAUSE, "暂停");
		}
		return loginStatus;
	}
	
	public static Map<String, String> codeChannel =  new LinkedHashMap<String, String>();//打码渠道
	public static final String QUANR = "qunar";//去哪
	public static final String DAMATU = "damatu";//打码兔
	public static final String YAOJIUE = "19e";//19e
	public static final String WUREN = "wuren";//无人打码
	public static Map<String, String> getCodeChannel(){
		if(codeChannel.isEmpty()){
			codeChannel.put(DAMATU, "打码兔");
			codeChannel.put(YAOJIUE, "19e");
			codeChannel.put(QUANR, "去哪");
			codeChannel.put(WUREN, "无人");
		}
		return codeChannel;
	}
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer userId) {
		user_id = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String realName) {
		real_name = realName;
	}
	public Integer getLogin_status() {
		return login_status;
	}
	public void setLogin_status(Integer loginStatus) {
		login_status = loginStatus;
	}
	public Integer getCode_total_number() {
		return code_total_number;
	}
	public void setCode_total_number(Integer codeTotalNumber) {
		code_total_number = codeTotalNumber;
	}
	public Integer getCode_error_number() {
		return code_error_number;
	}
	public void setCode_error_number(Integer codeErrorNumber) {
		code_error_number = codeErrorNumber;
	}
	public Date getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(Date lastLoginTime) {
		last_login_time = lastLoginTime;
	}

	public String getUser_level() {
		return user_level;
	}

	public void setUser_level(String userLevel) {
		user_level = userLevel;
	}
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}

	public String getLogined() {
		return logined;
	}

	public void setLogined(String logined) {
		this.logined = logined;
	}
	
	
}
