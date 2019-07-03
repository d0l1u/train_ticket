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
	
	/*******************订单状态***********************/
	public static final String PRE_ORDER = "00";//预下单	
	public static final String PAY_SUCCESS = "11";//支付成功
	public static final String EOP_SEND = "12";//EOP发货
	public static final String BOOKING_TICKET = "22";//正在预订	
	public static final String BOOK_SUCCESS = "33";//预订成功
	public static final String OUT_SUCCESS = "44";//出票成功	
	public static final String OUT_FAIL = "45";//出票失败
	public static final String ORDER_FINISH = "88";//订单完成
	public static final String PAY_FAIL = "99";//订单完成
	
	/********************退款状态*****************************/
	public static final String REFUND_PRE_REFUND = "55";//准备退款
	public static final String REFUND_REFUNDING = "66";//正在退款	
	public static final String REFUND_EOP_REFUNDING = "67";//EOP正在退款
	public static final String REFUND_REFUND_FINISH = "77";//退票完成	
	public static final String REFUND_REFUSE = "88";//拒绝退款	
	
	/********************差价退款状态*****************************/
	public static final String DIFFER_PRE_REFUND = "00";//准备退款
	public static final String DIFFER_WAIT_REFUND = "11";//等待退款
	public static final String DIFFER_BEGIN_REFUND = "22";//开始退款
	public static final String DIFFER_EOP_REFUNDING = "33";//EOP退款中
	public static final String DIFFER_REFUND_FINISH = "44";//退款完成
	
	/********************退款流水状态*******************************/
	public static final String REFUND_STREAM_PRE_REFUND = "00";//准备退款
	public static final String REFUND_STREAM_WAIT_REFUND = "11";//等待退款
	public static final String REFUND_STREAM_BEGIN_REFUND = "22";//开始退款
	public static final String REFUND_STREAM_EOP_REFUNDING = "33";//19pay退款中
	public static final String REFUND_STREAM_EOP_FAIL = "34";//19pay退款申请失败
	public static final String REFUND_STREAM_REFUND_FINISH = "44";//退款完成
	public static final String REFUND_STREAM_REFUSE= "55";//退款完成
	
	/********************退款类型***************************************/
	public static final String REFUND_TYPE_1 = "1";//用户退款
	public static final String REFUND_TYPE_2 = "2";//差额退款
	public static final String REFUND_TYPE_3 = "3";//出票失败退款
	public static final String REFUND_TYPE_4 = "4";//改签差额退款
	public static final String REFUND_TYPE_5 = "5";//改签单退款
	
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
	
	//初始化
	static {
		BOOKSTATUS.put(PRE_ORDER, "预下单");//预下单
		BOOKSTATUS.put(PAY_SUCCESS, "支付成功");//支付成功
		BOOKSTATUS.put(EOP_SEND, "出票中");//支付成功
		BOOKSTATUS.put(BOOKING_TICKET, "出票中");//正在预订
		BOOKSTATUS.put(BOOK_SUCCESS, "预订成功");//预订成功
		BOOKSTATUS.put(OUT_SUCCESS, "出票成功");//出票成功
		BOOKSTATUS.put(OUT_FAIL, "出票失败");//出票失败
		BOOKSTATUS.put(ORDER_FINISH, "订单完成");//订单完成
		BOOKSTATUS.put(PAY_FAIL, "支付失败");//支付失败
		
		REFUND_STREAM_STATUS.put(REFUND_STREAM_PRE_REFUND, "退款申请中");//准备退款
		REFUND_STREAM_STATUS.put(REFUND_STREAM_WAIT_REFUND, "正在退款");//等待退款
		REFUND_STREAM_STATUS.put(REFUND_STREAM_BEGIN_REFUND, "正在退款");//开始退款
		REFUND_STREAM_STATUS.put(REFUND_STREAM_EOP_REFUNDING, "正在退款");//EOP退款中
		REFUND_STREAM_STATUS.put(REFUND_STREAM_REFUND_FINISH, "退款完成");//退款完成
		REFUND_STREAM_STATUS.put(REFUND_STREAM_REFUSE, "拒绝退款");//拒绝退款
		
		REFUND_TYPE.put(REFUND_TYPE_1, "用户申请退款");
		REFUND_TYPE.put(REFUND_TYPE_2, "差额退款");
		REFUND_TYPE.put(REFUND_TYPE_3, "出票失败退款");
		REFUND_TYPE.put(REFUND_TYPE_4, "改签差额退款");
		REFUND_TYPE.put(REFUND_TYPE_5, "改签单退款");
		
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
//		QUESTIONTYPE.put(QUESTION_TYPE_1, "加盟问题");
//		QUESTIONTYPE.put(QUESTION_TYPE_2, "配送问题");
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
		
		SHOP_TYPE.put(SHOP_TYPE_0, "手机充值店");
		SHOP_TYPE.put(SHOP_TYPE_1, "鲜花礼品店");
		SHOP_TYPE.put(SHOP_TYPE_2, "小型超市");
		SHOP_TYPE.put(SHOP_TYPE_3, "大型超市");
		SHOP_TYPE.put(SHOP_TYPE_4, "烟酒店");
		SHOP_TYPE.put(SHOP_TYPE_5, "报刊亭");
		SHOP_TYPE.put(SHOP_TYPE_6, "票务代售点");
		SHOP_TYPE.put(SHOP_TYPE_7, "彩票代售点");
		SHOP_TYPE.put(SHOP_TYPE_8, "旅行社");
		SHOP_TYPE.put(SHOP_TYPE_10, "网吧");
		SHOP_TYPE.put(SHOP_TYPE_9, "其他");
		
		FAIL_REASON.put(OUT_FAIL_REASON_1, "您所购买的车次坐席已无票。");
		FAIL_REASON.put(OUT_FAIL_REASON_2, "身份证件已经实名制购票，不能再次购买同日期同车次的车票。");
		FAIL_REASON.put(OUT_FAIL_REASON_3, "票价和12306不符。");
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
	
	public static Map<String, String> getOutFailReason(){
		return FAIL_REASON;
	}
}
