package com.l9e.transaction.vo;

import java.util.LinkedHashMap;

public class AppNoticeVo {
	String notice_id;
	String notice_status;
	//String pub_time;
	//String stop_time;
	String notice_name;
	String notice_content;
	//String provinces;
	String opt_ren;
	String notice_system;
	String user_phone;  
	private static LinkedHashMap<String, String> NOTICESTATUS = new LinkedHashMap<String, String>();
	static{
		NOTICESTATUS.put("00", "未发布");
		NOTICESTATUS.put("11", "已发布");
		NOTICESTATUS.put("22", "已到期");
	}
	public static LinkedHashMap<String, String> getNoticeStatusMap(){
		return NOTICESTATUS;
	}
	//消息发布对象： 00、全部 11、ios 22、android 33、个人
	private static LinkedHashMap<String, String> NOTICESYSTEM = new LinkedHashMap<String, String>();
	static{
		NOTICESYSTEM.put("00", "全部");
		NOTICESYSTEM.put("11", "IOS");
		NOTICESYSTEM.put("22", "Android");
		NOTICESYSTEM.put("33", "个人");
	}
	public static LinkedHashMap<String, String> getNoticeSystemMap(){
		return NOTICESYSTEM;
	}
	


	public String getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}
	public String getNotice_status() {
		return notice_status;
	}
	public void setNotice_status(String notice_status) {
		this.notice_status = notice_status;
	}
	public String getNotice_system() {
		return notice_system;
	}
	public void setNotice_system(String noticeSystem) {
		notice_system = noticeSystem;
	}
	public String getNotice_name() {
		return notice_name;
	}
	public void setNotice_name(String notice_name) {
		this.notice_name = notice_name;
	}
	public String getNotice_content() {
		return notice_content;
	}
	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}
//	public String getProvinces() {
//		return provinces;
//	}
//	public void setProvinces(String provinces) {
//		this.provinces = provinces;
//	}
	public String getOpt_ren() {
		return opt_ren;
	}
	public void setOpt_ren(String opt_ren) {
		this.opt_ren = opt_ren;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String userPhone) {
		user_phone = userPhone;
	}

}
