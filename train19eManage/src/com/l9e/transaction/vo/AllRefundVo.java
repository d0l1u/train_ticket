package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class AllRefundVo {
	//通知状态
	public static final Integer NOTIFY_0 = 0;//未通知
	public static final Integer NOTIFY_1 = 1;//等待通知
	public static final Integer NOTIFY_2 = 2;//正在通知
	public static final Integer NOTIFY_3 = 3;//通知成功
	public static final Integer NOTIFY_4 = 4;//通知失败
	public static final Integer NOTIFY_5 = 5;//重新通知
	
	/*******************通知状态************************/
	
	private static Map<Integer, String> NOTIFY_STATUS = new LinkedHashMap<Integer, String>();
	public static Map<Integer, String> getNotify_Status() {
		if(NOTIFY_STATUS.isEmpty()) {
			NOTIFY_STATUS.put(NOTIFY_0, "未通知");
			NOTIFY_STATUS.put(NOTIFY_1, "等待通知");
			NOTIFY_STATUS.put(NOTIFY_2, "正在通知");
			NOTIFY_STATUS.put(NOTIFY_3, "通知成功");
			NOTIFY_STATUS.put(NOTIFY_4, "通知失败");
			NOTIFY_STATUS.put(NOTIFY_5, "重新通知");
		}
		return NOTIFY_STATUS;
	}
	
	/*******************退款类型************************/
	public static final String REFUND_USER = "1"; //用户退款
	public static final String REFUND_BALANCE ="2";//差额退款
	public static final String REFUND_XIANXIA ="8";//线下退款
	public static final String REFUND_PAY_DEFEATED ="3";//出票失败退款
	public static final String REFUND_BALANCE_CHANGE ="4";//改签差额退款
	public static final String REFUND_BALANCE_ODD ="5";//改签单退款
	public static final String REFUND_TELEPHONE ="6";//电话退票
	public static final String REFUND_CANCEL_BOOK ="7";//取消预约退款
	
	
	public static Map<String,String>REFUNDTYPE_MAP = new LinkedHashMap<String,String>();
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
	
	public static Map<Integer,String>TICKETTYPE_MAP = new LinkedHashMap<Integer,String>();
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
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票
	//06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款
	public static final String WAITREFUND = "00";//等待退票
	public static final String AGREEREFUND = "11";//同意退票
	public static final String REFUSEREFUND = "22"; //拒绝退票
	public static final String SUCCESS = "33";//退款成功
	public static final String PHONECHECK = "44";//发起核验
	public static final String CHECKING = "45";//正在核验
	
	public static final String GEZHI = "99";//搁置订单
	public static final String REROBOT = "01";//重新机器改签
	public static final String STARTROBOT = "02";//开始机器改签
	public static final String ROBOTGAIQIAN = "03";//机器改签失败 
	public static final String WAITROBOT = "04";//等待机器退票
	public static final String REROBOTTUI = "05";//重新机器退票
	public static final String STARTROBOTTUI = "06";//开始机器退票
	public static final String FAILROBOTTUI = "07";//机器退票失败
	public static final String STARTRSHENHE = "08";//开始审核
	public static final String FAILSHENHE = "09";//正在审核
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票
	//06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款 44 手机核验
	private static Map<String, String> REFUNDSTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getRefundStatus() {
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
			REFUNDSTATUS.put(GEZHI, "搁置订单");
			REFUNDSTATUS.put(STARTRSHENHE, "正在审核");
			REFUNDSTATUS.put(FAILSHENHE, "正在审核");
			REFUNDSTATUS.put(PHONECHECK, "发起核验");
			REFUNDSTATUS.put(CHECKING, "正在核验");
		}
		return REFUNDSTATUS;
	}
	
	
}
