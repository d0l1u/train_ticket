package com.l9e.common;

import java.io.Serializable;

/**
 * 员工登陆存放session的实体类
 * 
 * @author
 */
public class LoginUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String user_id;//用户ID
	private String user_phone;//手机号（登陆账号）
	private String user_name;//用户名
	private String user_password;//账号密码
	private String create_time;//创建时间（注册时间）
	private String user_source;//app:（手机客户端）APP、web:（网站）WEB、weixin：微信、baidu：百度
	private String weather_able;//是否启用 1：启用 0：停用
	
	
	private String user_email;//用户邮箱
	private String user_verify;//用户证件信息审核 00：正在审核 11：审核通过 22：审核未通过
	private String login_time;//登录时间
	private String last_login_time;//最后登录时间
	private String login_ip;//登录IP
	private String modify_time;//修改时间
	private String gps_info;//位置信息
	private String acc_12306_name;//12306账号
	private String acc_12306_pwd;//12306密码
	private String score_num;//用户积分
	private String referee_account;//推荐人账号
	private String verify_code;//注册验证码（六位数字）
	private String login_num;//登陆次数
	private String user_sex;//用户性别：0、男 1、女
	private String user_birth;//用户出生日期
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String userPhone) {
		user_phone = userPhone;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String userPassword) {
		user_password = userPassword;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String userEmail) {
		user_email = userEmail;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getUser_source() {
		return user_source;
	}
	public void setUser_source(String userSource) {
		user_source = userSource;
	}
	public String getWeather_able() {
		return weather_able;
	}
	public void setWeather_able(String weatherAble) {
		weather_able = weatherAble;
	}
	public String getUser_verify() {
		return user_verify;
	}
	public void setUser_verify(String userVerify) {
		user_verify = userVerify;
	}
	public String getLogin_time() {
		return login_time;
	}
	public void setLogin_time(String loginTime) {
		login_time = loginTime;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String lastLoginTime) {
		last_login_time = lastLoginTime;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modifyTime) {
		modify_time = modifyTime;
	}
	public String getGps_info() {
		return gps_info;
	}
	public void setGps_info(String gpsInfo) {
		gps_info = gpsInfo;
	}
	public String getAcc_12306_name() {
		return acc_12306_name;
	}
	public void setAcc_12306_name(String acc_12306Name) {
		acc_12306_name = acc_12306Name;
	}
	public String getAcc_12306_pwd() {
		return acc_12306_pwd;
	}
	public void setAcc_12306_pwd(String acc_12306Pwd) {
		acc_12306_pwd = acc_12306Pwd;
	}
	public String getScore_num() {
		return score_num;
	}
	public void setScore_num(String scoreNum) {
		score_num = scoreNum;
	}
	public String getReferee_account() {
		return referee_account;
	}
	public void setReferee_account(String refereeAccount) {
		referee_account = refereeAccount;
	}
	public String getVerify_code() {
		return verify_code;
	}
	public void setVerify_code(String verifyCode) {
		verify_code = verifyCode;
	}
	public String getLogin_num() {
		return login_num;
	}
	public void setLogin_num(String loginNum) {
		login_num = loginNum;
	}
	public String getLogin_ip() {
		return login_ip;
	}
	public void setLogin_ip(String loginIp) {
		login_ip = loginIp;
	}
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String userSex) {
		user_sex = userSex;
	}
	public String getUser_birth() {
		return user_birth;
	}
	public void setUser_birth(String userBirth) {
		user_birth = userBirth;
	}
	
}
