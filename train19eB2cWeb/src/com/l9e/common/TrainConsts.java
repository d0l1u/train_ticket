package com.l9e.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 常量
 *
 */
public class TrainConsts {
	
	/**
	 * session中存放用户的KEY
	 */
	public static final String INF_LOGIN_USER = "loginUser";
	
	public static final String INF_SYS_CONF = "sysConf";
	
	public static final String SUCCESS = "success";
	
	public static final String FAIL = "fail";
	
	public static final String FAILURE = "failure";
	
	/*******************订单状态***********************/
	//订单状态 00、未支付 11、支付成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、已取消
	//19e	 00、预下单 11、支付成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支 付失败 77、预约购票 78、取消预约
	public static final String PRE_ORDER = "00";//未支付
	public static final String PRE_BOOK = "01";//准备预定
	public static final String PAY_SUCCESS = "11";//支付成功
	public static final String BOOKING_TICKET = "22";//正在预订	
	public static final String BOOK_SUCCESS = "33";//预订成功
	public static final String OUT_SUCCESS = "44";//出票成功	
	public static final String OUT_FAIL = "45";//出票失败
	public static final String ORDER_FINISH = "88";//订单完成
	public static final String PAY_FAIL = "99";//支付失败
	public static final String OVER_NOPAY = "21";//超时未支付
	
	/********************退款状态*****************************/
	public static final String REFUND_PRE_REFUND = "55";//准备退款
	public static final String REFUND_REFUNDING = "66";//正在退款	
	public static final String REFUND_EOP_REFUNDING = "67";//EOP正在退款
	public static final String REFUND_REFUND_FINISH = "77";//退票完成	
	public static final String REFUND_REFUSE = "88";//拒绝退款	
	//退款状态：00:无退票 11: 同意退票 22: 拒绝退票 33:退票完成 44:退票失败
	public static final String REFUND_STATUS_00 = "00";//无退票
	public static final String REFUND_STATUS_11 = "11";//同意退票
	public static final String REFUND_STATUS_22 = "22";//拒绝退票
	public static final String REFUND_STATUS_33 = "33";//退票完成
	public static final String REFUND_STATUS_44 = "44";//退票失败
	
	/********************差价退款状态*****************************/
	public static final String DIFFER_PRE_REFUND = "00";//准备退款
	public static final String DIFFER_WAIT_REFUND = "11";//等待退款
	public static final String DIFFER_BEGIN_REFUND = "22";//开始退款
	public static final String DIFFER_EOP_REFUNDING = "33";//EOP退款中
	public static final String DIFFER_REFUND_FINISH = "44";//退款完成
	
	/********************退款流水状态*******************************/
	//退款状态：00、等待退票 11、开始退票 22、拒绝退票 33、退票完成 44、退票失败 55、微信退款处理中
	//public static final String REFUND_STREAM_PRE_REFUND = "00";//准备退款
	public static final String REFUND_STREAM_WAIT_REFUND = "00";//等待退款
	public static final String REFUND_STREAM_BEGIN_REFUND = "11";//开始退款
	public static final String REFUND_STREAM_EOP_REFUNDING = "44";//EOP退款中------------退票失败
	public static final String REFUND_STREAM_REFUND_FINISH = "33";//退款完成
	public static final String REFUND_STREAM_REFUSE= "22";//拒绝退款
	
	/********************退款类型***************************************/
	//退款类型：1、用户线上退票 2、改签差额退款 3、用户电话退票 4、差额退款 5、出票失败退款 6、改签单退款
//	public static final String REFUND_TYPE_1 = "1";//用户退款
//	public static final String REFUND_TYPE_2 = "2";//差额退款---->改签差额退款
//	public static final String REFUND_TYPE_3 = "3";//出票失败退款---->用户电话退票
//	public static final String REFUND_TYPE_4 = "4";//改签差额退款---->差额退款
//	public static final String REFUND_TYPE_5 = "5";//改签单退款---->出票失败退款
//	public static final String REFUND_TYPE_6 = "6";//电话退票---->改签单退款
	public static final String REFUND_TYPE_1 = "1";//用户退款
	public static final String REFUND_TYPE_2 = "4";//差额退款---->改签差额退款
	public static final String REFUND_TYPE_3 = "5";//出票失败退款---->用户电话退票
	public static final String REFUND_TYPE_4 = "2";//改签差额退款---->差额退款
	public static final String REFUND_TYPE_5 = "6";//改签单退款---->出票失败退款
	public static final String REFUND_TYPE_6 = "3";//电话退票---->改签单退款
	
