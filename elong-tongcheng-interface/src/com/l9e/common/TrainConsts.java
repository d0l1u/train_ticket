package com.l9e.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 常量
 * @author zhangjun
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
	
	/********************订单处理结果通知退款类型***************************************/
	public static final String ORDER_RETURN_TYPE_1 = "001";//差额退款
	public static final String ORDER_RETURN_TYPE_2 = "002";//出票失败退款
	
	/*******************订单状态***********************/
	public static final String NO_PAY = "00";//未支付
	public static final String PAY_SUCCESS = "11";//支付成功
	public static final String EOP_SEND = "12"; //eop发货
	public static final String BOOKING_TICKET = "22";//正在预订	
	public static final String BOOK_SUCCESS = "33";//预订成功
	public static final String OUT_SUCCESS = "44";//出票成功	
	public static final String OUT_FAIL = "45";//出票失败
	public static final String ORDER_FINISH = "88";//订单完成
	
	/********************退款流水状态*******************************/
	public static final String REFUND_STATUS_WAIT = "00";//等待退票
	public static final String REFUND_STATUS_AGREE = "11";//同意退票
	public static final String REFUND_STATUS_REFUSE= "22";//拒绝退票
	public static final String REFUND_STATUS_SUCCESS = "33";//退票完成
	public static final String REFUND_STATUS_FAIL= "44";//退票失败
	
	/*********************退款通知状态*************************************/
	public static final String REFUND_NOTIFY_NOT = "00";//未通知
	public static final String REFUND_NOTIFY_PREPARE = "11";//准备通知
	public static final String REFUND_NOTIFY_BEGIN = "22";//开始通知
	public static final String REFUND_NOTIFY_FINISH = "33";//通知完成
	
	/********************退款类型***************************************/
	public static final String REFUND_TYPE_ONLINE = "1";//用户线上退款
	public static final String REFUND_TYPE_STATION = "2";//用户车站退款
	public static final String REFUND_TYPE_ALTER = "3";//改签单退款
	public static final String REFUND_TYPE_DIFF = "4";//差额退款
	public static final String REFUND_TYPE_FAIL = "5";//出票失败退款
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
	
	
	/********************出票失败原因***************************/
	public static final String OUT_FAIL_REASON_1 = "1";//所购买的车次坐席已无票
	public static final String OUT_FAIL_REASON_2 = "2";//身份证件已经实名制购票，不能再次购买同日期同车次的车票
	public static final String OUT_FAIL_REASON_3 = "3";//票价和12306不符
	public static final String OUT_FAIL_REASON_4 = "4";//乘车时间异常
	public static final String OUT_FAIL_REASON_5 = "5";//证件错误
	public static final String OUT_FAIL_REASON_6 = "6";//用户要求取消订单
	public static final String OUT_FAIL_REASON_7 = "7";//未通过12306实名认证
	public static final String OUT_FAIL_REASON_8 = "8";//乘客身份信息待核验
	
	/*********************状态码************************************/
	public static final String RETURN_CODE_000 = "000";
	public static final String RETURN_CODE_001 = "001";
	public static final String RETURN_CODE_002 = "002";
	public static final String RETURN_CODE_003 = "003";
	public static final String RETURN_CODE_004 = "004";
	public static final String RETURN_CODE_005 = "005";
	public static final String RETURN_CODE_006 = "006";
	public static final String RETURN_CODE_007 = "007";
	public static final String RETURN_CODE_101 = "101";
	public static final String RETURN_CODE_102 = "102";
	public static final String RETURN_CODE_103 = "103";
	public static final String RETURN_CODE_104 = "104";
	public static final String RETURN_CODE_105 = "105";
	public static final String RETURN_CODE_106 = "106";
	public static final String RETURN_CODE_107 = "107";
	public static final String RETURN_CODE_201 = "201";
	public static final String RETURN_CODE_202 = "202";
	public static final String RETURN_CODE_203 = "203";
	public static final String RETURN_CODE_204 = "204";
	public static final String RETURN_CODE_205 = "205";
	public static final String RETURN_CODE_301 = "301";
	public static final String RETURN_CODE_401 = "401";
	public static final String RETURN_CODE_402 = "402";
	public static final String RETURN_CODE_501 = "501";
	public static final String RETURN_CODE_601 = "601";
	public static final String RETURN_CODE_602 = "602";
	public static final String RETURN_CODE_701 = "701";

	private static Map<String, String> BOOKSTATUS = new LinkedHashMap<String, String>();//订单状态
	private static Map<String, String> REFUND_STREAM_STATUS = new HashMap<String, String>();//退款流水状态
	private static Map<String, String> REFUND_TYPE = new HashMap<String, String>();//退款类型
	private static Map<String, String> IDSTYPE = new LinkedHashMap<String, String>();//身份证
	private static Map<String, String> TICKETTYPE = new LinkedHashMap<String, String>();//票类型
	private static Map<String, String> SEATTTYPE = new LinkedHashMap<String, String>();//坐席
	private static Map<String, String> AGENTESTATE = new LinkedHashMap<String, String>();//代理商审核状态
	private static Map<String, String> SALETYPE = new LinkedHashMap<String, String>();//计费方式
	private static Map<String, String> QUESTIONTYPE = new LinkedHashMap<String, String>();//投诉建议问题类型
	private static Map<String, String> PSSTATUS = new LinkedHashMap<String, String>();//配送状态
	private static Map<String, String> SEATID_YP = new HashMap<String, String>();//坐席和余票映射
	private static Map<String, String> SHOP_TYPE = new HashMap<String, String>();//店铺类型
	private static Map<String, String> FAIL_REASON = new HashMap<String, String>();//出票失败原因
	
	private static Map<String, String> EXTBOOKSTATUS = new LinkedHashMap<String, String>();//对外订单状态
	private static Map<String, String> EXTREFUND_TYPE = new HashMap<String, String>();//对外退款类型
	private static Map<String, String> RETURNCODE_TYPE = new HashMap<String, String>();//对外接口状态码说明
	//初始化
	static {
		BOOKSTATUS.put(PAY_SUCCESS, "支付成功");//支付成功
		BOOKSTATUS.put(BOOKING_TICKET, "出票中");//正在预订
		BOOKSTATUS.put(BOOK_SUCCESS, "预订成功");//预订成功
		BOOKSTATUS.put(OUT_SUCCESS, "出票成功");//出票成功
		BOOKSTATUS.put(OUT_FAIL, "出票失败");//出票失败
		BOOKSTATUS.put(ORDER_FINISH, "订单完成");//订单完成
		
		REFUND_TYPE.put(REFUND_TYPE_ONLINE, "用户线上退款");
		REFUND_TYPE.put(REFUND_TYPE_STATION, "用户车站退款");
		REFUND_TYPE.put(REFUND_TYPE_ALTER, "改签单退款");
		REFUND_TYPE.put(REFUND_TYPE_DIFF, "差额退款");
		REFUND_TYPE.put(REFUND_TYPE_FAIL, "出票失败退款");
		
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
		
		AGENTESTATE.put(AGENT_ESTATE_NEED_PAY, "需要付费");
		AGENTESTATE.put(AGENT_ESTATE_WAITING, "等待审核");
		AGENTESTATE.put(AGENT_ESTATE_NOT, "审核未通过");
		AGENTESTATE.put(AGENT_ESTATE_PASSED, "审核通过");
		AGENTESTATE.put(AGENT_ESTATE_NEED_REPAY, "需要续费");
		
		SALETYPE.put(SALE_TYPE_MONTH, "元/月");
		SALETYPE.put(SALE_TYPE_YEAR, "元/年");
		
		QUESTIONTYPE.put(QUESTION_TYPE_0, "订单问题");
		QUESTIONTYPE.put(QUESTION_TYPE_1, "加盟问题");
		QUESTIONTYPE.put(QUESTION_TYPE_2, "配送问题");
		QUESTIONTYPE.put(QUESTION_TYPE_3, "出票问题");
		QUESTIONTYPE.put(QUESTION_TYPE_4, "业务建议");
		QUESTIONTYPE.put(QUESTION_TYPE_5, "其他");
		
		
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
		
		
		EXTBOOKSTATUS.put(PAY_SUCCESS, "BOOKING");
		EXTBOOKSTATUS.put(BOOKING_TICKET, "BOOKING");
		EXTBOOKSTATUS.put(BOOK_SUCCESS, "BOOK_SUCCESS");
		EXTBOOKSTATUS.put(OUT_SUCCESS, "OUT_SUCCESS");
		EXTBOOKSTATUS.put(OUT_FAIL, "OUT_FAIL");
		
		EXTREFUND_TYPE.put(REFUND_STATUS_WAIT, "REFUNDING");
		EXTREFUND_TYPE.put(REFUND_STATUS_AGREE, "REFUNDING");
		EXTREFUND_TYPE.put(REFUND_STATUS_REFUSE, "REFUSE_REFUND");
		
		RETURNCODE_TYPE.put(RETURN_CODE_000, "");
		RETURNCODE_TYPE.put(RETURN_CODE_001, "系统错误，未知服务异常。");
		RETURNCODE_TYPE.put(RETURN_CODE_002, "安全验证错误，不符合安全校验规则。");
		RETURNCODE_TYPE.put(RETURN_CODE_003, "输入参数为空或错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_004, "非授权合作商户。");
		RETURNCODE_TYPE.put(RETURN_CODE_005, "备注不能超过100个字符。");
		RETURNCODE_TYPE.put(RETURN_CODE_006, "访问限制，访问频率过高，被禁止。");
		RETURNCODE_TYPE.put(RETURN_CODE_007, "合作商户余额不足，暂时停用，请充值后通知服务商重新启用！");
		RETURNCODE_TYPE.put(RETURN_CODE_101, "余票查询参数输入错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_102, "输入车站名为空，请确认!");
		RETURNCODE_TYPE.put(RETURN_CODE_103, "出发车站错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_104, "到达车站错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_105, "输入车站名错误，请确认");
		RETURNCODE_TYPE.put(RETURN_CODE_106, "查询返回数据失败，暂无列车信息");
		RETURNCODE_TYPE.put(RETURN_CODE_107, "车次信息有误，暂无该列车信息");
		RETURNCODE_TYPE.put(RETURN_CODE_201, "重复下单异常");
		RETURNCODE_TYPE.put(RETURN_CODE_202, "不在受理时间内，拒绝下订单操作，请在规定时间执行此操作。");
		RETURNCODE_TYPE.put(RETURN_CODE_203, "一个订单最多允许订购五张票。");
		RETURNCODE_TYPE.put(RETURN_CODE_204, "下单参数为空或者错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_205, "订单内乘客信息参数为空或者错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_301, "订单未找到（订单号错误或不存在）");
		RETURNCODE_TYPE.put(RETURN_CODE_401, "订单已出票成功，无法取消，请走退票退款流程。");
		RETURNCODE_TYPE.put(RETURN_CODE_402, "不在受理退款时间内，拒绝取消订单操作, 请在规定时间执行此操作。");
		RETURNCODE_TYPE.put(RETURN_CODE_501, "暂时没有该途经站信息。");
		RETURNCODE_TYPE.put(RETURN_CODE_601, "支付金额与下单时支付金额不符。");
		
	}
	
	public static Map<String,String> getReturnCode(){
		return RETURNCODE_TYPE;
	}
	public static Map<String, String> getBookStatus(){
		return BOOKSTATUS;
	}
	
	public static Map<String, String> getExtBookStatus(){
		return EXTBOOKSTATUS;
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
	
	public static Map<String, String> getAgentEstate(){
		return AGENTESTATE;
	}
	
	public static Map<String, String> getSaleType(){
		return SALETYPE;
	}
	
	public static Map<String, String> getQuestionType(){
		return QUESTIONTYPE;
	}
	
	public static Map<String, String> getPsStatus(){
		return PSSTATUS;
	}
	
	public static Map<String, String> getSeatIdYpMap(){
		return SEATID_YP;
	}
	
	public static Map<String, String> getShopType(){
		return SHOP_TYPE;
	}
	
	public static Map<String, String> getRefundStreamStatus(){
		return REFUND_STREAM_STATUS;
	}
	
	public static Map<String, String> getRefundType(){
		return REFUND_TYPE;
	}
	
	public static Map<String, String> getExtRefundType(){
		return EXTREFUND_TYPE;
	}
	
	public static Map<String, String> getOutFailReason(){
		return FAIL_REASON;
	}
}
