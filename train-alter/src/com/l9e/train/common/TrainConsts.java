package com.l9e.train.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 常量
 * 
 * @author wangsf
 *
 */
public class TrainConsts {

	/**
	 * 301 : 没有余票
	 */
	public static String FAILCODE_NOTICKET = "301"; // 没有余票
	/**
	 * 308 : 存在未完成订单
	 */
	public static String FAILCODE_HAVEUNFINISHEDORDER = "308"; // 存在未完成订单
	/**
	 * 310 : 本次购票与其它订单行程冲突
	 */
	public static String FAILCODE_TRAVELCONFLICT = "310"; // 本次购票与其它行程冲突
	/**
	 * 313 : 法院依法限制高消费，禁止乘坐列车**座位
	 */
	public static String FAILCODE_HIGHCONSUME = "313"; // 高消费
	/**
	 * 314 : 账号登陆失败
	 */
	public static String FAILCODE_LOGINFAIL = "314"; // 账号登陆失败
	/**
	 * 315 : 未找到要改签的车次
	 */
	public static String FAILCODE_UNFINDTRAINNO = "315"; // 未找到要改签的车次
	/**
	 * 316 : 不允许改签到指定时间的车票
	 */
	public static String FAILCODE_APPOINTTIME = "316"; // 不允许改签到指定时间的车票
	/**
	 * 317 : 已出票，不可改签
	 */
	public static String FAILCODE_YETOUTTICKET = "317"; // 已出票，不可改签
	/**
	 * 318 : 已退票，不可改签
	 */
	public static String FAILCODE_YETRETURNTICKET = "318"; // 已退票，不可改签
	/**
	 * 319 : 已改签，不可改签
	 */
	public static String FAILCODE_YETALTERTICKET = "319"; // 已改签，不可改签
	/**
	 * 320 : 不存在改签待支付订单
	 */
	public static String FAILCODE_NOPAYMENTORDER = "320"; // 不存在改签待支付订单
	/**
	 * 322 : 排队购票人数过多
	 */
	public static String FAILCODE_LISTTOOMUCH = "322"; // 排队购票人数过多
	/**
	 * 324 : 已确认改签，不可取消
	 */
	public static String FAILCODE_VERIFYALTER = "324"; // 已确认改签，不可取消
	/**
	 * 506 : 系统错误
	 */
	public static String FAILCODE_SYSTEMERROR = "506"; // 系统错误
	/**
	 * 999 : 所有未定义的12306错误
	 */
	public static String FAILCODE_UNDEFINEDERROR = "999"; // 未定义的12306错误
	/**
	 * 1002 : 距离开车时间太近，无法改签
	 */
	public static String FAILCODE_DRIVETIMECLOSE = "1002"; // 距开车时间太近
	/**
	 * 1004 : 取消改签次数超过上限
	 */
	public static String FAILCODE_CANCELUPTOLIMIT = "1004"; // 取消改签次数超过上限
	/**
	 * 9991 : 旅游票请到车站办理改签
	 */
	public static String FAILCODE_TRAVELTICKET = "9991"; // 旅游票请到车站办理改签

	/**
	 * 改签支付类型 1：平改
	 */
	public static Integer ALTER_PAY_TYPEONE = 1; // 1：平改

	/**
	 * 改签支付类型 2：高改低
	 */
	public static Integer ALTER_PAY_TYPESECOND = 2; // 2：高改低

	/**
	 * 改签支付类型 3：低改高
	 */
	public static Integer ALTER_PAY_TYPETHREE = 3; // 3：低改高

	public static String ORDER_ERROR = "1001";
	public static String ORDER_SUCCESS = "0000";

	public static final String ROBOT_REFUND_STATUS00 = "00"; // 等待机器改签
	public static final String ROBOT_REFUND_STATUS02 = "02"; // 开始机器改签
	public static final String ROBOT_REFUND_STATUS03 = "03"; // 人工改签
	public static final String ROBOT_REFUND_STATUS04 = "04"; // 等待机器退票
	public static final String ROBOT_REFUND_STATUS05 = "05"; // 重新机器退票
	public static final String ROBOT_REFUND_STATUS06 = "06"; // 开始机器退票
	public static final String ROBOT_REFUND_STATUS07 = "07"; // 人工退票
	public static final String ROBOT_REFUND_STATUS22 = "22"; // 拒绝退票
	public static final String ROBOT_REFUND_STATUS33 = "33"; // 人工审核机器退款结果

	public static final String SEAT_TYPE0 = "9"; // 商务座
	public static final String SEAT_TYPE1 = "P"; // 特等座
	public static final String SEAT_TYPE2 = "M"; // 一等座
	public static final String SEAT_TYPE3 = "O"; // 二等座
	public static final String SEAT_TYPE4 = "6"; // 高级软座
	public static final String SEAT_TYPE5 = "4"; // 软卧
	public static final String SEAT_TYPE6 = "3"; // 硬卧
	public static final String SEAT_TYPE7 = "2"; // 软座
	public static final String SEAT_TYPE8 = "1"; // 硬座
	public static final String SEAT_TYPE10 = ""; // 无座 动车无座为二等座
	public static final String SEAT_TYPE11 = "5"; // 包厢硬卧
	public static final String SEAT_TYPE12 = "F"; // 动卧

	public static final Integer WORKER_TYPE_ALTER = 7;
	public static final Integer WORKER_TYPE_REFUND = 8;

	private static Map<String, String> qunar_seattype = new LinkedHashMap<String, String>();// 坐席

	public static Map<String, String> getQunarSeatType() {
		return qunar_seattype;
	}

	static {
		qunar_seattype.put(SEAT_TYPE0, "0");
		qunar_seattype.put(SEAT_TYPE1, "1");
		qunar_seattype.put(SEAT_TYPE2, "2");
		qunar_seattype.put(SEAT_TYPE3, "3");
		qunar_seattype.put(SEAT_TYPE4, "4");
		qunar_seattype.put(SEAT_TYPE5, "5");
		qunar_seattype.put(SEAT_TYPE6, "6");
		qunar_seattype.put(SEAT_TYPE7, "7");
		qunar_seattype.put(SEAT_TYPE8, "8");
		qunar_seattype.put(SEAT_TYPE10, "");
		qunar_seattype.put(SEAT_TYPE11, "5");
		qunar_seattype.put(SEAT_TYPE12, "20");
	}

}
