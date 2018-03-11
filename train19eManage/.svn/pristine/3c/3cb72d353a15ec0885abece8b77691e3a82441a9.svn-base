package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppUserVo {
	
	//来源channel：（手机客户端）APP、（网站）WEB
	private static final String CHANNEL_APP = "app"; //19e
	private static final String CHANNEL_WEB = "web"; //批量导入
	private static final String CHANNEL_WEIXIN = "weixin"; //批量导入
	private static final String CHANNEL_BAIDU = "baidu"; //批量导入
	private static Map<String, String> CHANNEL_SOURCE = new LinkedHashMap<String, String>();
	public static Map<String,String>getChannels(){
		if(CHANNEL_SOURCE.isEmpty()){
			CHANNEL_SOURCE.put(CHANNEL_APP, "手机客户端");
			CHANNEL_SOURCE.put(CHANNEL_WEB, "WEB网站");
			CHANNEL_SOURCE.put(CHANNEL_WEIXIN, "微信");
			CHANNEL_SOURCE.put(CHANNEL_BAIDU, "百度");
		}
		return CHANNEL_SOURCE;
	}
	
	//是否启用 1：启用 0：不启用  weather_able
	private static final String WEATHER_YES = "1"; //启用
	private static final String WEATHER_NO = "0"; //停用
	private static Map<String, String> WEATHER = new LinkedHashMap<String, String>();
	public static Map<String,String>getWeatherAble(){
		if(WEATHER.isEmpty()){
			WEATHER.put(WEATHER_YES, "启用");
			WEATHER.put(WEATHER_NO, "停用");
		}
		return WEATHER;
	}
	
	/*******************乘客类型************************/
	public static final String TICKET_MAN = "0";//成人票
	public static final String TICKET_BABE = "1";//儿童票
	public static Map<String,String>TICKETTYPE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getTicket_Types(){
		if(TICKETTYPE_MAP.isEmpty()){
			TICKETTYPE_MAP.put(TICKET_MAN, "成人票");
			TICKETTYPE_MAP.put(TICKET_BABE, "儿童票");
		}
		return TICKETTYPE_MAP;
	}
	
	/*******************证件类型************************/
	//证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
	public static final Integer IDENTITY_CARD_ONE = 1;//一代身份证
	public static final Integer IDENTITY_CARD_TWO = 2;//二代身份证
	public static final Integer HONGKONG_MACAO_PASS_CARD = 3;//港澳通行证
	public static final Integer TAIWAN_PASS_CARD = 4;//台湾通行证
	public static final Integer PASSPORT = 5;//护照
	
	public static Map<Integer,String>CERTIFICATE_MAP = new LinkedHashMap<Integer,String>();
	public static Map<Integer,String> getCertificate_type(){
		if(CERTIFICATE_MAP.isEmpty()){
			CERTIFICATE_MAP.put(IDENTITY_CARD_ONE, "一代身份证");
			CERTIFICATE_MAP.put(IDENTITY_CARD_TWO, "二代身份证");
			CERTIFICATE_MAP.put(HONGKONG_MACAO_PASS_CARD, "港澳通行证");
			CERTIFICATE_MAP.put(TAIWAN_PASS_CARD, "台湾通行证");
			CERTIFICATE_MAP.put(PASSPORT, "护照");
		}
		return CERTIFICATE_MAP;
	}
	
	//联系人身份核验状态：1已通过 2待核验 3未通过
	public static final String LINKER_PASS = "1";//已通过
	public static final String LINKER_WAIT_PASS = "2";//待核验
	public static final String LINKER_NO_PASS = "3";//未通过
	
	public static Map<String,String>LINKERVERIFYSTATUS = new LinkedHashMap<String,String>();
	public static Map<String,String> getLinkerVerifyStatus(){
		if(LINKERVERIFYSTATUS.isEmpty()){
			LINKERVERIFYSTATUS.put(LINKER_PASS, "已通过");
			LINKERVERIFYSTATUS.put(LINKER_WAIT_PASS, "待核验");
			LINKERVERIFYSTATUS.put(LINKER_NO_PASS, "未通过");
		}
		return LINKERVERIFYSTATUS;
	}
	
	
	public static void main(String[] args){
		String str = "99- ";
		String[] arr = str.split("-");
		System.out.println(arr[0]+","+arr[1]);
	}
}
