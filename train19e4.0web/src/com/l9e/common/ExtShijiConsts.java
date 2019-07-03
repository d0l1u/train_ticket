package com.l9e.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExtShijiConsts {
	
	public static final String INF_LOGIN_USER = "loginUser";
	
	public static final String INF_SYS_CONF = "sysConf";
	
	public static final String SUCCESS = "success";
	
	public static final String FAIL = "fail";
	
	public static final String FAILURE = "failure";
	
	/*******************订单状态***********************/
	//订单状态 00、未支付 11、支付成功 12、eop发货成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支付失败
	public static final String PRE_ORDER = "00";//未支付
	public static final String PAY_SUCCESS = "11";//支付成功
	public static final String EOP_SEND = "12";//EOP发货
	public static final String BOOKING_TICKET = "22";//正在预订	
	public static final String BOOK_SUCCESS = "33";//预订成功
	public static final String OUT_SUCCESS = "44";//出票成功	
	public static final String OUT_FAIL = "45";//出票失败
	public static final String ORDER_FINISH = "88";//订单完成
	public static final String PAY_FAIL = "99";//支付失败
	
	/********************退款流水状态*******************************/
	//退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败
	public static final String REFUND_STREAM_PRE_REFUND = "00";//准备退款
	public static final String REFUND_STREAM_GEZHI = "99";//搁置订单
	public static final String REFUND_STREAM_WAIT_REFUND = "11";//等待退款
	public static final String REFUND_STREAM_BEGIN_REFUND = "22";//开始退款
	public static final String REFUND_STREAM_EOP_REFUNDING = "33";//EOP退款中
	public static final String REFUND_STREAM_REFUND_FINISH = "44";//退款完成
	//public static final String REFUND_STREAM_REFUSE= "55";//拒绝退款
	
	/********************退款类型***************************************/
	//退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款
	public static final String REFUND_TYPE_1 = "1";//用户退款
	public static final String REFUND_TYPE_2 = "2";//差额退款
	public static final String REFUND_TYPE_3 = "3";//出票失败退款
	public static final String REFUND_TYPE_4 = "4";//改签差额退款
	public static final String REFUND_TYPE_5 = "5";//改签单退款
	
	/********************出票失败原因***************************/
	public static final String OUT_FAIL_REASON_1 = "1";//所购买的车次坐席已无票
	public static final String OUT_FAIL_REASON_2 = "2";//身份证件已经实名制购票，不能再次购买同日期同车次的车票
	public static final String OUT_FAIL_REASON_3 = "3";//票价和12306不符
	public static final String OUT_FAIL_REASON_4 = "4";//乘车时间异常
	public static final String OUT_FAIL_REASON_5 = "5";//证件错误
	public static final String OUT_FAIL_REASON_6 = "6";//用户要求取消订单
	public static final String OUT_FAIL_REASON_7 = "7";//未通过12306实名认证
	public static final String OUT_FAIL_REASON_8 = "8";//乘客身份信息待核验
	/********************支付类型***************************************/
	//支付类型：0 钱包 1 支付宝
	public static final String PAY_TYPE_1 = "0"; 
	public static final String PAY_TYPE_2 = "1"; 
	
	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();//订单状态
	private static Map<String, String> REFUND_STREAM_STATUS = new HashMap<String, String>();//退款流水状态
	private static Map<String, String> REFUND_TYPE = new HashMap<String, String>();//退款类型
	private static Map<String, String> FAIL_REASON = new HashMap<String, String>();//出票失败原因
	private static Map<String, String> PAY_TYPE = new HashMap<String, String>();//支付类型
	
	//初始化
	static {
		//订单状态 00、未支付 11、支付成功 12、eop发货成功 22、正在预订 33、预订成功 44、出票成功 45、出票失败 88、订单完成 99、支付失败
		BOOKSTATUS.put(PRE_ORDER, "未支付");//预下单
		BOOKSTATUS.put(PAY_SUCCESS, "出票中");//支付成功
		BOOKSTATUS.put(EOP_SEND, "出票中");//支付成功
		BOOKSTATUS.put(BOOKING_TICKET, "出票中");//正在预订
		BOOKSTATUS.put(BOOK_SUCCESS, "出票中");//预订成功
		BOOKSTATUS.put(OUT_SUCCESS, "出票成功");//出票成功
		BOOKSTATUS.put(OUT_FAIL, "出票失败");//出票失败
		BOOKSTATUS.put(ORDER_FINISH, "订单完成");//订单完成
		BOOKSTATUS.put(PAY_FAIL, "支付失败");//支付失败
		
		//退款状态：00、等待退票 11、同意退票 22、拒绝退票 33、退票完成      44、退票失败
		REFUND_STREAM_STATUS.put(REFUND_STREAM_PRE_REFUND, "正在退款");//准备退款
		REFUND_STREAM_STATUS.put(REFUND_STREAM_GEZHI, "正在退款");//搁置订单
		REFUND_STREAM_STATUS.put(REFUND_STREAM_WAIT_REFUND, "正在退款");//等待退款
		REFUND_STREAM_STATUS.put(REFUND_STREAM_BEGIN_REFUND, "退款失败");//开始退款
		REFUND_STREAM_STATUS.put(REFUND_STREAM_EOP_REFUNDING, "退款完成");//EOP退款中
		REFUND_STREAM_STATUS.put(REFUND_STREAM_REFUND_FINISH, "退款失败");//退款完成
		//REFUND_STREAM_STATUS.put(REFUND_STREAM_REFUSE, "拒绝退款");//拒绝退款
		
		//退款类型：1、用户线上退票 2、用户车站退票 3、改签单退款 4、差额退款 5、出票失败退款
		REFUND_TYPE.put(REFUND_TYPE_1, "用户申请退款");
		REFUND_TYPE.put(REFUND_TYPE_2, "用户车站退票");
		REFUND_TYPE.put(REFUND_TYPE_3, "改签单退款");
		REFUND_TYPE.put(REFUND_TYPE_4, "差额退款");
		REFUND_TYPE.put(REFUND_TYPE_5, "出票失败退款");
		
		//失败原因
		FAIL_REASON.put(OUT_FAIL_REASON_1, "您所购买的车次坐席已无票。");
		FAIL_REASON.put(OUT_FAIL_REASON_2, "身份证件已经实名制购票，不能再次购买同日期同车次的车票。");
		FAIL_REASON.put(OUT_FAIL_REASON_3, "票价与铁路系统不符。");
		FAIL_REASON.put(OUT_FAIL_REASON_4, "乘车时间异常。");
		FAIL_REASON.put(OUT_FAIL_REASON_5, "证件错误。");
		FAIL_REASON.put(OUT_FAIL_REASON_6, "用户要求取消订单。");
		FAIL_REASON.put(OUT_FAIL_REASON_7, "未通过铁路系统实名认证。");
		FAIL_REASON.put(OUT_FAIL_REASON_8, "乘客身份信息待核验。");
		
		//支付类型：0 钱包 1 支付宝
		PAY_TYPE.put(PAY_TYPE_1, "19e钱包");
		PAY_TYPE.put(PAY_TYPE_2, "支付宝");
	}
	
	public static Map<String, String> getPayType(){
		return PAY_TYPE;
	}
	
	public static Map<String, String> getBookStatus(){
		return BOOKSTATUS;
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
}
