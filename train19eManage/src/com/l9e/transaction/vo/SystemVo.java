package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class SystemVo {
	
	String config_id;
	String province_id;
	String is_open;
	String is_cost;
	String is_ps;
	String is_buyable;
	String rule_content;
	String setting_value ;
	String opt_ren;
	private static Map<String, String> OPENSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> PSSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> COSTSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> SETTING = new LinkedHashMap<String,String> () ;
	private static Map<String, String> ISBUYSTATUS = new LinkedHashMap<String,String>();
	
	static{
		OPENSTATUS.put("00", "否");
		OPENSTATUS.put("11", "是");
		
		PSSTATUS.put("00", "否");
		PSSTATUS.put("11", "是");
		
		COSTSTATUS.put("00", "否");
		COSTSTATUS.put("11", "是");
		
		
	}
	private static String JIQIREN="1" ; 
	private static String OTHER = "2"; 
	private static String ISNOTBUY ="00";
	private static String ISBUYOK = "11";
	
	public static Map<String,String>getSETTING(){
		if(SETTING.isEmpty()){
			SETTING.put(JIQIREN, "机器人接口") ;
			SETTING.put(OTHER, "第三方接口") ;
		}
		return SETTING ;
	}
	public static Map<String,String>getISBUYMAP(){
		if(ISBUYSTATUS.isEmpty()){
			ISBUYSTATUS.put(ISBUYOK, "是");
			ISBUYSTATUS.put(ISNOTBUY, "否");
		}
		return ISBUYSTATUS ;
	}
	
	public static Map<String, String> getOpenStatus(){
		return OPENSTATUS;
	}
	
	public String getSetting_value() {
		return setting_value;
	}

	public void setSetting_value(String setting_value) {
		this.setting_value = setting_value;
	}

	public static void setSETTING(Map<String, String> setting) {
		SETTING = setting;
	}

	public static Map<String, String> getPsStatus(){
		return PSSTATUS;
	}
	
	public static Map<String, String> getCostStatus(){
		return COSTSTATUS;
	}
	
	public String getConfig_id() {
		return config_id;
	}
	public void setConfig_id(String config_id) {
		this.config_id = config_id;
	}
	public String getProvince_id() {
		return province_id;
	}
	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}
	public String getIs_open() {
		return is_open;
	}
	public void setIs_open(String is_open) {
		this.is_open = is_open;
	}
	public String getIs_cost() {
		return is_cost;
	}
	public void setIs_cost(String is_cost) {
		this.is_cost = is_cost;
	}
	public String getIs_ps() {
		return is_ps;
	}
	public void setIs_ps(String is_ps) {
		this.is_ps = is_ps;
	}
	public String getRule_content() {
		return rule_content;
	}
	public void setRule_content(String rule_content) {
		this.rule_content = rule_content;
	}

	public String getIs_buyable() {
		return is_buyable;
	}

	public void setIs_buyable(String is_buyable) {
		this.is_buyable = is_buyable;
	}
	public String getOpt_ren() {
		return opt_ren;
	}
	public void setOpt_ren(String opt_ren) {
		this.opt_ren = opt_ren;
	}
	
	

}
