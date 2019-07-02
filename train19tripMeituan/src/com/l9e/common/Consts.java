package com.l9e.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;


/**
 *  系统常量
 * @author liuyi02
 */
public class Consts {
	public static final String SUCCESS = "success";
	public static final String CHARACTERENCODING = "UTF-8";;
	public static final String FAIL = "fail";
	public static final String FAILURE = "failure";
	
	/***********************同程配置参数**************************/
	public static String TC_OUTNOTIFYURL="";//出票结果确认接口
	public static String TC_BOOKNOTIFYURL="";//预订结果通知接口
	public static String TC_REFUND_NOTIFY_URL="";//线上线下退票通知地址
	public static String TC_CANCELBACKURL="";//取消结果回调地址
	
	public static Double TC_DIFFRATE_LESS_24 = 0.2;//20%
	public static Double TC_DIFFRATE_24_48 = 0.1;//10%
	public static Double TC_DIFFRATE_48_MORE = 0.05;//5%
	
	/***********************美团配置参数***********************/
	public static String MT_KEY = "";
	public static String MT_URL = "";
	
	
	/***********************系统请求路径配置参数**************************/
	public static String NOTIFY_CP_INTERFACE_URL="";//通知出票系统出票地址
	public static String NOTIFY_CP_BACK_URL="";//出票结果接受接口(艺龙渠道)
	public static String NOTIFY_CP_ALLCHANNEL_BACK_URL="";//出票结果接受接口(所有渠道的订单)
	
	public static String NOTIFY_REFUND_INTERFACE_URL="";//通知退票系统退票
	public static String NOTIFY_REFUND_BACK_URL="";//退票结果回调接口
	
	public static String NOTIFY_CP_CANCEL_URL="";//取消订单接口url
	public static String NOTIFY_CP_PAY_URL="";//支付完成通知url
	
	public static String REAL_NAME_VERIFY_URL="";//身份信息核验接口url
	public static String VERIFY_COUNT_URL="";//核验数据统计接口url
	public static String  RESIGN_TICKET_NOTIFY_URL="";//美团请求改签异步回调地址
	public static String  CANCEL_TICKET_NOTIFY_URL="";//美团取消改签异步回调地址
	public static String  CONFIRM_TICKET_NOTIFY_URL="";//美团确认改签异步回调地址
	
	
	public static String QUERY_TICKET="";
	public static String QUERY_TRAIN_TIME="";
	public static String QUERY_PRICE="";
	public static String QUERY_TRAIN_INFO = "";
	/***********************elong订单表状态*************************/
	public static String ELONG_ORDER_DOWN="00";//下单成功
	public static String ELONG_ORDER_ING="11";//通知出票成功
	public static String ELONG_ORDER_MAKED="22";//预订成功
	//public static String ELONG_ORDER_CANCELING="23";//取消预订
	public static String ELONG_ORDER_CANCELED="24";//取消成功
	//public static String ELONG_ORDER_SENDPAY="31";//请求支付-同城
	public static String ELONG_ORDER_WAITPAY="32";//支付中-同城
	public static String ELONG_ORDER_SUCCESS="33";//出票成功
	public static String ELONG_ORDER_FAIL="44";//出票失败
	public static String ELONG_ORDER_BACKING="51";//撤销中
	public static String ELONG_ORDER_BACKFAIL="52";//撤销失败
	public static String ELONG_USER_OFFLINEREFUND="73";//用户生成线下退款(艺龙接口推送)
	public static String ELONG_SYS_OFFLINEREFUND="72";//商旅生成线下退款(运营添加)
	public static String ELONG_DO_OFFLINEREFUND="71";//线下退款中(通知中状态)
	public static String ELONG_OUT_TIME="88";//超时订单(针对同城  同步接口 未返回结果订单变更为超时订单)
	/***********************elong出票结果/退票结果通知表状态*************************/
	public static String NOTICE_START="00";//00、准备通知  11、开始通知  22、通知完成  33、通知失败
	public static String NOTICE_ING="11";// 11、开始通知
	public static String NOTICE_OVER="22";// 22、通知完成 
	public static String NOTICE_FAIL="33";// 33、通知失败
	public static int CP_NOTICE_NUM=6;//  通知出票系统次数
	public static int OUT_NOTICE_NUM=10;// 通知出票结果次数
	public static int REFUND_NOTICE_NUM=10;// 通知退票结果次数
	public static int GOBACK_NOTICE_NUM=6;// 通知退票结果次数
	
