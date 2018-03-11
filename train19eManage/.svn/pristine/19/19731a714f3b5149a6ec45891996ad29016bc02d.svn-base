package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class MailVo implements Serializable {
	private String mail_id;
	private String address;
	private String pwd;
	private String status;
	private String create_time;
	private String opt_name;
	private String is_open;
	
	//邮箱状态：0、未使用 1、已使用
	public static final String MAIL_NO = "0"; //未使用
	public static final String MAIL_USE ="1";//已使用
	
	public static Map<String,String>MAIL_STATUS = new LinkedHashMap<String,String>();
	public static Map<String,String> getMailStatus(){
		if(MAIL_STATUS.isEmpty()){
			MAIL_STATUS.put(MAIL_NO, "未使用");
			MAIL_STATUS.put(MAIL_USE, "已使用");
		}
		return MAIL_STATUS ;
	}
	
	//是否启用：0停用 1启用
	public static final String MAIL_STOP = "0"; //停用
	public static final String MAIL_OPEN ="1";//启用
	
	public static Map<String,String>MAIL_ISOPEN = new LinkedHashMap<String,String>();
	public static Map<String,String> getMailIsOpen(){
		if(MAIL_ISOPEN.isEmpty()){
			MAIL_ISOPEN.put(MAIL_STOP, "停用");
			MAIL_ISOPEN.put(MAIL_OPEN, "启用");
		}
		return MAIL_ISOPEN ;
	}
	
	public String getMail_id() {
		return mail_id;
	}
	public void setMail_id(String mailId) {
		mail_id = mailId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getOpt_name() {
		return opt_name;
	}
	public void setOpt_name(String optName) {
		opt_name = optName;
	}
	public String getIs_open() {
		return is_open;
	}
	public void setIs_open(String isOpen) {
		is_open = isOpen;
	}
	
}
