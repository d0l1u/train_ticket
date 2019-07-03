package com.l9e.transaction.vo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElongVo {
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
	private String out_ticket_billno;
	private String ticket_num;
	private String refund_type;
	private String channel;
	
	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> TICKETTYPE = new LinkedHashMap<String, String>();
	private static Map<String, String> NOTIFYSTATUS = new LinkedHashMap<String, String>();

	public String getNotify_status() {
		return notify_status;
	}

	public void setRefund_type(String refundType) {
		refund_type = refundType;
	}
	public String getRefund_type() {
		return refund_type;
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
	
	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}

	public void setOut_ticket_billno(String outTicketBillno) {
		out_ticket_billno = outTicketBillno;
	}

	public String getTicket_num() {
		return ticket_num;
	}

	public void setTicket_num(String ticketNum) {
		ticket_num = ticketNum;
	}



	public static final String TICKET_MAN = "0";// 成人票
	public static final String TICKET_BABE = "1";// 儿童票
	public static final String TICKET_STUDENT = "3";// 学生票
	
	//通知状态:00、准备通知 11、开始通知 22、通知完成 33、通知失败
	public static final String NOTIFY_READY = "00";
	public static final String NOTIFY_START = "11";
	public static final String NOTIFY_FINISH = "22";
	public static final String NOTIFY_FAIL = "33";
	
	public static Map<String, String> getNotifyStatus() {
		if(NOTIFYSTATUS.isEmpty()) {
			NOTIFYSTATUS.put(NOTIFY_READY, "准备通知");
			NOTIFYSTATUS.put(NOTIFY_START, "开始通知");
			NOTIFYSTATUS.put(NOTIFY_FINISH, "通知完成");
			NOTIFYSTATUS.put(NOTIFY_FAIL, "通知失败");
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
	public static String SEAT_DW = "20";// 动卧

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
		}
		return SEAT_MAP;
	}
	
	//订单状态: 00下单成功 11通知出票成功 22预订成功 33出票成功 44出票失败
	public static final String ORDER_SUCCESS = "00"; // 下单成功
	public static final String PAY_SUCCESS = "11"; // 通知出票成功 
	public static final String OUT_FAIL = "22";// 预订成功 
	public static final String OUT_SUCCESS = "33";// 出票成功
	public static final String OUT_TICKET_FAIL = "44";// 出票失败
	public static final String CHEXIAO = "51";// 正在撤销
	public static final String CHEXIAOFAIL = "52";// 撤销失败
	
	public static String ELONG_ORDER_WAITPAY="32";//支付中-同城
	public static String ELONG_OUT_TIME="88";//超时订单(针对同城  同步接口 未返回结果订单变更为超时订单)
	public static String ELONG_FAIL="24";//取消成功
	public static Map<String, String> getBookStatus() {
		if (BOOKSTATUS.isEmpty()) {
			BOOKSTATUS.put(ORDER_SUCCESS, "下单成功");
			BOOKSTATUS.put(PAY_SUCCESS, "通知出票成功");
			BOOKSTATUS.put(OUT_FAIL, "预订成功");
			
			BOOKSTATUS.put(ELONG_ORDER_WAITPAY, "支付中");
			BOOKSTATUS.put(OUT_SUCCESS, "出票成功");
			BOOKSTATUS.put(OUT_TICKET_FAIL, "出票失败");
			BOOKSTATUS.put(CHEXIAO, "正在撤销");
			BOOKSTATUS.put(CHEXIAOFAIL, "撤销失败");
			BOOKSTATUS.put(ELONG_OUT_TIME, "超时订单");
			BOOKSTATUS.put(ELONG_FAIL, "取消成功");
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
	public static String QUNARCHANNEL_1 = "elong";
	public static String QUNARCHANNEL_2 = "tongcheng";
	public static Map<String, String> getQunarChannel() {
		if (QUNARCHANNEL.isEmpty()) {
			QUNARCHANNEL.put(QUNARCHANNEL_1, "艺龙");
			QUNARCHANNEL.put(QUNARCHANNEL_2, "同程");
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
	
	
	//拒绝退款原因
	public static final String TICKETOUT = "1";//已取票
	public static final String TIMEOUT = "2";//已过时间
	public static final String PHONECALL = "3"; //电话取消
	public static final String FENGHAO = "4"; //账号被封  

	//public static final String OTHER = "4";//其他
	
	//通知状态
	public static final String NOTIFY_00 = "00";//准备通知
	public static final String NOTIFY_11 = "11";//开始通知
	public static final String NOTIFY_22 = "22";//通知完成
	public static final String NOTIFY_33 = "33";//通知失败
	
	//退款类型
	public static final String TYPE_11 = "11";//用户退款
	public static final String TYPE_22 = "22";//线下退款
	public static final String TYPE_33 = "33";//车站退票
	public static final String TYPE_44 = "44";//申请退票
	public static final String TYPE_55 = "55";//改签退票
	
	private static Map<String, String> REFUSEREASON = new LinkedHashMap<String, String>();
	private static Map<String, String> REFUNDSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> REFUNDORIGSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> NOTIFY_STATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> REFUND_TYPES = new LinkedHashMap<String, String>();

	public static Map<String, String> getRefund_types() {
		if(REFUND_TYPES.isEmpty()) {
			REFUND_TYPES.put(TYPE_11, "用户退款");
			REFUND_TYPES.put(TYPE_22, "线下退款");
			REFUND_TYPES.put(TYPE_33, "车站退票");
			REFUND_TYPES.put(TYPE_44, "申请退票");
			REFUND_TYPES.put(TYPE_55, "改签退票");
		}
		return REFUND_TYPES;
	}
	
	public static Map<String, String> getNotify_Status() {
		if(NOTIFY_STATUS.isEmpty()) {
			NOTIFY_STATUS.put(NOTIFY_00, "准备通知");
			NOTIFY_STATUS.put(NOTIFY_11, "开始通知");
			NOTIFY_STATUS.put(NOTIFY_22, "完成通知");
			NOTIFY_STATUS.put(NOTIFY_33, "通知失败");
		}
		return NOTIFY_STATUS;
	}
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
	
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票
	//06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款
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
			REFUNDSTATUS.put(REFUNDADDING, "线下退款中");
			REFUNDSTATUS.put(REFUNDADD, "审核线下退款");
			REFUNDSTATUS.put(REFUNDELONG, "申请线下");
			REFUNDSTATUS.put(REFUNDSTATION, "车站退票");
		}
		return REFUNDSTATUS;
	}

	public static Map<String, String> getRefuseReason(){
		if(REFUSEREASON.isEmpty()) {
			REFUSEREASON.put(TICKETOUT, "已取票");
			REFUSEREASON.put(TIMEOUT, "已过时间");
			REFUSEREASON.put(PHONECALL, "来电取消");
			REFUSEREASON.put(FENGHAO, "账号被封");
			//REFUSEREASON.put(OTHER, "其他");
		}
		return REFUSEREASON;
	}
	//退款状态：11、同意退票 22、拒绝退票 33、退款成功
	public static Map<String, String> getRefundOrigStatus() {
		if(REFUNDORIGSTATUS.isEmpty()) {
			REFUNDORIGSTATUS.put(AGREEREFUND, "同意退款");
			REFUNDORIGSTATUS.put(SUCCESS, "退款成功");
			REFUNDORIGSTATUS.put(REFUSEREFUND, "拒绝退款");
		}
		return REFUNDORIGSTATUS;
	}
	//用户退款原因：11、时间/车次错误  22、行程有变  33、其他
	public static final String REASON_TIME = "11";//时间/车次错误
	public static final String REASON_TRIP = "22";//行程有变
	public static final String REASON_QITA = "33";//其他
	private static Map<String, String> REFUNDREASON = new HashMap<String, String>();
	public static Map<String, String> getRefundOrigReason() {
		if(REFUNDREASON.isEmpty()) {
			REFUNDREASON.put(REASON_TIME, "时间/车次错误");
			REFUNDREASON.put(REASON_TRIP, "行程有变");
			REFUNDREASON.put(REASON_QITA, "其他");
		}
		return REFUNDREASON;
	}
	
	
	public static final String ORDERSTATUS_11 = "11";
	public static final String ORDERSTATUS_22 = "22";
	private static Map<String, String> ORDERSTATUS = new HashMap<String, String>();
	public static Map<String, String> getOrderStatus() {
		if(ORDERSTATUS.isEmpty()) {
			ORDERSTATUS.put(ORDERSTATUS_11, "导入成功");
			ORDERSTATUS.put(ORDERSTATUS_22, "重复导入");
		}
		return ORDERSTATUS;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
	/*
	 * 改签状态：11、改签预订 12、正在改签预订 13、人工改签预订 14、改签成功等待确认 15、改签预订失败21、改签取消 22、正在取消 23、取消成功 
	 * 24、取消失败 31、开始支付 32、正在支付 33、人工支付 34、支付成功 35、支付失败 36、补价支付',
	 */
	public static final String ALTER_BOOK = "11";
	public static final String ALTER_BOOKING = "12";
	public static final String ALTER_BOOK_OPT = "13";
	public static final String ALTER_VERIFY = "14";
	public static final String ALTER_FAIL = "15";
	public static final String ALTER_CANCEL = "21";
	public static final String ALTER_CANCELING = "22";
	public static final String ALTER_CANCEL_SUCCESS = "23";
	public static final String ALTER_CANCEL_FAIL = "24";
	public static final String ALTER_PAY_START = "31"; 
	public static final String ALTER_PAYING = "32";
	public static final String ALTER_PAY_OPT = "33";
	public static final String ALTER_PAY_SUCCESS = "34";
	public static final String ALTER_PAY_FAIL = "35";
	public static final String ALTER_PAY_SUPPY = "36";
	public static final String ALTER_TIME_OUT = "37";
	/*
	 * 改签状态：11、改签预订 12、正在改签预订 13、人工改签预订 14、改签成功等待确认 15、改签预订失败21、改签取消 22、正在取消 23、取消成功 
	 * 24、取消失败 31、开始支付 32、正在支付 33、人工支付 34、支付成功 35、支付失败 36、补价支付',37、改签超时
	 */
	private static Map<String, String> ALTERSTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getAlterStatus() {
		if(ALTERSTATUS.isEmpty()) {
			ALTERSTATUS.put(ALTER_BOOK,  "改签预订");
			ALTERSTATUS.put(ALTER_BOOKING , "正在改签预订");
			ALTERSTATUS.put(ALTER_BOOK_OPT , "人工改签预订");
			ALTERSTATUS.put(ALTER_VERIFY , "改签成功待核验");
			ALTERSTATUS.put(ALTER_FAIL , "改签预订失败");
			ALTERSTATUS.put(ALTER_CANCEL , "改签取消");
			ALTERSTATUS.put(ALTER_CANCELING , "正在取消");
			ALTERSTATUS.put(ALTER_CANCEL_SUCCESS , "取消成功");
			ALTERSTATUS.put(ALTER_CANCEL_FAIL , "取消失败");
			ALTERSTATUS.put(ALTER_PAY_START , "开始支付"); 
			ALTERSTATUS.put(ALTER_PAYING , "正在支付");
			ALTERSTATUS.put(ALTER_PAY_OPT , "人工支付");
			ALTERSTATUS.put(ALTER_PAY_SUCCESS , "支付成功");
			ALTERSTATUS.put(ALTER_PAY_FAIL , "支付失败");
			ALTERSTATUS.put(ALTER_PAY_SUPPY , "补价支付");
			ALTERSTATUS.put(ALTER_TIME_OUT , "改签超时");

		}
		return ALTERSTATUS;
	}
	public static final String ALTER_NOTIFY_000 = "000";
	public static final String ALTER_NOTIFY_111 = "111";
	public static final String ALTER_NOTIFY_222 = "222";
	public static final String ALTER_NOTIFY_333 = "333";
	/*
	 * 回调状态:000、准备回调 111、开始回调 222、回调完成 333、回调失败
	 */
	private static Map<String, String> ALTERNOTIFYSTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getChangeNotifyStatus() {
		if(ALTERNOTIFYSTATUS.isEmpty()) {
			ALTERNOTIFYSTATUS.put(ALTER_NOTIFY_000, "准备通知");
			ALTERNOTIFYSTATUS.put(ALTER_NOTIFY_111, "开始通知");
			ALTERNOTIFYSTATUS.put(ALTER_NOTIFY_222, "完成通知");
			ALTERNOTIFYSTATUS.put(ALTER_NOTIFY_333, "通知失败");
		}
		return ALTERNOTIFYSTATUS;
	}
	
	public static final String FAIL_REASON_301 = "301";
	public static final String FAIL_REASON_305 = "305";
	public static final String FAIL_REASON_309 = "309";
	public static final String FAIL_REASON_310 = "310";
	public static final String FAIL_REASON_506 = "506";
	public static final String FAIL_REASON_313 = "313";
	public static final String FAIL_REASON_998 = "998";
	public static final String FAIL_REASON_999 = "999";
	public static final String FAIL_REASON_314 = "314";
	public static final String FAIL_REASON_1004 = "1004";
	public static final String FAIL_REASON_9991 = "9991";
	public static final String FAIL_REASON_308 = "308";
	public static final String FAIL_REASON_1010 = "1010";
	public static final String FAIL_REASON_316 = "316";
	public static final String FAIL_REASON_318 = "318";
	public static final String FAIL_REASON_320 = "320";
	public static final String FAIL_REASON_1002 = "1002";
	public static final String FAIL_REASON_315 = "315";
	public static final String FAIL_REASON_317 = "317";
	public static final String FAIL_REASON_319 = "319";
	public static final String FAIL_REASON_322 = "322";
	public static final String FAIL_REASON_324= "324";
	public static final String FAIL_REASON_325= "325";
	public static final String FAIL_REASON_326= "326";
	/*
	 * 
	301	没有余票
	305	乘客已经预订过该车次
	309	没有足够的票
	310	本次购票与其他订单行程冲突
	506	实际系统错误原因（即供应商本身系统问题导致无法占座操作）
	313	示例：“张三已被法院依法限制高消费，禁止乘坐列车XX座位“，具体按12306实际提示结果返回
	998	订单所属账号被封，无法处理
	999	所有未定义的12306即时返回的错误提示 
	314	12306账号异常_该账号需要进行手机核验，请确认
	1004 "取消改签次数超过上限,无法继续操作"
	9991 旅游票,请到车站办理
	 */
	private static Map<String, String> ALTERFAILREASON = new LinkedHashMap<String, String>();
	public static Map<String, String> getAlertFailReason() {
		if(ALTERFAILREASON.isEmpty()) {
			ALTERFAILREASON.put(FAIL_REASON_301, "没有余票");
			ALTERFAILREASON.put(FAIL_REASON_305, "乘客已经预定过该车次");
			ALTERFAILREASON.put(FAIL_REASON_1002, "距离开车时间太近无法改签");
			ALTERFAILREASON.put(FAIL_REASON_309, "没有足够的票");
			ALTERFAILREASON.put(FAIL_REASON_310, "本次购票与其他订单行程冲突");
			ALTERFAILREASON.put(FAIL_REASON_506, "系统错误");
			ALTERFAILREASON.put(FAIL_REASON_313, "法院依法限制高消费，禁止乘坐列车XX座位");
			ALTERFAILREASON.put(FAIL_REASON_998, "订单所属账号被封，无法处理");
			ALTERFAILREASON.put(FAIL_REASON_999, "所有未定义的12306即时返回的错误提示 ");
			ALTERFAILREASON.put(FAIL_REASON_314, "12306账号异常_该账号需要进行手机核验，请确认");
			ALTERFAILREASON.put(FAIL_REASON_1004, "取消改签次数超过上限,无法继续操作");
			ALTERFAILREASON.put(FAIL_REASON_9991, "旅游票,请到车站办理");
			ALTERFAILREASON.put(FAIL_REASON_308, "存在未完成订单");
			ALTERFAILREASON.put(FAIL_REASON_1010, "不符合变更到站条件");
		}
		return ALTERFAILREASON;
	}
	private static Map<String, String> TUNIUALTERFAILREASON = new LinkedHashMap<String, String>();
	public static Map<String, String> getTuniuAlertFailReason() {
		if(TUNIUALTERFAILREASON.isEmpty()) {
			TUNIUALTERFAILREASON.put(FAIL_REASON_301, "没有余票");
			TUNIUALTERFAILREASON.put(FAIL_REASON_310, "本次购票与其他订单行程冲突");
			TUNIUALTERFAILREASON.put(FAIL_REASON_506, "系统错误");
			TUNIUALTERFAILREASON.put(FAIL_REASON_313, "法院依法限制高消费，禁止乘坐列车XX座位");
			TUNIUALTERFAILREASON.put(FAIL_REASON_999, "所有未定义的12306错误 ");
			TUNIUALTERFAILREASON.put(FAIL_REASON_314, "账号登陆失败");
			TUNIUALTERFAILREASON.put(FAIL_REASON_1002, "距离开车时间太近，无法改签");
			TUNIUALTERFAILREASON.put(FAIL_REASON_1004, "取消改签次数超过上限,无法继续操作");
			TUNIUALTERFAILREASON.put(FAIL_REASON_9991, "旅游票,请到车站办理");
			TUNIUALTERFAILREASON.put(FAIL_REASON_308, "存在未完成订单");
			TUNIUALTERFAILREASON.put(FAIL_REASON_316, "不允许改签到指定时间的车票");
			TUNIUALTERFAILREASON.put(FAIL_REASON_318, "已退票，不可改签");
			TUNIUALTERFAILREASON.put(FAIL_REASON_320, "不存在改签待支付订单");
			TUNIUALTERFAILREASON.put(FAIL_REASON_315, "未找到要改签的车次");
			TUNIUALTERFAILREASON.put(FAIL_REASON_317, "已出票，不可改签");
			TUNIUALTERFAILREASON.put(FAIL_REASON_319, "已改签，不可改签");
			TUNIUALTERFAILREASON.put(FAIL_REASON_322, "当前的排队购票人数过多，请稍后重试");
			TUNIUALTERFAILREASON.put(FAIL_REASON_324, "已确认改签，不可取消");
			TUNIUALTERFAILREASON.put(FAIL_REASON_325, "确认改签失败");
			TUNIUALTERFAILREASON.put(FAIL_REASON_326, "确认改签时间已超时，确认改签失败");
		}
		return TUNIUALTERFAILREASON;
	}
	private static Map<String, String> ALLALTERFAILREASON = new LinkedHashMap<String, String>();
	public static Map<String, String> getAllAlertFailReason() {
		if(ALLALTERFAILREASON.isEmpty()) {
			ALLALTERFAILREASON.put(FAIL_REASON_305, "乘客已经预定过该车次");
			ALLALTERFAILREASON.put(FAIL_REASON_309, "没有足够的票");
			ALLALTERFAILREASON.put(FAIL_REASON_998, "订单所属账号被封，无法处理");
			ALLALTERFAILREASON.put(FAIL_REASON_301, "没有余票");
			ALLALTERFAILREASON.put(FAIL_REASON_310, "本次购票与其他订单行程冲突");
			ALLALTERFAILREASON.put(FAIL_REASON_506, "系统错误");
			ALLALTERFAILREASON.put(FAIL_REASON_313, "法院依法限制高消费，禁止乘坐列车XX座位");
			ALLALTERFAILREASON.put(FAIL_REASON_999, "所有未定义的12306错误 ");
			ALLALTERFAILREASON.put(FAIL_REASON_314, "账号登陆失败");
			ALLALTERFAILREASON.put(FAIL_REASON_1002, "距离开车时间太近，无法改签");
			ALLALTERFAILREASON.put(FAIL_REASON_1004, "取消改签次数超过上限,无法继续操作");
			ALLALTERFAILREASON.put(FAIL_REASON_9991, "旅游票,请到车站办理");
			ALLALTERFAILREASON.put(FAIL_REASON_308, "存在未完成订单");
			ALLALTERFAILREASON.put(FAIL_REASON_316, "不允许改签到指定时间的车票");
			ALLALTERFAILREASON.put(FAIL_REASON_318, "已退票，不可改签");
			ALLALTERFAILREASON.put(FAIL_REASON_320, "不存在改签待支付订单");
			ALLALTERFAILREASON.put(FAIL_REASON_315, "未找到要改签的车次");
			ALLALTERFAILREASON.put(FAIL_REASON_317, "已出票，不可改签");
			ALLALTERFAILREASON.put(FAIL_REASON_319, "已改签，不可改签");
			ALLALTERFAILREASON.put(FAIL_REASON_322, "当前的排队购票人数过多，请稍后重试");
			ALLALTERFAILREASON.put(FAIL_REASON_324, "已确认改签，不可取消");
			
		}
		return ALLALTERFAILREASON;
	}
	
	
}
