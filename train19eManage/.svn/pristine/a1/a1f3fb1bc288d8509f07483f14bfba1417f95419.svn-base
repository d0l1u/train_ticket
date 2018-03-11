package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;


public class JoinUsVo {
	private  String user_id ;
	private  String city_id ;
	private  String province_id ;
	private  String district_id ;
	private  String apply_time ;
	private  String user_name ;
	private  String user_phone ;
	private  String user_qq ;
	private  String user_address ;	
	private  String user_level ;
	private  String begin_time ;
	private  String end_time ;
	private  String beginInfo_time ;
	private  String endInfo_time ;
	private  String create_time ;
	private  String last_create_time;
	private  String opt_person ;
	private  String jm_status ;
	private  String estate ;
	private  String product_id ;
	private  String jm_order_id ;
	private  String order_id ;
	private  String order_status ;
	private  String auditing_time;
	private  String shop_name;
	private  String shop_short_name;
	private  String shop_type;
	private  String agent_grade;//代理商等级
	

	

//	public static String NEEDPAY="00" ; //需要付费
//	public static String WAIT="11" ; //等待审核
//	public static String DOESNOT="22" ;//审核未通过
//	public static String PASS="33" ;//审核通过
//	public static String REPAY ="44" ; //需要续费
	
	public static String WAIT="11" ; //等待审核
	public static String DOESNOT="22" ;//未通过
	public static String PASS="33" ;//已通过

	public static String COMMON="0" ; // 普通用户
	public static String VIP="1" ; // VIP用户
	public static String FREE = "2"; //免费用户
	
	public static String SHOP_PAY_PHONE = "0"; //手机充值店
	public static String SHOP_FLOWER = "1"; //鲜花礼品店
	public static String SMALL_SUPERMARKET ="2"; //小型超市
	public static String BIG_SUPERMARKET = "3"; //大型超市
	public static String CIGARETTES_AND_WINE= "4"; //烟酒店
	public static String THE_PRESS = "5";//报刊亭
	public static String TICKET_OUTLETS = "6";//票务代售点
	public static String LOTTERY_OUTLETS = "7";//彩票代售点
	public static String TRAVEL_AGENCY = "8"; //旅行社
	public static String OTHER_STYLE = "9"; //其他
	public static String NET_HOME = "10";//网吧
	
	public static String JINPAI="11" ; //金牌用户
	public static String YINPAI="22" ; // 银牌用户
	public static String TONGPAI = "33"; //铜牌用户
	public static String PUTONG = "44"; //普通用户
	
	public JoinUsVo(){}
	
	public static Map<String,String>ESTAT = new LinkedHashMap<String,String>() ;
	public static Map<String,String>LEVEL = new LinkedHashMap<String,String>() ;
	public static Map<String,String>SHOP_TYPE = new LinkedHashMap<String,String>() ;
	public static Map<String,String>AGENT_GRADE = new LinkedHashMap<String,String>() ;
	public static Map<String,String>getEestat(){
		if(ESTAT.isEmpty()){
//			ESTAT.put(NEEDPAY, "需要付费") ;
//			ESTAT.put(WAIT, "等待审核") ;
//			ESTAT.put(DOESNOT, "未通过") ;
//			ESTAT.put(PASS, "审核通过");
//			ESTAT.put(REPAY, "需要续费") ;
			ESTAT.put(WAIT, "等待审核") ;
			ESTAT.put(DOESNOT, "未通过") ;
			ESTAT.put(PASS, "已通过");
		}
		return ESTAT ;
	}

	
	public static Map<String,String>getLevel(){
		if(LEVEL.isEmpty()){
			LEVEL.put(COMMON, "普通用户");
			LEVEL.put(VIP, "VIP用户") ;
			LEVEL.put(FREE, "免费用户") ;
		}
		return LEVEL ;
	}
	public static Map<String,String>getShop_Type(){
		if(SHOP_TYPE.isEmpty()){
			SHOP_TYPE.put(SHOP_PAY_PHONE, "手机充值店");
			SHOP_TYPE.put(SHOP_FLOWER, "鲜花礼品店");
			SHOP_TYPE.put(SMALL_SUPERMARKET, "小型超市");
			SHOP_TYPE.put(BIG_SUPERMARKET, "大型超市");
			SHOP_TYPE.put(CIGARETTES_AND_WINE, "烟酒店");
			SHOP_TYPE.put(THE_PRESS, "报刊亭");
			SHOP_TYPE.put(TICKET_OUTLETS, "票务代售点");
			SHOP_TYPE.put(LOTTERY_OUTLETS, "彩票代售点");
			SHOP_TYPE.put(TRAVEL_AGENCY, "旅行社");
			SHOP_TYPE.put(NET_HOME, "网吧");
			SHOP_TYPE.put(OTHER_STYLE, "其他");
		}
		return SHOP_TYPE;
	}
	
