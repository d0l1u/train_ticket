package com.l9e.train.po;

/**
 * 订单错误返回信息实体类
 * @author wangsf
 *
 */
public class ErrorInfo {
	
	/**
	 * 1 : 所购买的车次坐席已无票
	 */
	public static final String WITHOUT_TICKET= "1";
	
	/**
	 * 2 : 身份证件已经实名制购票
	 */
	public static final String YET_BUY_TICKET= "2";
	
	/**
	 * 3 : 票价和12306不符
	 */
	public static final String TICKET_PRICE_WRONG= "3";
	
	/**
	 * 4 : 乘车时间异常
	 */
	public static final String RIDING_TIME_EXCEPTION= "4";
	
	/**
	 * 5 : 证件错误
	 */
	public static final String PAPERS_WRONG= "5";
	
	/**
	 * 6 : 用户要求取消订单 
	 */
	public static final String CANCEL_ORDER= "6";
	
	/**
	 * 7 : 未通过12306实名认证 
	 */
	public static final String NOPASS_REALNAME_VERIFY= "7";
	
	/**
	 * 8 : 乘客身份信息待核验
	 */
	public static final String PASSENGER_NO_VERIFY= "8";
	
	/**
	 * 9 : 系统异常
	 */
	public static final String SYSTEM_EXCEPTION= "9";
	
	/**
	 * 10 : 高消费限制失败
	 */
	public static final String HIGH_CONSUMPTION_LIMITFAIL= "10";
	
	/**
	 * 11 : 乘客超时未支付
	 */
	public static final String TIMEOUT_NO_PAY= "11";
	
	/**
	 * 12 : 信息冒用
	 */
	public static final String INFORMATION_ILLEGAL_USE= "12";
	
	
	//下面属于途牛返回的错误信息
	
	
	/**
	 * 21 : 传入12306账号未进行手机核验
	 */
	public static final String MOBILEPHONE_NO_VERIFY= "21";
	
	/**
	 * 22 : 传入12306账号 用户达上限
	 */
	public static final String USERNUMBERS_OVER_TOPLIMIT= "22";
	
	/**
	 * 23 : 传入12306账号中存在未完成订单
	 */
	public static final String EXIST_UNFINISHED_ORDER= "23";
	
	/**
	 * 24 : 传入12306账号取消次数过多
	 */
	public static final String CANCEL_NUMBER_TOMUCH= "24";
	
	/**
	 * 25 : 传入12306账号待核验
	 */
	public static final String ACCOUNT_WAIT_VERIFY= "25";
	
	/**
	 * 26 : 传入12306账号不存在
	 */
	public static final String ACCOUNT_NO_EXIST= "26";
	
	/**
	 * 27 : 传入12306账号登录密码错误
	 */
	public static final String PASSWORD_WRONG= "27";
	
	

}
