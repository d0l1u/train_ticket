package com.l9e.transaction.vo;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class QunarBookVo {

	private String order_id;
	private String startAndEnd;
	private String train_no;
	private String from_time;// 出发时间
	private Double pay_money;// 支付价格
	private Double buy_money;// 成本价格
	private String order_status;
	private Timestamp order_time;
	private Timestamp create_time;
	private Timestamp out_ticket_time;
	private String out_ticket_type;
	private String opt_ren;
	private String order_source;
	private String seat_type;
	private String order_type;
	private String notify_status;
	private String ext_field1;
	
	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> TICKETTYPE = new LinkedHashMap<String, String>();
	private static Map<String, String> NOTIFYSTATUS = new LinkedHashMap<String, String>();

	public String getNotify_status() {
		return notify_status;
	}

	public String getExt_field1() {
		return ext_field1;
	}

	public void setExt_field1(String extField1) {
		ext_field1 = extField1;
	}

	public void setNotify_status(String notify_status) {
		this.notify_status = notify_status;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getStartAndEnd() {
		return startAndEnd;
	}

	public void setStartAndEnd(String startAndEnd) {
		this.startAndEnd = startAndEnd;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}

	public String getFrom_time() {
		return from_time;
	}

	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}

	public Double getPay_money() {
		return pay_money;
	}

	public void setPay_money(Double pay_money) {
		this.pay_money = pay_money;
	}

	public Double getBuy_money() {
		return buy_money;
	}

	public void setBuy_money(Double buy_money) {
		this.buy_money = buy_money;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public Timestamp getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Timestamp order_time) {
		this.order_time = order_time;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getOut_ticket_time() {
		return out_ticket_time;
	}

	public void setOut_ticket_time(Timestamp out_ticket_time) {
		this.out_ticket_time = out_ticket_time;
	}

	public String getOut_ticket_type() {
		return out_ticket_type;
	}

	public void setOut_ticket_type(String out_ticket_type) {
		this.out_ticket_type = out_ticket_type;
	}

	public String getOpt_ren() {
		return opt_ren;
	}

	public void setOpt_ren(String opt_ren) {
		this.opt_ren = opt_ren;
	}

	public static final String PAY_SUCCESS = "11"; // 支付成功
	public static final String OUT_FAIL = "33";// 预订成功
	public static final String OUT_SUCCESS = "44";// 出票成功
	public static final String OUT_TICKET_FAIL = "45";// 出票失败
	public static final String CANCEL_SUCCESS = "99";// 出票失败
	
	public static final String TICKET_MAN = "0";// 成人票
	public static final String TICKET_BABE = "1";// 儿童票
	public static final String TICKET_STUDENT = "2";// 学生票
	
	public static final String NOTIFY_FAILURE = "0";
	public static final String NOTIFY_SUCCESS = "1";
	
	public static Map<String, String> getNotifyStatus() {
		if(NOTIFYSTATUS.isEmpty()) {
			NOTIFYSTATUS.put(NOTIFY_FAILURE, "通知失败");
			NOTIFYSTATUS.put(NOTIFY_SUCCESS, "通知成功");
		}
		return NOTIFYSTATUS;
	}

	// public static Map<Integer, String> TICKETTYPE_MAP = new
	// LinkedHashMap<Integer, String>();

	public static Map<String, String> getTicket_Types() {
		if (TICKETTYPE.isEmpty()) {
			TICKETTYPE.put(TICKET_MAN, "成人票");
			TICKETTYPE.put(TICKET_BABE, "儿童票");
			TICKETTYPE.put(TICKET_STUDENT, "学生票");
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
		}
		return SEAT_MAP;
	}

	public static Map<String, String> getBookStatus() {

		if (BOOKSTATUS.isEmpty()) {
			BOOKSTATUS.put(PAY_SUCCESS, "支付成功");
			BOOKSTATUS.put(OUT_FAIL, "预订成功");
			BOOKSTATUS.put(OUT_SUCCESS, "出票成功");
			BOOKSTATUS.put(OUT_TICKET_FAIL, "出票失败");
			BOOKSTATUS.put(CANCEL_SUCCESS, "取消成功");
		}

		return BOOKSTATUS;
	}

	/** *****************去哪儿座位类型*********************** */
	public static String QUNAR_SEAT_STAND = "0";// 无座
	public static String QUNAR_SEAT_HARD = "1";// 硬座
	public static String QUNAR_SEAT_SOFT = "2";// 软座
	public static String QUNAR_SEAT_FIRST_SOFT = "3";// 一等软座
	public static String QUNAR_SEAT_SECOND_SOFT = "4";// 二等软座
	public static String QUNAR_TOURIST_CAR_UP = "5";// 硬卧上
	public static String QUNAR_TOURIST_CAR_CENTER = "6";// 硬卧中
	public static String QUNAR_TOURIST_CAR_DOWN = "7";// 硬卧下
	public static String QUNAR_SOFT_SLEEPER_UP = "8";// 软卧上
	public static String QUNAR_SOFT_SLEEPER_DOWN = "9";// 软卧下
	public static String QUNAR_ADVANCED_SOFT_SLEEPER_UP = "10";// 高等软卧上
	public static String QUNAR_ADVANCED_SOFT_SLEEPER_DOWN = "11";// 高等软卧下

	public static Map<String, String> QUNAR_SEAT_MAP = new LinkedHashMap<String, String>();

	public static Map<String, String> getQunar_Seat_type() {
		if (QUNAR_SEAT_MAP.isEmpty()) {
			QUNAR_SEAT_MAP.put(QUNAR_SEAT_STAND, "无座");
			QUNAR_SEAT_MAP.put(QUNAR_SEAT_HARD, "硬座");
			QUNAR_SEAT_MAP.put(QUNAR_SEAT_SOFT, "软座");
			QUNAR_SEAT_MAP.put(QUNAR_SEAT_FIRST_SOFT, "一等软座");
			QUNAR_SEAT_MAP.put(QUNAR_SEAT_SECOND_SOFT, "二等软座");
			QUNAR_SEAT_MAP.put(QUNAR_TOURIST_CAR_UP, "硬卧上");
			QUNAR_SEAT_MAP.put(QUNAR_TOURIST_CAR_CENTER, "硬卧中");
			QUNAR_SEAT_MAP.put(QUNAR_TOURIST_CAR_DOWN, "硬卧下");
			QUNAR_SEAT_MAP.put(QUNAR_SOFT_SLEEPER_UP, "软卧上");
			QUNAR_SEAT_MAP.put(QUNAR_SOFT_SLEEPER_DOWN, "软卧下");
			QUNAR_SEAT_MAP.put(QUNAR_ADVANCED_SOFT_SLEEPER_UP, "高等软卧上");
			QUNAR_SEAT_MAP.put(QUNAR_ADVANCED_SOFT_SLEEPER_DOWN, "高等软卧下");
		}
		return QUNAR_SEAT_MAP;
	}

	// 出票失败原因
	public static String NO_TICKET = "1";
	public static String ALREADY_TICKET = "2";
	public static String NOT_SAME; // 票价不相同

	public static Map<String, String> FAIL_REASON = new LinkedHashMap<String, String>();

	public static Map<String, String> getFailReason() {
		if (FAIL_REASON.isEmpty()) {
			FAIL_REASON.put(NO_TICKET, "所购买的车次坐席已无票");
			FAIL_REASON.put(ALREADY_TICKET, "身份证件已经实名制购票，不能再次购买同日期同车次的车票");
			FAIL_REASON.put(NOT_SAME, "qunar票价和12306不符");
		}
		return FAIL_REASON;
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
	
	//渠道id：qunar1、19旅行 qunar2、久久商旅
	private static Map<String, String> QUNARCHANNEL = new LinkedHashMap<String, String>();
	public static String QUNARCHANNEL_1 = "qunar1";
	public static String QUNARCHANNEL_2 = "qunar2";
	public static Map<String, String> getQunarChannel() {
		if (QUNARCHANNEL.isEmpty()) {
			QUNARCHANNEL.put(QUNARCHANNEL_2, "久久商旅");
			QUNARCHANNEL.put(QUNARCHANNEL_1, "19旅行");
		}
		return QUNARCHANNEL;
	}
	
	public void setSeat_type(String seat_type) {
		this.seat_type = seat_type;
	}

	public String getSeat_type() {
		return seat_type;
	}

	public String getOrder_source() {
		return order_source;
	}

	public void setOrder_source(String order_source) {
		this.order_source = order_source;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	
	
}
