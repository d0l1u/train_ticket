package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class RefundTicketVo {
	
	
	/*******************退款类型************************/
	public static final String REFUND_USER = "1"; //用户退款
	public static final String REFUND_BALANCE ="2";//差额退款
	public static final String REFUND_XIANXIA ="8";//线下退款
	public static final String REFUND_PAY_DEFEATED ="3";//出票失败退款
	public static final String REFUND_BALANCE_CHANGE ="4";//改签差额退款
	public static final String REFUND_BALANCE_ODD ="5";//改签单退款
	public static final String REFUND_TELEPHONE ="6";//电话退票
	public static final String REFUND_CANCEL_BOOK ="7";//取消预约退款
	public static final String REFUND_STATION ="33";//车站退票
	
	
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
			REFUNDTYPE_MAP.put(REFUND_STATION, "车站退票");
		}
		return REFUNDTYPE_MAP ;
	}
	
	/*******************退款状态************************/
	public static String REFUND_STATUS_PREPARE = "00";//准备退款
	//00、准备退款（机器改签） 01：重新机器改签;02：开始机器改签;03：机器改签失败;04：等待机器退票；05：重新机器退票；06：开始机器退票；07：机器退票失败；09: 审核退款；
	//11、等待退款 22、开始退款 33、EOP退款中 44、退款完成 55、拒绝退款 99、搁置订单
	public static String REFUND_STATUS_00 = "00";//
	public static String REFUND_STATUS_01 = "01";//
	public static String REFUND_STATUS_02 = "02";//
	public static String REFUND_STATUS_03 = "03";//
	public static String REFUND_STATUS_04 = "04";//
	public static String REFUND_STATUS_05 = "05";//
	public static String REFUND_STATUS_06 = "06";//
	public static String REFUND_STATUS_07 = "07";//
	public static String REFUND_STATUS_09 = "09";//
	public static String REFUND_STATUS_WAIT = "11";//等待退款
	public static String REFUND_STATUS_START = "22";//开始退款
	public static String REFUND_STATUS_EOP = "33";//EOP退款中
	public static String REFUND_STATUS_ACHIEVE ="44";//退款完成
	public static String REFUND_STATUS_REFUSE = "55";//拒绝退款
	public static String REFUND_STATUS_GEZHI = "99";//搁置订单
	public static String REFUND_STATUS_OFFLINE_EXAMINE = "66";//线下退款审核
	public static String REFUND_STATUS_OFFLINEING = "77";//线下退款中
	public static String REFUND_STATUS_FAIL = "34";//退款失败
	
	public static Map<String,String>REFUNDSTATUS_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getRefund_Status(){
		if(REFUNDSTATUS_MAP.isEmpty()){
			REFUNDSTATUS_MAP.put(REFUND_STATUS_00, "准备退款");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_01, "正在改签");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_02, "正在改签");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_03, "人工改签");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_04, "正在退票");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_05, "正在退票");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_06, "正在退票");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_07, "人工退票");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_09, "审核退款");