	public static Map<String,String>getAgent_Grade(){
		if(AGENT_GRADE.isEmpty()){
			AGENT_GRADE.put(JINPAI, "金牌用户");
			AGENT_GRADE.put(YINPAI, "银牌用户");
			AGENT_GRADE.put(TONGPAI, "铜牌用户");
			AGENT_GRADE.put(PUTONG, "普通用户");
			
		}
		return AGENT_GRADE;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getCity_id() {
		return city_id;
	}


	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}


	public String getProvince_id() {
		return province_id;
	}


	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}


	public String getDistrict_id() {
		return district_id;
	}


	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}


	public String getApply_time() {
		return apply_time;
	}


	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}


	public String getUser_name() {
		return user_name;
	}


	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	public String getUser_phone() {
		return user_phone;
	}


	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}


	public String getUser_qq() {
		return user_qq;
	}


	public void setUser_qq(String user_qq) {
		this.user_qq = user_qq;
	}


	public String getUser_address() {
		return user_address;
	}


	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}


	public String getUser_level() {
		return user_level;
	}


	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}


	public String getBegin_time() {
		return begin_time;
	}


	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}


	public String getEnd_time() {
		return end_time;
	}


	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}


	public String getBeginInfo_time() {
		return beginInfo_time;
	}


	public void setBeginInfo_time(String beginInfo_time) {
		this.beginInfo_time = beginInfo_time;
	}


	public String getEndInfo_time() {
		return endInfo_time;
	}


	public void setEndInfo_time(String endInfo_time) {
		this.endInfo_time = endInfo_time;
	}


	public String getCreate_time() {
		return create_time;
	}


	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}


	public String getLast_create_time() {
		return last_create_time;
	}


	public void setLast_create_time(String last_create_time) {
		this.last_create_time = last_create_time;
	}


	public String getOpt_person() {
		return opt_person;
	}


	public void setOpt_person(String opt_person) {
		this.opt_person = opt_person;
	}


	public String getJm_status() {
		return jm_status;
	}


	public void setJm_status(String jm_status) {
		this.jm_status = jm_status;
	}


	public String getEstate() {
		return estate;
	}


	public void setEstate(String estate) {
		this.estate = estate;
	}


	public String getProduct_id() {
		return product_id;
	}


	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}


	public String getJm_order_id() {
		return jm_order_id;
	}


	public void setJm_order_id(String jm_order_id) {
		this.jm_order_id = jm_order_id;
	}


	public String getOrder_id() {
		return order_id;
	}


	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}


	public String getOrder_status() {
		return order_status;
	}


	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}


	public String getAuditing_time() {
		return auditing_time;
	}


	public void setAuditing_time(String auditing_time) {
		this.auditing_time = auditing_time;
	}


	public String getShop_name() {
		return shop_name;
	}


	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}


	public String getShop_short_name() {
		return shop_short_name;
	}


	public void setShop_short_name(String shop_short_name) {
		this.shop_short_name = shop_short_name;
	}


	public String getShop_type() {
		return shop_type;
	}


	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}


	public String getAgent_grade() {
		return agent_grade;
	}


	public void setAgent_grade(String agentGrade) {
		agent_grade = agentGrade;
	}
	









	
	
	
}
