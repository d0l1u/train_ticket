package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class JDRefundVo {
	
	/*******************退款类型************************/
	public static final String REFUND_USER = "1"; //用户退款
	public static final String REFUND_BALANCE ="2";//差额退款
	public static final String REFUND_XIANXIA ="8";//线下退款
	public static final String REFUND_PAY_DEFEATED ="3";//出票失败退款
	public static final String REFUND_BALANCE_CHANGE ="4";//改签差额退款
	public static final String REFUND_BALANCE_ODD ="5";//改签单退款
	public static final String REFUND_TELEPHONE ="6";//电话退票
	public static final String REFUND_CANCEL_BOOK ="7";//取消预约退款
	
	
	public static Map<String,String> REFUNDTYPE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getRefund_Types(){
		if(REFUNDTYPE_MAP.isEmpty()){
			REFUNDTYPE_MAP.put(REFUND_USER, "用户退款");
			REFUNDTYPE_MAP.put(REFUND_BALANCE, "差额退款");
			REFUNDTYPE_MAP.put(REFUND_XIANXIA, "线下退款");
			REFUNDTYPE_MAP.put(REFUND_PAY_DEFEATED, "出票失败退款");
			REFUNDTYPE_MAP.put(REFUND_BALANCE_CHANGE, "改签差额退款");
			REFUNDTYPE_MAP.put(REFUND_BALANCE_ODD, "改签单退款");
			REFUNDTYPE_MAP.put(REFUND_TELEPHONE, "电话退票");
			REFUNDTYPE_MAP.put(REFUND_CANCEL_BOOK, "取消预约退款");
		}
		return REFUNDTYPE_MAP ;
	}
	
	/*******************车票类型************************/
	public static final Integer TICKET_MAN = 0;//成人票
	public static final Integer TICKET_BABE = 1;//儿童票
	public static final Integer TICKET_STUDENT = 2;//学生票
	
	public static Map<Integer,String> TICKETTYPE_MAP = new LinkedHashMap<Integer,String>();
	public static Map<Integer,String> getTicket_Types(){
		if(TICKETTYPE_MAP.isEmpty()){
			TICKETTYPE_MAP.put(TICKET_MAN, "成人票");
			TICKETTYPE_MAP.put(TICKET_BABE, "儿童票");
			TICKETTYPE_MAP.put(TICKET_STUDENT, "学生票");
		}
		return TICKETTYPE_MAP;
	}
	
	/*******************证件类型************************/
	public static final Integer IDENTITY_CARD_ONE = 1;//一代身份证
	public static final Integer IDENTITY_CARD_TWO = 2;//二代身份证
	public static final Integer HONGKONG_MACAO_PASS_CARD = 3;//港澳通行证
	public static final Integer TAIWAN_PASS_CARD = 4;//台湾通行证
	public static final Integer PASSPORT = 5;//护照
	
	public static Map<Integer,String> CERTIFICATE_MAP = new LinkedHashMap<Integer,String>();
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
	
	public static Map<Integer,String> SEAT_MAP = new LinkedHashMap<Integer,String>();
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
	
	/*******************拒绝退款原因************************/
	//通用
	public static final String TICKETOUT = "1";//已取票
	public static final String TIMEOUT = "2";//已过时间
	public static final String PHONECALL = "3"; //电话取消
	public static final String FENGHAO = "4"; //账号被封
	public static final String MONEYFAIL = "6"; //退款金额有损失（只针对去哪）
	public static final String CANNOT = "7"; //不可退票
	private static Map<String, String> REFUSEREASON = new LinkedHashMap<String, String>();
	//途牛
	public static final String TN_1 = "1";//已取票
	public static final String TN_2 = "2";//已过时间
	public static final String TN_3 = "3"; //电话取消
	public static final String TN_4= "4"; //账号被封
	public static final String TN_7 = "7"; //不可退票
	public static final String TN_8 = "8"; //12306账号密码错误
	public static final String TN_9 = "9"; //12306账号已被锁定，请稍后再试
	private static Map<String, String> TN_REFUSEREASON = new LinkedHashMap<String, String>();
	//同程
	public static final String TC_REASON_31 = "31";//已改签
	public static final String TC_REASON_32 = "32";//已退票
	public static final String TC_REASON_33 = "33";//已出票，只能在窗口办理退票
	public static final String TC_REASON_3 = "3";//未查询到该车票
	public static final String TC_REASON_9 = "9";//退票操作异常，请与客服联系
	public static final String TC_REASON_40 = "40";//订单所在账号被封
	public static final String TC_REASON_41 = "41";//无法在线退票_XXX地区处于旅游旺季，无法在线退票
	private static Map<String, String> TC_REFUSEREASON = new LinkedHashMap<String, String>();
	//高铁管家
	public static final String GT_REASON_2001 = "2001";//车票过期 或者12306限制的热门车次不能网上退票
	public static final String GT_REASON_2002 = "2002";//抱歉，退票失败，请前往就近火车站售票窗口办理。
	public static final String GT_REASON_2005 = "2005";//已取票
	private static Map<String, String> GT_REFUSEREASON = new LinkedHashMap<String, String>();
	//美团
	public static final String MT_REASON_3 = "3";//未查询到该车票
	public static final String MT_REASON_9 = "9";//退票操作异常，请与客服联系
	public static final String MT_REASON_31 = "31";//已改签
	public static final String MT_REASON_32 = "32";//已退票 
	public static final String MT_REASON_33 = "33";//已出票，只能在窗口办理退票 
	public static final String MT_REASON_39 = "39";//不可退票
	private static Map<String, String> MT_REFUSEREASON = new LinkedHashMap<String, String>();
	static{
		REFUSEREASON.put(TICKETOUT, "已取票");
		REFUSEREASON.put(TIMEOUT, "已过时间");
		REFUSEREASON.put(PHONECALL, "来电取消");
		REFUSEREASON.put(FENGHAO, "账号被封");
		REFUSEREASON.put(MONEYFAIL, "退款金额有损失");
		REFUSEREASON.put(CANNOT, "不可退票");
		
		TN_REFUSEREASON.put(TN_1, "已取票");
		TN_REFUSEREASON.put(TN_2, "已过时间");
		TN_REFUSEREASON.put(TN_3, "来电取消");
		TN_REFUSEREASON.put(TN_4, "账号被封");
		TN_REFUSEREASON.put(TN_7, "不可退票");
		TN_REFUSEREASON.put(TN_8, "12306账号密码错误");
		TN_REFUSEREASON.put(TN_9, "12306账号已被锁定，请稍后再试");
		
		TC_REFUSEREASON.put(TC_REASON_33, "已出票，只能在窗口办理退票");
		TC_REFUSEREASON.put(TC_REASON_32, "已退票");
		TC_REFUSEREASON.put(TC_REASON_31, "已改签");
		TC_REFUSEREASON.put(TC_REASON_3, "未查询到该车票");
		TC_REFUSEREASON.put(TC_REASON_9, "退票操作异常");
		TC_REFUSEREASON.put(TC_REASON_40, "订单所在账号被封");
		TC_REFUSEREASON.put(TC_REASON_41, "旅游旺季，无法在线退票");
		
		GT_REFUSEREASON.put(GT_REASON_2001, "车票过期 或者12306限制的热门车次不能网上退票");
		GT_REFUSEREASON.put(GT_REASON_2002, "抱歉，退票失败，请前往就近火车站售票窗口办理。");
		GT_REFUSEREASON.put(GT_REASON_2005, "已取票。");
		
		MT_REFUSEREASON.put(MT_REASON_3, "未查询到该车票");
		MT_REFUSEREASON.put(MT_REASON_9, "退票操作异常，请与客服联系");
		MT_REFUSEREASON.put(MT_REASON_31, "已改签");
		MT_REFUSEREASON.put(MT_REASON_32, "已退票 ");
		MT_REFUSEREASON.put(MT_REASON_33, "已出票，只能在窗口办理退票 ");
		MT_REFUSEREASON.put(MT_REASON_39, "不可退票");
	}

	private static Map<String, Map<String,String>> REFUSEREASON_MAP =  new LinkedHashMap<String,  Map<String,String>>();
	public static Map<String, Map<String,String>> getRefuseReason(){
		REFUSEREASON_MAP.put("OTHER",REFUSEREASON);
		REFUSEREASON_MAP.put("tongcheng",TC_REFUSEREASON);
		REFUSEREASON_MAP.put("301030",GT_REFUSEREASON);
		REFUSEREASON_MAP.put("meituan",MT_REFUSEREASON);
		REFUSEREASON_MAP.put("tuniu",TN_REFUSEREASON);
		return REFUSEREASON_MAP;
	}
	
	
	
	/******************退款状态 ***********************/
	//退票状态 00、开始退票  01、重发退票   02、正在退票   04：开始退票结果查询  05：退票结果查询重发 
	//      06：退票结果查询中  09：退票人工  10、退票失败   11、退票成功 
	public static String STATUS_REFUND_START= "00";//开始退票
	public static String STATUS_REFUND_RESEND= "01";//重发退票
	public static String STATUS_REFUND_ING= "02";//正在退票
	
	public static String STATUS_REFUND_QUERY_START= "04";//开始退票结果查询
	public static String STATUS_REFUND_QUERY_RESEND= "05";//退票结果查询重发
	public static String STATUS_REFUND_QUERY_ING= "06";//退票结果查询中

	public static String STATUS_REFUND_MANUAL= "09";//退票人工
	public static String STATUS_REFUND_FAILURE="10";//10退票失败
	public static String STATUS_REFUND_SUCCESS="11";//11退票完成

	private static Map<String, String> REFUNDSTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getRefundStatus() {
		if(REFUNDSTATUS.isEmpty()) {
			REFUNDSTATUS.put(STATUS_REFUND_START, "开始退票");
			REFUNDSTATUS.put(STATUS_REFUND_RESEND, "重发退票");
			REFUNDSTATUS.put(STATUS_REFUND_ING, "正在退票");
			REFUNDSTATUS.put(STATUS_REFUND_QUERY_START, "开始退票结果查询");
			REFUNDSTATUS.put(STATUS_REFUND_QUERY_RESEND, "退票结果查询重发");
			REFUNDSTATUS.put(STATUS_REFUND_QUERY_ING, "退票结果查询中");
			REFUNDSTATUS.put(STATUS_REFUND_MANUAL, "人工退票");
			REFUNDSTATUS.put(STATUS_REFUND_FAILURE, "退票失败");
			REFUNDSTATUS.put(STATUS_REFUND_SUCCESS, "退票完成");
		}
		return REFUNDSTATUS;
	}
	
	
}
