package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class TuniuBookVo {
	/*
	 * 订单状态: 00下单成功 11通知出票成功 22预订成功 32、 正在出票 33出票成功 44出票失败 51撤销中 52撤销失败 88 超时订单 23、正在取消 24 取消成功
	 */
	public static final String STATUS_00 = "00";
	public static final String STATUS_11 = "11";
	public static final String STATUS_22 = "22";
	public static final String STATUS_32 = "32";
	public static final String STATUS_33 = "33";
	public static final String STATUS_44 = "44";
	public static final String STATUS_51 = "51";
	public static final String STATUS_52 = "52";
	public static final String STATUS_88 = "88";
	public static final String STATUS_23 = "23";
	public static final String STATUS_24 = "24";
	
	
	public static String TICKETTYPE_STUDENT ="2";
	public static String TICKETTYPE_CHILDREN ="1";
	public static String TICKETTYPE_ADULT ="0";
	

	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> TICKETTYPE = new LinkedHashMap<String, String>();
	private static Map<String, String> SEATTTYPE = new LinkedHashMap<String, String>();
	
	public static Map<String, String> getBookStatus(){
		/*
		 * 订单状态: 00下单成功 11通知出票成功 22预订成功 32、 正在出票 33出票成功 44出票失败 51撤销中 52撤销失败 88 超时订单 23、正在取消 24 取消成功
		 */
		if(BOOKSTATUS.isEmpty()){
			BOOKSTATUS.put(STATUS_00, "下单成功 ");
			BOOKSTATUS.put(STATUS_11, "通知出票成功 ");
			BOOKSTATUS.put(STATUS_22, "预订成功  ");
			BOOKSTATUS.put(STATUS_32, "正在出票 ");
			BOOKSTATUS.put(STATUS_33, "出票成功 ");
			BOOKSTATUS.put(STATUS_44, "出票失败 ");
			BOOKSTATUS.put(STATUS_51, "撤销中 ");
			BOOKSTATUS.put(STATUS_52, "撤销失败 ");
			BOOKSTATUS.put(STATUS_88, "超时订单 ");
			BOOKSTATUS.put(STATUS_23, "正在取消 ");
			BOOKSTATUS.put(STATUS_24, "取消成功 ");
		}
		
		return BOOKSTATUS;
	}
	
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
	
	public static Map<String, String> getTicketType(){
		
		if(TICKETTYPE.isEmpty()){
			TICKETTYPE.put(TICKETTYPE_CHILDREN, "儿童票");
			TICKETTYPE.put(TICKETTYPE_ADULT, "成人票");
			TICKETTYPE.put(TICKETTYPE_STUDENT, "学生票");
		
		}
		
		return TICKETTYPE;
	}
	
	
	/** *****************座位类型*********************** */
	public static String SEAT_COMMERCE = "0";// 商务座
	public static String SEAT_ESPECIALLY = "1";// 特等座
	public static String SEAT_ONE_RANK = "2";// 一等座
	public static String SEAT_TWO_RANK = "3";// 二等座
	public static String ADVANCED_SOFT_SLEEPER = "4";// 高级软卧
	public static String ADVANCED_SOFT_SLEEPER_UP = "41";// 高级软卧上
	public static String ADVANCED_SOFT_SLEEPER_DOWN = "42";// 高级软卧下
	public static String SOFT_SLEEPER = "5";// 软卧
	public static String SOFT_SLEEPER_UP = "51";// 软卧上
	public static String SOFT_SLEEPER_DOWN = "52";// 软卧下
	public static String TOURIST_CAR = "6";// 硬卧
	public static String TOURIST_CAR_UP = "61";// 硬卧上
	public static String TOURIST_CAR_CENTER = "62";// 硬卧中
	public static String TOURIST_CAR_DOWN = "63";// 硬卧下
	public static String SEAT_SOFT = "7";// 软座
	public static String SEAT_HARD = "8";// 硬座
	public static String SEAT_NO = "9";// 无座
	public static String SEAT_OTHER = "10";// 其他
	public static String SEAT_DW = "20";// 动卧
	public static String SEAT_DWS = "201";// 动卧上
	public static String SEAT_DWX = "202";// 动卧下
	

	public static Map<String, String> SEAT_MAP = new LinkedHashMap<String, String>();

	public static Map<String, String> getSeatType() {
		if (SEAT_MAP.isEmpty()) {
			SEAT_MAP.put(SEAT_COMMERCE, "商务座");
			SEAT_MAP.put(SEAT_ESPECIALLY, "特等座");
			SEAT_MAP.put(SEAT_ONE_RANK, "一等座");
			SEAT_MAP.put(SEAT_TWO_RANK, "二等座");
			SEAT_MAP.put(ADVANCED_SOFT_SLEEPER, "高级软卧");
			SEAT_MAP.put(ADVANCED_SOFT_SLEEPER_UP, "高级软卧上铺");
			SEAT_MAP.put(ADVANCED_SOFT_SLEEPER_DOWN, "高级软卧下铺");
			SEAT_MAP.put(SOFT_SLEEPER, "软卧");
			SEAT_MAP.put(SOFT_SLEEPER_UP, "软卧上");
			SEAT_MAP.put(SOFT_SLEEPER_DOWN, "软卧下");
			SEAT_MAP.put(TOURIST_CAR, "硬卧");
			SEAT_MAP.put(TOURIST_CAR_UP, "硬卧上");
			SEAT_MAP.put(TOURIST_CAR_CENTER, "硬卧中");
			SEAT_MAP.put(TOURIST_CAR_DOWN, "硬卧下");
			SEAT_MAP.put(SEAT_SOFT, "软座");
			SEAT_MAP.put(SEAT_HARD, "硬座");
			SEAT_MAP.put(SEAT_NO, "无座");
			SEAT_MAP.put(SEAT_OTHER, "其他");
			SEAT_MAP.put(SEAT_DW, "动卧");
			SEAT_MAP.put(SEAT_DWS, "动卧上");
			SEAT_MAP.put(SEAT_DWX, "动卧下");
		}
		return SEAT_MAP;
	}
	
	//	出票系统通知状态：00、准备通知 11、开始通知 22、通知完成 33、通知失败
	public static final String NOTIFY_00 = "00";//准备通知
	public static final String NOTIFY_11 = "11";//开始通知
	public static final String NOTIFY_22 = "22";//通知完成
	public static final String NOTIFY_33 = "33";//通知失败
	private static Map<String, String>BOOK_NOTIFY_STATUS = new LinkedHashMap<String, String>();
	private static Map<String, String>OUT_NOTIFY_STATUS = new LinkedHashMap<String, String>();
	private static Map<String, String>NOTIFY_STATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getBookNotifyStatus() {
		if(BOOK_NOTIFY_STATUS.isEmpty()) {
			BOOK_NOTIFY_STATUS.put(NOTIFY_00, "占座准备");
			BOOK_NOTIFY_STATUS.put(NOTIFY_11, "占座开始");
			BOOK_NOTIFY_STATUS.put(NOTIFY_22, "占座完成");
			BOOK_NOTIFY_STATUS.put(NOTIFY_33, "占座失败");
		}
		return BOOK_NOTIFY_STATUS;
	}
	
	public static Map<String, String> getOutNotifyStatus() {
		if(OUT_NOTIFY_STATUS.isEmpty()) {
			OUT_NOTIFY_STATUS.put(NOTIFY_00, "出票准备");
			OUT_NOTIFY_STATUS.put(NOTIFY_11, "出票开始");
			OUT_NOTIFY_STATUS.put(NOTIFY_22, "出票完成");
			OUT_NOTIFY_STATUS.put(NOTIFY_33, "出票失败");
		}
		return OUT_NOTIFY_STATUS;
	}
	
	public static Map<String, String> getNotifyStatus() {
		if(NOTIFY_STATUS.isEmpty()) {
			NOTIFY_STATUS.put(NOTIFY_00, "准备通知");
			NOTIFY_STATUS.put(NOTIFY_11, "开始通知");
			NOTIFY_STATUS.put(NOTIFY_22, "通知完成");
			NOTIFY_STATUS.put(NOTIFY_33, "通知失败");
		}
		return NOTIFY_STATUS;
	}
	
	
}
