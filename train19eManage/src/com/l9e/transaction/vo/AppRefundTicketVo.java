package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppRefundTicketVo {

	/*******************退款类型************************/
	//app退款类型：1、用户线上退票 2、改签差额退款 3、用户电话退票 4、差额退款 5、出票失败退款 6、改签单退款
	public static final String REFUND_USER = "1"; //用户线上退票
	public static final String REFUND_BALANCE ="2";//改签差额退款
	public static final String REFUND_PHONE = "3";//用户电话退票
	public static final String REFUND_CHAE = "4";//差额退款
	public static final String REFUND_FAIL = "5";//出票失败退款
	public static final String REFUND_GAIQIAN = "6";//改签单退款
	public static Map<String,String>REFUNDTYPE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getRefund_Types(){
		if(REFUNDTYPE_MAP.isEmpty()){
			REFUNDTYPE_MAP.put(REFUND_USER, "用户线上退票");
			REFUNDTYPE_MAP.put(REFUND_BALANCE, "改签差额退款");
			REFUNDTYPE_MAP.put(REFUND_PHONE, "用户电话退票");
			REFUNDTYPE_MAP.put(REFUND_CHAE, "差额退款");
			REFUNDTYPE_MAP.put(REFUND_FAIL, "出票失败退款");
			REFUNDTYPE_MAP.put(REFUND_GAIQIAN, "改签单退款");
		}
		return REFUNDTYPE_MAP ;
	}
	
	/*******************退款类型************************/
//	public static final String REFUND_USER = "1"; //用户退款
//	public static final String REFUND_BALANCE ="2";//差额退款
//	public static final String REFUND_PAY_DEFEATED ="3";//出票失败退款
//	public static final String REFUND_BALANCE_CHANGE ="4";//改签差额退款
//	public static final String REFUND_BALANCE_ODD ="5";//改签单退款
//	
//	
//	public static Map<String,String>REFUNDTYPE_MAP = new LinkedHashMap<String,String>();
//	public static Map<String,String> getRefund_Types(){
//		if(REFUNDTYPE_MAP.isEmpty()){
//			REFUNDTYPE_MAP.put(REFUND_USER, "用户退款");
//			REFUNDTYPE_MAP.put(REFUND_BALANCE, "差额退款");
//			REFUNDTYPE_MAP.put(REFUND_PAY_DEFEATED, "出票失败退款");
//			REFUNDTYPE_MAP.put(REFUND_BALANCE_CHANGE, "改签差额退款");
//			REFUNDTYPE_MAP.put(REFUND_BALANCE_ODD, "改签单退款");
//		}
//		return REFUNDTYPE_MAP ;
//	}
	
	/*******************退款状态************************/
	//退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败
	public static String REFUND_STATUS_PREPARE = "00";//等待退票
	public static String REFUND_STATUS_WAIT = "11";//同意退票
	public static String REFUND_STATUS_START = "22";//拒绝退票
	public static String REFUND_STATUS_FINISH = "33";//退票完成
	public static String REFUND_STATUS_FAIL = "44";//退票失败
	public static Map<String,String>REFUNDSTATUS_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getRefund_Status(){
		if(REFUNDSTATUS_MAP.isEmpty()){
			REFUNDSTATUS_MAP.put(REFUND_STATUS_PREPARE, "等待退票");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_WAIT, "同意退票");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_START, "拒绝退票");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_FINISH, "退票完成");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_FAIL, "退票失败");
		}
		return REFUNDSTATUS_MAP;
	}
	
	/*******************退款状态************************/
//	public static String REFUND_STATUS_PREPARE = "00";//准备退款
//	public static String REFUND_STATUS_WAIT = "11";//等待退款
//	public static String REFUND_STATUS_START = "22";//开始退款
//	public static String REFUND_STATUS_EOP = "33";//EOP退款中
//	public static String REFUND_STATUS_ACHIEVE ="44";//退款完成
//	public static String REFUND_STATUS_REFUSE = "55";//拒绝退款
//	
//	public static Map<String,String>REFUNDSTATUS_MAP = new LinkedHashMap<String,String>();
//	public static Map<String,String> getRefund_Status(){
//		if(REFUNDSTATUS_MAP.isEmpty()){
//			REFUNDSTATUS_MAP.put(REFUND_STATUS_PREPARE, "准备退款");
//			REFUNDSTATUS_MAP.put(REFUND_STATUS_WAIT, "等待退款");
//			REFUNDSTATUS_MAP.put(REFUND_STATUS_START, "开始退款");
//			//REFUNDSTATUS_MAP.put(REFUND_STATUS_EOP, "EOP退款中");
//			REFUNDSTATUS_MAP.put(REFUND_STATUS_ACHIEVE, "退款完成");
//			REFUNDSTATUS_MAP.put(REFUND_STATUS_REFUSE, "拒绝退款");
//		}
//		return REFUNDSTATUS_MAP;
//	}
	/*******************车票类型************************/
	public static final Integer TICKET_MAN = 0;//成人票
	public static final Integer TICKET_BABE = 1;//儿童票
	
	public static Map<Integer,String>TICKETTYPE_MAP = new LinkedHashMap<Integer,String>();
	public static Map<Integer,String> getTicket_Types(){
		if(TICKETTYPE_MAP.isEmpty()){
			TICKETTYPE_MAP.put(TICKET_MAN, "成人票");
			TICKETTYPE_MAP.put(TICKET_BABE, "儿童票");
		}
		return TICKETTYPE_MAP;
	}
	
	/*******************证件类型************************/
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
	
	/*******************座位类型************************/
	public static Integer SEAT_COMMERCE = 0;//商务座
	public static Integer SEAT_ESPECIALLY = 1;//特等座
	public static Integer SEAT_ONE_RANK = 2;//一等座
	public static Integer SEAT_TWO_RANK = 3;//二等座
	public static Integer ADVANCED_SOFT_SLEEPER = 4;//高级软卧
	public static Integer SOFT_SLEEPER = 5;//软卧
	public static Integer TOURIST_CAR = 6;//硬卧
	public static Integer SEAT_SOFT = 7;//软座
	public static Integer SEAT_HARD = 8;//硬座
	public static Integer SEAT_NO = 9;//无座
	public static Integer SEAT_OTHER = 10;//其他
	
	public static Map<Integer,String>SEAT_MAP = new LinkedHashMap<Integer,String>();
	public static Map<Integer,String> getSeat_type(){
		if(SEAT_MAP.isEmpty()){
			SEAT_MAP.put(SEAT_COMMERCE, "商务座");
			SEAT_MAP.put(SEAT_ESPECIALLY, "特等座");
			SEAT_MAP.put(SEAT_ONE_RANK, "一等座");
			SEAT_MAP.put(SEAT_TWO_RANK, "二等座");
			SEAT_MAP.put(ADVANCED_SOFT_SLEEPER, "高级软卧");
			SEAT_MAP.put(SOFT_SLEEPER, "软卧");
			SEAT_MAP.put(TOURIST_CAR, "硬卧");
			SEAT_MAP.put(SEAT_SOFT, "软座");
			SEAT_MAP.put(SEAT_HARD, "硬座");
			SEAT_MAP.put(SEAT_NO, "无座");
			SEAT_MAP.put(SEAT_OTHER, "其他");
		}
		return SEAT_MAP;
	}
	



}