	/*******************身份证类别***********************/
	public static final String IDS_1 = "1";
	public static final String IDS_2 = "2";
	public static final String IDS_3 = "3";
	public static final String IDS_4 = "4";
	public static final String IDS_5 = "5";
	
	/*******************票种***********************/
	public static final String TICKETTYPE_CHILDREN = "1";
	public static final String TICKETTYPE_ADULT = "0";
	
	/*******************坐席***********************/
	public static final String SEAT_0 = "0";
	public static final String SEAT_1 = "1";
	public static final String SEAT_2 = "2";
	public static final String SEAT_3 = "3";
	public static final String SEAT_4 = "4";
	public static final String SEAT_5 = "5";
	public static final String SEAT_6 = "6";
	public static final String SEAT_7 = "7";
	public static final String SEAT_8 = "8";
	public static final String SEAT_9 = "9";
	public static final String SEAT_10 = "10";
	
	/*******************产品类别***********************/
	public static final Integer PRODUCT_TYPE_0 = 0;//自有产品
	public static final Integer PRODUCT_TYPE_1 = 1;//保险
	
	/*******************产品状态***********************/
	public static final Integer PRODUCT_STATUS_0 = 0;//未上架
	public static final Integer PRODUCT_STATUS_1 = 1;//上架
	
	/*******************出票方式***********************/
	public static final String OUT_TICKET_TYPE_ELEC = "11";//电子票
	public static final String OUT_TICKET_TYPE_PS = "22";//配送票
	
	/*******************代理商审核状态***************************/
	public static final String AGENT_ESTATE_NEED_PAY = "00";//需要付费
	public static final String AGENT_ESTATE_WAITING = "11";//等待审核
	public static final String AGENT_ESTATE_NOT = "22";//审核未通过
	public static final String AGENT_ESTATE_PASSED = "33";//审核通过
	public static final String AGENT_ESTATE_NEED_REPAY = "44";//需要续费
	
	/********************用户级别*******************************/
	public static final String USER_LEVEL_NORMAL = "0";//普通用户
	public static final String USER_LEVEL_VIP = "1";//vip用户
	public static final String USER_LEVEL_FREE = "2";//免费用户
	
	/*******************计费方式***********************************/
	public static final String SALE_TYPE_MONTH = "1";//元/月
	public static final String SALE_TYPE_YEAR = "2";//元/年
	
	/*******************投诉建议问题类型*****************************/
	public static final String QUESTION_TYPE_0 = "0";//订单问题
	public static final String QUESTION_TYPE_1 = "1";//加盟问题
	public static final String QUESTION_TYPE_2 = "2";//配送问题
	public static final String QUESTION_TYPE_3 = "3";//出票问题
	public static final String QUESTION_TYPE_4 = "4";//业务建议
	public static final String QUESTION_TYPE_5 = "5";//其他
	
	/*******************配送状态***********************************/
	public static final String PS_STATUS_WAITING = "00";//等待配送
	public static final String PS_STATUS_SENDING = "11";//正在配送
	public static final String PS_STATUS_SENDED = "22";//配送成功
	
	/*******************区域开通状态********************************/
	public static final String AREA_OPEN_NO = "00";//关闭
	public static final String AREA_OPEN_YES = "11";//开通
	
	/*******************区域付费********************************/
	public static final String AREA_COST_NO = "00";//否
	public static final String AREA_COST_YES = "11";//是
	
	/*******************区域配送********************************/
	public static final String AREA_PS_NO = "00";//关闭
	public static final String AREA_PS_YES = "11";//开通
	
	/*******************区域开通购买********************************/
	public static final String AREA_BUY_NO = "00";//否
	public static final String AREA_BUY_YES = "11";//是
	
	/*******************店铺类别*********************************/
	public static final String SHOP_TYPE_0 = "0";//手机充值店
	public static final String SHOP_TYPE_1 = "1";//鲜花礼品店
	public static final String SHOP_TYPE_2 = "2";//小型超市
	public static final String SHOP_TYPE_3 = "3";//大型超市
	public static final String SHOP_TYPE_4 = "4";//烟酒店
	public static final String SHOP_TYPE_5 = "5";//报刊亭
	public static final String SHOP_TYPE_6 = "6";//票务代售点
	public static final String SHOP_TYPE_7 = "7";//彩票代售点
	public static final String SHOP_TYPE_8 = "8";//旅行社
	public static final String SHOP_TYPE_10 = "10";//网吧
	public static final String SHOP_TYPE_9 = "9";//其他
	
