package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class TuniuRefundVo {

	/*******************退款类型************************/
	//退款类型 11、线上退款 22、线下退款（人工退款）33 车站退票 44 申请退款 55 改签退票
	//退款类型
	public static final String TYPE_11 = "11";//用户退款
	public static final String TYPE_22 = "22";//线下退款
	public static final String TYPE_33 = "33";//车站退票
	public static final String TYPE_44 = "44";//申请退票
	public static final String TYPE_55 = "55";//改签退票
	public static Map<String,String>REFUNDTYPE_MAP = new LinkedHashMap<String,String>();
	public static Map<String, String> getRefund_Types() {
		if(REFUNDTYPE_MAP.isEmpty()) {
			REFUNDTYPE_MAP.put(TYPE_11, "用户退款");
			REFUNDTYPE_MAP.put(TYPE_22, "线下退款");
			REFUNDTYPE_MAP.put(TYPE_33, "车站退票");
			REFUNDTYPE_MAP.put(TYPE_44, "申请退票");
			REFUNDTYPE_MAP.put(TYPE_55, "改签退票");
		}
		return REFUNDTYPE_MAP;
	}
	
	/*******************退款状态************************/
	
	//退款状态 
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票
	//06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款
	public static final String WAITREFUND = "00";//等待退票
	public static final String AGREEREFUND = "11";//同意退票
	public static final String REFUSEREFUND = "22"; //拒绝退票
	public static final String SUCCESS = "33";//退款成功
	public static final String REFUNDADDING = "71";//线下退款中
	public static final String REFUNDADD = "72";//审核线下退款
	public static final String REFUNDELONG = "73";//申请线下
	public static final String REROBOT = "01";//重新机器改签
	public static final String STARTROBOT = "02";//开始机器改签
	public static final String ROBOTGAIQIAN = "03";//机器改签失败 
	public static final String WAITROBOT = "04";//等待机器退票
	public static final String REROBOTTUI = "05";//重新机器退票
	public static final String STARTROBOTTUI = "06";//开始机器退票
	public static final String FAILROBOTTUI = "07";//机器退票失败
	public static final String REFUNDSTATION = "81";//车站退票
	private static Map<String, String> REFUNDSTATUS = new LinkedHashMap<String, String>();
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票
	//06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款
	public static Map<String, String> getRefund_Status() {
		if(REFUNDSTATUS.isEmpty()) {
			REFUNDSTATUS.put(WAITREFUND, "正在改签");
			REFUNDSTATUS.put(REROBOT, "正在改签");
			REFUNDSTATUS.put(STARTROBOT, "正在改签");
			REFUNDSTATUS.put(ROBOTGAIQIAN, "人工改签");
			REFUNDSTATUS.put(WAITROBOT, "正在退票");
			REFUNDSTATUS.put(REROBOTTUI, "正在退票");
			REFUNDSTATUS.put(STARTROBOTTUI, "正在退票");
			REFUNDSTATUS.put(FAILROBOTTUI, "人工退票");
			REFUNDSTATUS.put(AGREEREFUND, "退票完成");
			REFUNDSTATUS.put(REFUSEREFUND, "拒绝退票");
			REFUNDSTATUS.put(SUCCESS, "审核退款");
			REFUNDSTATUS.put(REFUNDADDING, "线下退款中");
			REFUNDSTATUS.put(REFUNDADD, "审核线下退款");
			REFUNDSTATUS.put(REFUNDELONG, "申请线下");
			REFUNDSTATUS.put(REFUNDSTATION, "车站退票");
		}
		return REFUNDSTATUS;
	}
	
	/*******************车票类型************************/
	public static final String TICKET_MAN = "0";// 成人票
	public static final String TICKET_BABE = "1";// 儿童票
	public static final String TICKET_STUDENT = "2";// 学生票
	
	public static Map<String,String>TICKETTYPE = new LinkedHashMap<String,String>();
	public static Map<String, String> getTicket_Types() {
		if (TICKETTYPE.isEmpty()) {
			TICKETTYPE.put(TICKET_MAN, "成人票");
			TICKETTYPE.put(TICKET_BABE, "儿童票");
			TICKETTYPE.put(TICKET_STUDENT, "学生票");
		}
		return TICKETTYPE;
	}
	
	/*******************证件类型************************/
	// 证件类型
	public static String IDS_1 = "1";
	public static String IDS_2 = "2";
	public static String IDS_3 = "3";
	public static String IDS_4 = "4";
	public static String IDS_5 = "5";

	private static Map<String, String> IDSTYPE = new LinkedHashMap<String, String>();

	public static Map<String, String> getIdstype() {

		if (IDSTYPE.isEmpty()) {
			IDSTYPE.put(IDS_1, "一代身份证");
			IDSTYPE.put(IDS_2, "二代身份证");
			IDSTYPE.put(IDS_3, "港澳通行证 ");
			IDSTYPE.put(IDS_4, "台湾通行证 ");
			IDSTYPE.put(IDS_5, "护照");
		}
		return IDSTYPE;
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
	//通知状态
//	结果通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败
	public static final String NOTIFY_00 = "00";
	public static final String NOTIFY_11 = "11";
	public static final String NOTIFY_22 = "22";
	public static final String NOTIFY_33 = "33";
	private static Map<String, String> NOTIFY_STATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getNotify_Status() {
		if(NOTIFY_STATUS.isEmpty()) {
			NOTIFY_STATUS.put(NOTIFY_00, "准备通知");
			NOTIFY_STATUS.put(NOTIFY_11, "开始通知");
			NOTIFY_STATUS.put(NOTIFY_22, "完成通知");
			NOTIFY_STATUS.put(NOTIFY_33, "通知失败");
		}
		return NOTIFY_STATUS;
	}

}
