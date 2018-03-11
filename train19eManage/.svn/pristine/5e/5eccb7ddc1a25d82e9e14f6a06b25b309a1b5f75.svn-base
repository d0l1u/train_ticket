package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class NoticeVo {
	
	String notice_id;
	String notice_status;
	String pub_time;
	String stop_time;
	String notice_name;
	String notice_content;
	String provinces;
	String opt_ren;
	String inner_channel;
	String ext_channel;
	

	private static LinkedHashMap<String, String> NOTICESTATUS = new LinkedHashMap<String, String>();
	
	static{
		NOTICESTATUS.put("00", "未发布");
		NOTICESTATUS.put("11", "已发布");
		NOTICESTATUS.put("22", "已到期");
	}

	public static LinkedHashMap<String, String> getNoticeStatusMap(){
		
		return NOTICESTATUS;
	}
	private static LinkedHashMap<String, String> Channel = new LinkedHashMap<String, String>();
	static{
		Channel.put("19pay", "19pay");
		Channel.put("cmpay", "cmpay");
		Channel.put("ccb", "建行");
	}
	public static LinkedHashMap<String, String> getChannel(){
		
		return Channel;
	}
	
	public String getStop_time() {
		return stop_time;
	}


	public void setStop_time(String stop_time) {
		this.stop_time = stop_time;
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
	public String getPub_time() {
		return pub_time;
	}
	public void setPub_time(String pub_time) {
		this.pub_time = pub_time;
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
	public String getProvinces() {
		return provinces;
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}


	public String getOpt_ren() {
		return opt_ren;
	}


	public void setOpt_ren(String opt_ren) {
		this.opt_ren = opt_ren;
	}

	public String getInner_channel() {
		return inner_channel;
	}

	public void setInner_channel(String innerChannel) {
		this.inner_channel = innerChannel;
	}

	public String getExt_channel() {
		return ext_channel;
	}

	public void setExt_channel(String extChannel) {
		this.ext_channel = extChannel;
	}

	



	

}
