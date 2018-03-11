package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginUserVo implements Serializable {

	
	private String user_id;
	private String user_name;
	private String password;
	private String real_name;
	private String user_level;
	private String user_IsOpen;
	private String supervise_name;
	
	public static final String COMMONLY = "0";//普通用户
	public static final String SENIOR = "1";//高级用户
	public static final String MANAGER = "2";//管理员
	public static final String PRESIDE = "1.1";//省级负责人
	public static final String KAOHE = "1.2";
	
	public static final String LEADER = "77";//出票员领导
	public static final String USER = "78";//出票员（人工出票团队）
	
//	public static final String BEIJING = "110000";//北京
//	public static final String TAINJIN = "120000";//天津
//	public static final String HEBEI = "130000";//河北
//	public static final String SHANXI = "140000";//山西
//	public static final String NEIMENG = "150000";//内蒙
//	public static final String LIAONING = "210000";//辽宁
//	public static final String JILIN = "220000";//吉林
//	public static final String HEILONGJIANG = "230000";// 黑龙江
//	public static final String SHANGHAI = "310000";//上海
//	public static final String JIANGSU = "320000";//江苏
//	public static final String ZHEJIANG = "330000";//浙江
//	public static final String ANHUI = "340000";//安徽
//	public static final String FUJIAN = "350000";//福建
//	public static final String JIANGXI = "360000";//江西
//	public static final String SHENDONG ="370000";//山东
//	public static final String HENAN = "410000";//河南
//	public static final String HUBEI = "420000";//湖北
//	public static final String HUNAN = "430000";//湖南
//	public static final String GUANGDONG = "440000";//广东
//	public static final String GUANGXI = "450000";//广西
//	public static final String HAINAN = "460000";//海南
//	public static final String CHONGQING = "500000";//重庆
//	public static final String SICHUAN = "510000";//四川
//	public static final String GUIZHOU = "520000";//贵州
//	public static final String YUNNAN = "530000";//云南
//	public static final String XIZANG = "540000";//西藏
//	public static final String SHAN_XI = "610000";//陕西
//	public static final String GANSU = "620000";//甘肃
//	public static final String QINGHAI = "630000";//青海
//	public static final String NINGXIA = "640000";//宁夏
//	public static final String XINJIANG = "650000";//新疆
//	public static final String TAIWAN = "710000";//台湾
//	public static final String XIANGGANG = "810000";//香港
//	public static final String AOMEN = "820000";//澳门
//	
//	
//	public static Map<String,String>area_nos = new LinkedHashMap<String,String>();
//	
//	public static Map<String,String>getAll_area_nos(){
//		if(area_nos.isEmpty()){
//			area_nos.put(BEIJING, "北京");
//			area_nos.put(TAINJIN, "天津");
//			area_nos.put(HEBEI, "河北");
//			area_nos.put(SHANXI, "山西");
//			area_nos.put(NEIMENG, "内蒙");
//			area_nos.put(LIAONING, "辽宁");
//			area_nos.put(JILIN, "吉林");
//			area_nos.put(HEILONGJIANG, "黑龙江");
//			area_nos.put(SHANGHAI, "上海");
//			area_nos.put(JIANGSU, "江苏");
//			area_nos.put(ZHEJIANG, "浙江");
//			area_nos.put(ANHUI, "安徽");
//			area_nos.put(FUJIAN, "福建");
//			area_nos.put(JIANGXI, "江西");
//			area_nos.put(SHENDONG, "山东");
//			area_nos.put(HENAN, "河南");
//			area_nos.put(HUBEI, "湖北");
//			area_nos.put(HUNAN, "湖南");
//			area_nos.put(GUANGDONG, "广东");
//			area_nos.put(GUANGXI, "广西");
//			area_nos.put(HAINAN, "海南");
//			area_nos.put(CHONGQING, "重庆");
//			area_nos.put(SICHUAN, "四川");
//			area_nos.put(GUIZHOU, "贵州");
//			area_nos.put(YUNNAN, "云南");
//			area_nos.put(XIZANG, "西藏");
//			area_nos.put(SHAN_XI, "陕西");
//			area_nos.put(GANSU, "甘肃");
//			area_nos.put(QINGHAI, "青海");
//			area_nos.put(NINGXIA, "宁夏");
//			area_nos.put(XINJIANG, "新疆");
//			area_nos.put(TAIWAN, "台湾");
//			area_nos.put(XIANGGANG, "香港");
//			area_nos.put(AOMEN, "澳门");
//		}
//		return area_nos;
//	}
	
	
	
	public static Map<String,String>user_Type = new LinkedHashMap<String,String>();
	
	public static Map<String,String>getUser_Types(){
		if(user_Type.isEmpty()){
			user_Type.put(COMMONLY, "普通用户");
			user_Type.put(SENIOR, "高级用户");
			user_Type.put(MANAGER, "管理员");
			user_Type.put(PRESIDE, "省级负责人");
			user_Type.put(KAOHE, "质检");
			user_Type.put(LEADER, "Leader");
			user_Type.put(USER, "User");
		
		}
		return user_Type;
	}
	
	public static final String STATUS_WAIT = "0";//等待审核
	public static final String STATUS_PASS = "1";//审核通过
	public static final String STATUS_NOT = "2";//审核未通过
	
	public static Map<String,String>user_Status = new LinkedHashMap<String,String>();
	
	public static Map<String,String>getUser_Status(){
		if(user_Status.isEmpty()){
			user_Status.put(STATUS_WAIT, "等待审核");
			user_Status.put(STATUS_PASS, "审核通过");
			user_Status.put(STATUS_NOT, "审核未通过");
		}
		return user_Status;
	}
	
	public static final String ISOPEN_YES = "0";//启用
	public static final String ISOPEN_NO = "1";//停用
	
	public static Map<String,String>user_ISOPEN = new LinkedHashMap<String,String>();
	
	public static Map<String,String>getUser_ISOPENS(){
		if(user_ISOPEN.isEmpty()){
			user_ISOPEN.put(ISOPEN_YES, "已启用");
			user_ISOPEN.put(ISOPEN_NO, "已停用");
		}
		return user_ISOPEN;
	}
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getUser_level() {
		return user_level;
	}

	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}

	public String getUser_IsOpen() {
		return user_IsOpen;
	}

	public void setUser_IsOpen(String user_IsOpen) {
		this.user_IsOpen = user_IsOpen;
	}

	public String getSupervise_name() {
		return supervise_name;
	}

	public void setSupervise_name(String supervise_name) {
		this.supervise_name = supervise_name;
	}

}
