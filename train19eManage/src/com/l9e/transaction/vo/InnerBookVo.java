package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class InnerBookVo {
	public static final String PRE_ORDER = "00";// 预下单
	public static final String PAY_SUCCESS = "11";// 支付成功
	public static final String OUTING_TICKET = "22";// 正在预订
	public static final String OUT_FAIL = "33";// 预订成功
	public static final String OUT_SUCCESS = "44";// 出票成功
	public static final String OUT_TICKET_FAIL = "45";// 出票失败
	public static final String ORDER_FINISH = "88";// 订单完成
	public static final String PAY_FAIL = "99";// 支付失败
	
	public static final String OVERLOOK = "1";//无视截止时间
	public static final String NOT_OVERLOOK = "0";//不无视截止时间
	
	public static final String CHANNEL_1 ="19pay";
	public static final String CHANNEL_2 ="cmpay";
	public static final String CHANNEL_3 ="ccb";
	public static final String CHANNEL_4 ="chq";
	public static final String CHANNEL_5 ="cmwap";
	
	public static Integer IDS_1 =1;
	public static Integer IDS_2 =2;
	public static Integer IDS_3 =3;
	public static Integer IDS_4 =4;
	public static Integer IDS_5 =5;
	
	public static Integer TICKETTYPE_CHILDREN =1;
	public static Integer TICKETTYPE_ADULT =0;
	
	public static Integer SEAT_0 =0;
	public static Integer SEAT_1 =1;
	public static Integer SEAT_2 =2;
	public static Integer SEAT_3 =3;
	public static Integer SEAT_4 =4;
	public static Integer SEAT_5 =5;
	public static Integer SEAT_6 =6;
	public static Integer SEAT_7 =7;
	public static Integer SEAT_8 =8;
	public static Integer SEAT_9 =9;
	public static Integer SEAT_10 =10;
	public static Integer SEAT_20 =20;
	//支付方式: 11、联动优势支付；22、支付宝支付；33、微信支付
	public static final String PAY_TYPE_LIANDONG = "11";// 联动优势
	public static final String PAY_TYPE_ZHIFUBAO = "22";// 支付宝支付
	public static final String PAY_TYPE_WEIXIN = "33";// 微信支付
	//渠道：app   weixin  baidu
	public static final String CHANNEL_APP = "app";// APP
	public static final String CHANNEL_WEIXINP = "weixin";// 微信
	public static final String CHANNEL_BAIDU = "baidu";// 百度
	
	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> Channel = new LinkedHashMap<String, String>();
	private static Map<Integer, String> IDSTYPE = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> TICKETTYPE = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> SEATTTYPE = new LinkedHashMap<Integer, String>();
	private static Map<String, String> IGNORE = new LinkedHashMap<String,String>();
	private static Map<String, String> PAYTYPE = new LinkedHashMap<String,String>();
	private static Map<String, String> CHANNEL_TYPE = new LinkedHashMap<String, String>();
	
	public static Map<String,String> getchannel_type(){
		if(CHANNEL_TYPE.isEmpty()){
			CHANNEL_TYPE.put(CHANNEL_APP, "app");
			CHANNEL_TYPE.put(CHANNEL_WEIXINP, "微信");
		}
		return CHANNEL_TYPE;
	}
	
	public static Map<String,String> getPay_type(){
		if(PAYTYPE.isEmpty()){
			PAYTYPE.put(PAY_TYPE_LIANDONG, "联动优势");
			PAYTYPE.put(PAY_TYPE_ZHIFUBAO, "支付宝");
			PAYTYPE.put(PAY_TYPE_WEIXIN, "微信支付");
		}
		return PAYTYPE;
	}
	
	public static Map<String,String> getIgnore(){
		if(IGNORE.isEmpty()){
			IGNORE.put(OVERLOOK, "开启退款截止");
			IGNORE.put(NOT_OVERLOOK, "关闭退款截止");
		}
		return IGNORE;
	}
	
	public static Map<String, String> getBookStatus(){
		
		if(BOOKSTATUS.isEmpty()){
			BOOKSTATUS.put(PRE_ORDER, "预下单");
			BOOKSTATUS.put(PAY_SUCCESS, "支付成功");
			BOOKSTATUS.put(OUTING_TICKET, "正在预订");
			BOOKSTATUS.put(OUT_FAIL, "预订成功");
			BOOKSTATUS.put(OUT_SUCCESS, "出票成功");
			BOOKSTATUS.put(OUT_TICKET_FAIL, "出票失败");
			BOOKSTATUS.put(ORDER_FINISH, "订单完成");
			BOOKSTATUS.put(PAY_FAIL, "支付失败");
		}
		
		return BOOKSTATUS;
	}
	
public static Map<String, String> getChannel(){
		
		if(Channel.isEmpty()){
			
			Channel.put(CHANNEL_1, "19pay");
			Channel.put(CHANNEL_2, "cmpay");
			Channel.put(CHANNEL_5, "cmwap");
			Channel.put(CHANNEL_3, "建行");
			Channel.put(CHANNEL_3, "建行");
			Channel.put(CHANNEL_4, "春秋");
			
		}
		
		return Channel;
	}
	
	public static Map<Integer, String> getIdstype(){
		
		if(IDSTYPE.isEmpty()){
			IDSTYPE.put(IDS_1, "一代身份证");
			IDSTYPE.put(IDS_2, "二代身份证");
			IDSTYPE.put(IDS_3, "港澳通行证 ");
			IDSTYPE.put(IDS_4, "台湾通行证 ");
			IDSTYPE.put(IDS_5, "护照  ");
		}
		
		return IDSTYPE;
	}
	
	public static Map<Integer, String> getTicketType(){
		
		if(TICKETTYPE.isEmpty()){
			TICKETTYPE.put(TICKETTYPE_CHILDREN, "儿童票");
			TICKETTYPE.put(TICKETTYPE_ADULT, "成人票");
		
		}
		
		return TICKETTYPE;
	}
	
	
	public static Map<Integer, String> getSeattype(){
		//座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他
		if(SEATTTYPE.isEmpty()){
			SEATTTYPE.put(SEAT_0, "商务座 ");
			SEATTTYPE.put(SEAT_1, "特等座");
			SEATTTYPE.put(SEAT_2, "一等座");
			SEATTTYPE.put(SEAT_3, "二等座");
			SEATTTYPE.put(SEAT_4, "高级软卧");
			SEATTTYPE.put(SEAT_5, "软卧");
			SEATTTYPE.put(SEAT_6, "硬卧");
			SEATTTYPE.put(SEAT_7, "软座");
			SEATTTYPE.put(SEAT_8, "硬座");
			SEATTTYPE.put(SEAT_9, "无座");
			SEATTTYPE.put(SEAT_10, "其他 ");
			SEATTTYPE.put(SEAT_20, "动卧 ");
		}
		
		return SEATTTYPE;
	}
	
	/*******************退款类型************************/
	public static final String REFUND_USER = "1"; //用户退款
	public static final String REFUND_BALANCE ="2";//差额退款
	public static final String REFUND_PAY_DEFEATED ="3";//出票失败退款
	public static final String REFUND_BALANCE_CHANGE ="4";//改签差额退款
	public static final String REFUND_BALANCE_ODD ="5";//改签单退款
	public static final String REFUND_OFFLINE ="6";//线下退款
//	public static final String REFUND_CANCEL_BOOK ="7";//取消预约退款
	
	
	public static Map<String,String>REFUNDTYPE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> getRefund_Types(){
		if(REFUNDTYPE_MAP.isEmpty()){
			REFUNDTYPE_MAP.put(REFUND_USER, "用户退款");
			REFUNDTYPE_MAP.put(REFUND_BALANCE, "差额退款");
			REFUNDTYPE_MAP.put(REFUND_PAY_DEFEATED, "出票失败退款");
			REFUNDTYPE_MAP.put(REFUND_BALANCE_CHANGE, "改签差额退款");
			REFUNDTYPE_MAP.put(REFUND_BALANCE_ODD, "改签单退款");
			REFUNDTYPE_MAP.put(REFUND_OFFLINE, "线下退款");
	//		REFUNDTYPE_MAP.put(REFUND_CANCEL_BOOK, "取消预约退款");
		}
		return REFUNDTYPE_MAP ;
	}
}
