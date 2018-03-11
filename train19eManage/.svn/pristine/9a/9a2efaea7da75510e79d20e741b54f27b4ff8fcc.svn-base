package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class SystemSettingVo {

	private String setting_id;
	private String setting_name;
	private String setting_value;
	private String setting_status;
	private String setting_desc;
	
	private String show_name;
	private String show_type;
	private String show_priority;
	private String show_list;

	public String getSetting_id() {
		return setting_id;
	}

	public void setSetting_id(String setting_id) {
		this.setting_id = setting_id;
	}

	public String getSetting_name() {
		return setting_name;
	}

	public void setSetting_name(String setting_name) {
		this.setting_name = setting_name;
	}

	public String getSetting_value() {
		return setting_value;
	}

	public void setSetting_value(String setting_value) {
		this.setting_value = setting_value;
	}

	public String getSetting_status() {
		return setting_status;
	}

	public void setSetting_status(String setting_status) {
		this.setting_status = setting_status;
	}

	public String getSetting_desc() {
		return setting_desc;
	}

	public void setSetting_desc(String setting_desc) {
		this.setting_desc = setting_desc;
	}
	
	public static String SYSTEM_BOOK_WEXIN = "1";
	public static String SYSTEM_BOOK_IOS = "2";
	public static String SYSTEM_BOOK_ANDROID = "3";
	public static String SYSTEM_BAIDU = "4";
	
	private static Map<String,String>SYSTEM_BOOK = new LinkedHashMap<String,String>();
	
	public static Map<String,String>getSystemBooks(){
		if(SYSTEM_BOOK.isEmpty()){
			SYSTEM_BOOK.put(SYSTEM_BOOK_WEXIN, "微信WAP");
			SYSTEM_BOOK.put(SYSTEM_BOOK_IOS, "IOS");
			SYSTEM_BOOK.put(SYSTEM_BOOK_ANDROID, "ANDROID");
			SYSTEM_BOOK.put(SYSTEM_BAIDU, "百度");
		}
		return SYSTEM_BOOK;
	}

	public String getShow_name() {
		return show_name;
	}

	public void setShow_name(String showName) {
		show_name = showName;
	}

	public String getShow_type() {
		return show_type;
	}

	public void setShow_type(String showType) {
		show_type = showType;
	}

	public String getShow_priority() {
		return show_priority;
	}

	public void setShow_priority(String showPriority) {
		show_priority = showPriority;
	}

	public String getShow_list() {
		return show_list;
	}

	public void setShow_list(String showList) {
		show_list = showList;
	}
	
	
}
