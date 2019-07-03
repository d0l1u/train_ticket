package com.l9e.transaction.vo;

public class AgentRegistVo {
	private String regist_id;
	private String user_id;
	private String user_name;
	private String user_phone;
	private String ids_card;
	
	private String account_name;
	private String account_pwd;
	private String mail;
	private String mail_pwd;
	private String regist_status;
	private String description;
	private String fail_reason;
	
	public String getRegist_id() {
		return regist_id;
	}
	public void setRegist_id(String registId) {
		regist_id = registId;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String userPhone) {
		user_phone = userPhone;
	}
	public String getIds_card() {
		return ids_card;
	}
	public void setIds_card(String idsCard) {
		ids_card = idsCard;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String accountName) {
		account_name = accountName;
	}
	public String getAccount_pwd() {
		return account_pwd;
	}
	public void setAccount_pwd(String accountPwd) {
		account_pwd = accountPwd;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMail_pwd() {
		return mail_pwd;
	}
	public void setMail_pwd(String mailPwd) {
		mail_pwd = mailPwd;
	}
	public String getRegist_status() {
		return regist_status;
	}
	public void setRegist_status(String registStatus) {
		regist_status = registStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFail_reason() {
		return fail_reason;
	}
	public void setFail_reason(String failReason) {
		fail_reason = failReason;
	}
}