	public static int TCBUDANG_NOTICE_NUM=6;// 同程补单通知次数
	public static int TCOUT_NOTICE_NUM=6;// 同程出票结果次数
	public static int TCBOOK_NOTICE_NUM=6;// 同程出票结果次数
	/***********************elong 出票通知类型*************************/
	public static String SUCCESS_NOTICE="0";// 0、出票成功通知
	public static String MAKED_NOTICE="1";//1、预定成功通知  (针对出票通知)
	/********************出票失败原因***************************/
	public static final String OUT_FAIL_REASON_0 = "0";//其他
	public static final String OUT_FAIL_REASON_1 = "1";//所购买的车次坐席已无票
	public static final String OUT_FAIL_REASON_2 = "2";//身份证件已经实名制购票，不能再次购买同日期同车次的车票
	public static final String OUT_FAIL_REASON_3 = "3";//票价和12306不符
	public static final String OUT_FAIL_REASON_4 = "4";//车次数据与12306不一致
	public static final String OUT_FAIL_REASON_5 = "5";//乘客信息错误
	public static final String OUT_FAIL_REASON_6 = "6";//12306乘客身份信息核验失败
	/********************各个渠道编号***************************/
	public static final String CHANNEL_TONGCHENG = "tongcheng";//同程
	public static final String CHANNEL_MEITUAN = "meituan";//美团
	/************退款类型 11、退票退款 22、线下退款（人工退款）    */
	public static String ELONG_REFUNDTYPE_TICKET="11";//退票
	public static String ELONG_REFUNDTYPE_AMOUNT="22";//线下退款
	/************自拟定通知出票系统出票优先级*/
	public static int CP_NOTIFY_LEVEL5=5;//默认(异步返回模式)
	public static int CP_NOTIFY_LEVEL10=10;//同步返回
	/************退款状态  */
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票 
	//05：重新机器退票06：开始机器退票   
	//07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款  55:预退票
	public static String ELONG_REFUNDSTATUS_SUCESS="11";//退票成功
	public static String ELONG_REFUNDSTATUS_FAIL="22";//拒绝退票

	
	public static final List<String[]> REFUND_STATUS=new ArrayList<String[]>();
	static{
		REFUND_STATUS.add(new String[]{"00","准备退款"});
		REFUND_STATUS.add(new String[]{"11","等待退款"});
		REFUND_STATUS.add(new String[]{"22","开始退款"});
		REFUND_STATUS.add(new String[]{"33","EOP退款中"});
		REFUND_STATUS.add(new String[]{"44","退款完成"});
		REFUND_STATUS.add(new String[]{"55","拒绝退款"});
		REFUND_STATUS.add(new String[]{"66","eop审核失败"});
	}
	/*******************改签状态***********************************/
	public static final String TRAIN_REQUEST_CHANGE = "11";//改签预订
	public static final String TRAIN_REQUEST_CHANGE_ING = "12";//正在改签预订
	public static final String TRAIN_REQUEST_CHANGE_ARTIFICIAL = "13";// 人工改签预订
	public static final String TRAIN_REQUEST_CHANGE_SUCCESS = "14";//改签成功等待确认
	public static final String TRAIN_REQUEST_CHANGE_FAIL = "15";//改签预订失败
	public static final String TRAIN_CANCEL_CHANGE = "21";//改签取消
	public static final String TRAIN_CANCEL_CHANGE_ING = "22";// 正在取消
	public static final String TRAIN_CANCEL_CHANGE_SUCCESS = "23";//取消成功
	public static final String TRAIN_CANCEL_CHANGE_FAIL = "24";//取消失败
	public static final String TRAIN_CONFIRM_CHANGE = "31";//开始支付
	public static final String TRAIN_CONFIRM_CHANGE_PAY = "32";//正在支付
	public static final String TRAIN_CONFIRM_CHANGE_PAY_ARTIFICIAL = "33";//人工支付
	public static final String TRAIN_CONFIRM_CHANGE_SUCCESS = "34";//支付成功
	public static final String TRAIN_CONFIRM_CHANGE_FAIL = "35";//支付失败
	public static final String TRAIN_CONFIRM_CHANGE_START_PAY = "36";//补价支付
	
	/***************************改签回调状态*******************************/
	public static final String CHANGE_NOTIFY_PRE = "000";//准备异步回调
	public static final String CHANGE_NOTIFY_START = "111";//开始异步回调
	public static final String CHANGE_NOTIFY_OVER = "222";//回调完成
	public static final String CHANGE_NOTIFY_FAIL = "333";//回调失败
}