//			REFUNDSTATUS_MAP.put(REFUND_STATUS_PREPARE, "准备退款");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_WAIT, "等待退款");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_START, "开始退款");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_EOP, "EOP退款中");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_ACHIEVE, "退款完成");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_REFUSE, "拒绝退款");
			REFUNDSTATUS_MAP.put(REFUND_STATUS_GEZHI, "搁置订单");
		}
		return REFUNDSTATUS_MAP;
	}
	
	public static Map<String,String>REFUNDSTATUSMAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getRefundStatus(){
		if(REFUNDSTATUSMAP.isEmpty()){
			REFUNDSTATUSMAP.put(REFUND_STATUS_00, "准备退款");
			REFUNDSTATUSMAP.put(REFUND_STATUS_03, "人工改签");
			REFUNDSTATUSMAP.put(REFUND_STATUS_07, "人工退票");
			REFUNDSTATUSMAP.put(REFUND_STATUS_WAIT, "等待退款");
			REFUNDSTATUSMAP.put(REFUND_STATUS_START, "开始退款");
			REFUNDSTATUSMAP.put(REFUND_STATUS_EOP, "EOP退款中");
			REFUNDSTATUSMAP.put(REFUND_STATUS_ACHIEVE, "退款完成");
			REFUNDSTATUSMAP.put(REFUND_STATUS_REFUSE, "拒绝退款");
			REFUNDSTATUSMAP.put(REFUND_STATUS_09, "审核退款");
			REFUNDSTATUSMAP.put(REFUND_STATUS_GEZHI, "搁置订单");
		}
		return REFUNDSTATUSMAP;
	}
	
	public static Map<String,String>CMREFUNDSTATUS_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getCmRefund_Status(){
		if(CMREFUNDSTATUS_MAP.isEmpty()){
			CMREFUNDSTATUS_MAP.put(REFUND_STATUS_PREPARE, "准备退款");
			CMREFUNDSTATUS_MAP.put(REFUND_STATUS_WAIT, "等待退款");
			CMREFUNDSTATUS_MAP.put(REFUND_STATUS_START, "开始退款");
			CMREFUNDSTATUS_MAP.put(REFUND_STATUS_EOP, "cmpay退款中");
			CMREFUNDSTATUS_MAP.put(REFUND_STATUS_ACHIEVE, "退款完成");
			CMREFUNDSTATUS_MAP.put(REFUND_STATUS_REFUSE, "拒绝退款");
		}
		return CMREFUNDSTATUS_MAP;
	}
	
	public static Map<String,String>INNERREFUNDSTATUS_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getInnerRefund_Status(){
		if(INNERREFUNDSTATUS_MAP.isEmpty()){
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_PREPARE, "准备退款");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_WAIT, "等待退款");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_START, "开始退款");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_EOP, "退款中");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_ACHIEVE, "退款完成");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_REFUSE, "拒绝退款");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_OFFLINE_EXAMINE, "线下退款审核");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_OFFLINEING, "线下退款中");
			INNERREFUNDSTATUS_MAP.put(REFUND_STATUS_FAIL, "退款失败");
		}
		return INNERREFUNDSTATUS_MAP;
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
	
	/*******************拒绝退款备注************************/
	private static final String GOTO_STATION = "1";
	private static final String REFUND_REMARKS = "2";
	private static final String NO_REFUND = "3";
	private static final String AGENT_CANCLE = "4";
	private static final String OTHER_REMARKS = "5";
	
	public static Map<String, String> OURREMARK_MAP = new LinkedHashMap<String, String>();
	public static Map<String, String> getOur_remark(){
		if(OURREMARK_MAP.isEmpty()){
			OURREMARK_MAP.put(GOTO_STATION, "已无法在网上办理退票，请自行去车站办理");
			OURREMARK_MAP.put(REFUND_REMARKS, "请先到车站办理退票，然后填写用户备注为：已退票，再提交退款。");
			OURREMARK_MAP.put(NO_REFUND, "已与铁道部核实，乘客未退票");
			OURREMARK_MAP.put(AGENT_CANCLE, "用户来电取消退票");
			OURREMARK_MAP.put(OTHER_REMARKS, "其它");
		}
		return OURREMARK_MAP;
	}
	
	
	/*******************退款渠道备注************************/
	private static Map<String, String> Channel = new LinkedHashMap<String, String>();
	public static final String CHANNEL_1 ="19pay";
	public static final String CHANNEL_2 ="cmpay";
	public static final String CHANNEL_3 ="ccb";
	
	public static Map<String, String> getChannel(){
		
		if(Channel.isEmpty()){
			
			Channel.put(CHANNEL_1, "19pay");
			Channel.put(CHANNEL_2, "cmpay");
			Channel.put(CHANNEL_3, "建行");
			
		}
		
		return Channel;
	}
	
	/*******************返回日志************************/
	/**
	 * 00、已出票;11、系统异常;22、查询异常;33、任务超时;44、改签超时;55、未完成单;66、大于原价;77、任务超时;88、已改签;99、改签网址;
	 * 01、非法席别;02、网络繁忙;03、继续支付;04、改签失败
	 * */
	public static String OPT_log_00 = "00";
	public static String OPT_log_11 = "11";
	public static String OPT_log_22 = "22";
	public static String OPT_log_33 = "33";
	public static String OPT_log_44 = "44";
	public static String OPT_log_55 = "55";
	public static String OPT_log_66 = "66";
	public static String OPT_log_77 = "77";
	public static String OPT_log_88 = "88";
	public static String OPT_log_99 = "99";
	public static String OPT_log_01 = "01";
	public static String OPT_log_02 = "02";
	public static String OPT_log_03 = "03";
	public static String OPT_log_04 = "04";
	
	private static Map<String, String>  RETURNOPTLOG = new LinkedHashMap<String, String>();
	public static Map<String, String> getReturnOptlog(){
		if(RETURNOPTLOG.isEmpty()){
			RETURNOPTLOG.put(OPT_log_00, "已出票 ");
			RETURNOPTLOG.put(OPT_log_11 , "系统异常");
			RETURNOPTLOG.put(OPT_log_22 , "查询异常");
			RETURNOPTLOG.put(OPT_log_33 , "任务超时");
			RETURNOPTLOG.put(OPT_log_44 , "改签超时");
			RETURNOPTLOG.put(OPT_log_55 , "未完成单");
			RETURNOPTLOG.put(OPT_log_66 , "大于原价");
			RETURNOPTLOG.put(OPT_log_77 , "任务超时");
			RETURNOPTLOG.put(OPT_log_88 , "已改签");
			RETURNOPTLOG.put(OPT_log_99 , "改签网址");
			RETURNOPTLOG.put(OPT_log_01 , "非法席别");
			RETURNOPTLOG.put(OPT_log_02 , "网络繁忙");
			RETURNOPTLOG.put(OPT_log_03 , "继续支付");
			RETURNOPTLOG.put(OPT_log_04 , "改签失败");
			
		}
		return RETURNOPTLOG;
	}
	
	
	
}