	/********************出票失败原因***************************/
	public static final String OUT_FAIL_REASON_1 = "1";//所购买的车次坐席已无票
	public static final String OUT_FAIL_REASON_2 = "2";//身份证件已经实名制购票，不能再次购买同日期同车次的车票
	public static final String OUT_FAIL_REASON_3 = "3";//票价和12306不符
	public static final String OUT_FAIL_REASON_4 = "4";//乘车时间异常
	public static final String OUT_FAIL_REASON_5 = "5";//证件错误
	public static final String OUT_FAIL_REASON_6 = "6";//用户要求取消订单
	public static final String OUT_FAIL_REASON_7 = "7";//未通过12306实名认证
	public static final String OUT_FAIL_REASON_8 = "8";//乘客身份信息待核验

	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();//订单状态
	private static Map<String, String> REFUND_STREAM_STATUS = new HashMap<String, String>();//退款流水状态
	private static Map<String, String> REFUND_TYPE = new HashMap<String, String>();//退款类型
	private static Map<String, String> IDSTYPE = new LinkedHashMap<String, String>();//身份证
	private static Map<String, String> TICKETTYPE = new LinkedHashMap<String, String>();//票类型
	private static Map<String, String> SEATTTYPE = new LinkedHashMap<String, String>();//坐席
	private static Map<String, String> SALETYPE = new LinkedHashMap<String, String>();//计费方式
	private static Map<String, String> PSSTATUS = new LinkedHashMap<String, String>();//配送状态
	private static Map<String, String> SEATID_YP = new HashMap<String, String>();//坐席和余票映射
	private static Map<String, String> FAIL_REASON = new HashMap<String, String>();//出票失败原因
	private static Map<String, String> REFUND_STATUS = new HashMap<String, String>();//退款状态：00:无退票 11: 同意退票 22: 拒绝退票 33:退票完成 44:退票失败
	
