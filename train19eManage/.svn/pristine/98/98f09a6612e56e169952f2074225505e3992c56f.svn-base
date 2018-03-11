package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExtBookVo {
	public static final String PRE_ORDER = "00";// 预下单
	public static final String PAY_SUCCESS = "11";// 支付成功
	public static final String OUTING_TICKET = "22";// 正在预订
	public static final String OUT_FAIL = "33";// 预订成功
	public static final String OUT_SUCCESS = "44";// 出票成功
	public static final String OUT_TICKET_FAIL = "45";// 出票失败
	public static final String ORDER_FINISH = "88";// 订单完成
	public static final String PAY_FAIL = "99";// 支付失败
	public static final String CANCEL_SUCCESS = "24";//取消成功
	public static final String ORDER_OUT_TIME = "80";//超时订单
	
	public static final String OVERLOOK = "1";//无视截止时间
	public static final String NOT_OVERLOOK = "0";//不无视截止时间
	
	
	
	public static Integer IDS_1 =1;
	public static Integer IDS_2 =2;
	public static Integer IDS_3 =3;
	public static Integer IDS_4 =4;
	public static Integer IDS_5 =5;
	
	public static Integer TICKETTYPE_STUDENT =2;
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
	public static Integer SEAT_20 = 20;

	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();
	private static Map<Integer, String> IDSTYPE = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> TICKETTYPE = new LinkedHashMap<Integer, String>();
	private static Map<Integer, String> SEATTTYPE = new LinkedHashMap<Integer, String>();
	private static Map<String, String> IGNORE = new LinkedHashMap<String,String>();
	
	
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
			BOOKSTATUS.put(CANCEL_SUCCESS, "取消成功");
			BOOKSTATUS.put(ORDER_OUT_TIME, "超时订单");
		}
		
		return BOOKSTATUS;
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
			TICKETTYPE.put(TICKETTYPE_STUDENT, "学生票");
		
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
	
	//渠道
	private static Map<String, String> Channel = new LinkedHashMap<String, String>();
	public static Map<String, String> getChannel(){
			if(Channel.isEmpty()){
			}
			return Channel;
		}
	//通知状态：00、准备通知 11、开始通知 22、通知完成
	public static final String NOTIFY_00 = "00";//准备通知
	public static final String NOTIFY_11 = "11";//开始通知
	public static final String NOTIFY_22 = "22";//开始通知
	public static final String NOTIFY_44 = "44";//通知失败
	private static Map<String, String> NOTIFY_STATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getNotify_Status() {
		if(NOTIFY_STATUS.isEmpty()) {
			NOTIFY_STATUS.put(NOTIFY_00, "准备通知");
			NOTIFY_STATUS.put(NOTIFY_11, "开始通知");
			NOTIFY_STATUS.put(NOTIFY_22, "通知完成");
			NOTIFY_STATUS.put(NOTIFY_44, "通知失败");
		}
		return NOTIFY_STATUS;
	}
}
