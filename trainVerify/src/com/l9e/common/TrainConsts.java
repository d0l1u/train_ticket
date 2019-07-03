package com.l9e.common;


/**
 * 常量
 * @author zhangjun
 *
 */
public class TrainConsts {
	
	
	public static final String SUCCESS = "success";
	
	public static final String FAIL = "fail";
	
	public static final String FAILURE = "failure";
	
	/*******************订单状态***********************/
	public static final String PAY_SUCCESS = "11";//支付成功
	public static final String BOOK_SUCCESS = "33";//预订成功
	public static final String OUT_SUCCESS = "44";//出票成功	
	public static final String OUT_FAIL = "45";//出票失败
	
	/********************订单类别*************************/
	public static final String ORDER_TYPE_COMMON = "0";//普通订单
	public static final String ORDER_TYPE_TRIP = "1";//联程订单
	
	/********************退票状态*****************************/
	public static final String REFUND_WAITING = "00";//等待退票
	public static final String REFUND_AGREE = "11";//同意退票
	public static final String REFUND_REFUSE = "22";//拒绝退款	
	
	/********************拒绝退票原因***************************/
	public static final String REFUSE_REASON_1 = "1";//已取票
	public static final String REFUSE_REASON_2 = "2";//已过时间
	public static final String REFUSE_REASON_3 = "3";//来电取消
	
	/********************出票失败原因***************************/
	
	public static final String OUT_FAIL_REASON_0 = "0";//其他
	public static final String OUT_FAIL_REASON_1 = "1";//所购买的车次坐席已无票
	public static final String OUT_FAIL_REASON_2 = "2";//身份证件已经实名制购票，不能再次购买同日期同车次的车票
	public static final String OUT_FAIL_REASON_3 = "3";//qunar票价和12306不符
	public static final String OUT_FAIL_REASON_4 = "4";//车次数据与12306不一致
	public static final String OUT_FAIL_REASON_5 = "5";//乘客信息错误
	public static final String OUT_FAIL_REASON_6 = "6";//12306乘客身份信息核验失败
	
}