	//初始化
	static {
		//00、未支付 01、超时未支付 11、支付成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、已取消
		BOOKSTATUS.put(PRE_ORDER, "未支付");//预下单
		BOOKSTATUS.put(OVER_NOPAY, "超时未支付");//超时未支付
		BOOKSTATUS.put(PAY_SUCCESS, "支付成功");//支付成功
		BOOKSTATUS.put(BOOKING_TICKET, "出票中");//正在预订
		BOOKSTATUS.put(BOOK_SUCCESS, "出票中");//预订成功
		BOOKSTATUS.put(OUT_SUCCESS, "出票成功");//出票成功
		BOOKSTATUS.put(OUT_FAIL, "出票失败");//出票失败
		BOOKSTATUS.put(ORDER_FINISH, "订单完成");//订单完成
		BOOKSTATUS.put(PAY_FAIL, "支付失败");//支付失败
		
		//退款状态：00、等待退票 11、开始退票 22、拒绝退票 33、退票完成 44、退票失败 55、微信退款处理中
//		REFUND_STREAM_STATUS.put(REFUND_STREAM_PRE_REFUND, "正在退款");//准备退款
		REFUND_STREAM_STATUS.put(REFUND_STREAM_WAIT_REFUND, "正在退款");//等待退款00
		REFUND_STREAM_STATUS.put(REFUND_STREAM_BEGIN_REFUND, "正在退款");//开始退款11
		REFUND_STREAM_STATUS.put(REFUND_STREAM_EOP_REFUNDING, "退票失败");//退票失败44
		REFUND_STREAM_STATUS.put(REFUND_STREAM_REFUND_FINISH, "退票完成");//退款完成33
		REFUND_STREAM_STATUS.put(REFUND_STREAM_REFUSE, "退票失败");//拒绝退款22
		
		//退款状态：00:无退票 11: 同意退票 22: 拒绝退票 33:退票完成 44:退票失败
		REFUND_STATUS.put(REFUND_STATUS_00, "无退票");
		REFUND_STATUS.put(REFUND_STATUS_11, "正在退款");
		REFUND_STATUS.put(REFUND_STATUS_22, "退票失败");
		REFUND_STATUS.put(REFUND_STATUS_33, "退票完成");
		REFUND_STATUS.put(REFUND_STATUS_44, "退票失败");
		
		REFUND_TYPE.put(REFUND_TYPE_1, "用户申请退款");
		REFUND_TYPE.put(REFUND_TYPE_2, "差额退款");
		REFUND_TYPE.put(REFUND_TYPE_3, "出票失败退款");
		REFUND_TYPE.put(REFUND_TYPE_4, "改签差额退款");
		REFUND_TYPE.put(REFUND_TYPE_5, "改签单退款");
		REFUND_TYPE.put(REFUND_TYPE_6, "电话退票");
		
		IDSTYPE.put(IDS_1, "一代身份证");
		IDSTYPE.put(IDS_2, "二代身份证");
		IDSTYPE.put(IDS_3, "港澳通行证");
		IDSTYPE.put(IDS_4, "台湾通行证");
		IDSTYPE.put(IDS_5, "护照");
		
		TICKETTYPE.put(TICKETTYPE_CHILDREN, "儿童票");
		TICKETTYPE.put(TICKETTYPE_ADULT, "成人票");
		
		SEATTTYPE.put(SEAT_0, "商务座");
		SEATTTYPE.put(SEAT_1, "特等座");
		SEATTTYPE.put(SEAT_2, "一等座");
		SEATTTYPE.put(SEAT_3, "二等座");
		SEATTTYPE.put(SEAT_4, "高级软卧");
		SEATTTYPE.put(SEAT_5, "软卧");
		SEATTTYPE.put(SEAT_6, "硬卧");
		SEATTTYPE.put(SEAT_7, "软座");
		SEATTTYPE.put(SEAT_8, "硬座");
		SEATTTYPE.put(SEAT_9, "无座");
		SEATTTYPE.put(SEAT_10, "其他");
		
		
		SALETYPE.put(SALE_TYPE_MONTH, "元/月");
		SALETYPE.put(SALE_TYPE_YEAR, "元/年");
		
		
		
		PSSTATUS.put(PS_STATUS_WAITING, "等待配送");
		PSSTATUS.put(PS_STATUS_SENDING, "正在配送");
		PSSTATUS.put(PS_STATUS_SENDED, "配送成功");
		
		SEATID_YP.put(SEAT_0, "swz_yp");//商务座
		SEATID_YP.put(SEAT_1, "tdz_yp");//特等座
		SEATID_YP.put(SEAT_2, "rz1_yp");//一等座
		SEATID_YP.put(SEAT_3, "rz2_yp");//二等座
		SEATID_YP.put(SEAT_4, "gw_yp");//高级软卧
		SEATID_YP.put(SEAT_5, "rw_yp");//软卧
		SEATID_YP.put(SEAT_6, "yw_yp");//硬卧
		SEATID_YP.put(SEAT_7, "rz_yp");//软座
		SEATID_YP.put(SEAT_8, "yz_yp");//硬座
		SEATID_YP.put(SEAT_9, "wz_yp");//无座
		
		
		FAIL_REASON.put(OUT_FAIL_REASON_1, "您所购买的车次坐席已无票。");
		FAIL_REASON.put(OUT_FAIL_REASON_2, "身份证件已经实名制购票，不能再次购买同日期同车次的车票。");
		FAIL_REASON.put(OUT_FAIL_REASON_3, "票价与铁路系统不符。");
		FAIL_REASON.put(OUT_FAIL_REASON_4, "乘车时间异常。");
		FAIL_REASON.put(OUT_FAIL_REASON_5, "证件错误。");
		FAIL_REASON.put(OUT_FAIL_REASON_6, "用户要求取消订单。");
		FAIL_REASON.put(OUT_FAIL_REASON_7, "未通过铁路系统实名认证。");
		FAIL_REASON.put(OUT_FAIL_REASON_8, "乘客身份信息待核验。");
		
	}
	
	public static Map<String, String> getBookStatus(){
		return BOOKSTATUS;
	}
	
	public static Map<String, String> getIdsType(){
		return IDSTYPE;
	}
	
	public static Map<String, String> getTicketType(){
		return TICKETTYPE;
	}
	
	public static Map<String, String> getSeatType(){
		//座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他
		return SEATTTYPE;
	}
	
	
	public static Map<String, String> getSaleType(){
		return SALETYPE;
	}
	
	
	public static Map<String, String> getPsStatus(){
		return PSSTATUS;
	}
	
	public static Map<String, String> getSeatIdYpMap(){
		return SEATID_YP;
	}
	
	
	public static Map<String, String> getRefundStreamStatus(){
		return REFUND_STREAM_STATUS;
	}
	
	public static Map<String, String> getRefundType(){
		return REFUND_TYPE;
	}
	
	public static Map<String, String> getOutFailReason(){
		return FAIL_REASON;
	}

	public static Object getRefundStatus() {
		return REFUND_STATUS;
	}
}