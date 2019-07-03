package com.l9e.train.util;

public class Consts {

	/**
	 * 获取账号服务接口
	 */
	public static String GET_ACCOUNT_URL = "";
	
	/**
	 * 获取机器人，按类型
	 */
	public static String GET_WORKER_BY_TYPE = "";
	/**
	 * 获取机器人，按主键id
	 */
	public static String GET_WORKER_BY_ID = "";
	/**
	 * 释放机器人
	 */
	public static String RELEASE_WORKER = "";
	/**
	 * 停用机器人
	 */
	public static String STOP_WORKER = "";
	
	/**
	 * 随机获取一个java脚本的机器
	 */
	public static String GET_JAVA_WORKER; 
	
	/**
	 * 获取12306账号：按渠道
	 */
	public static String GET_CHANNEL_ACCOUNT;
	/**
	 * 获取订单关联12306账号
	 */
	public static String GET_ORDER_ACCOUNT;
	/**
	 * 停用账号
	 */
	public static String STOP_ACCOUNT;
	/**
	 * 修改账号
	 */
	public static String UPDATE_ACCOUNT;
	/**
	 * 证件号码匹配账号
	 */
	public static String FILTER_ACCOUNT;
	/**
	 * 释放账号
	 */
	public static String RELEASE_ACCOUNT;
	
	/**
	 * 获取订单乘客-车票信息
	 */
	public static String GET_ORDER_PASSENGER;
	/**
	 * 修改订单信息
	 */
	public static String UPDATE_ORDER;
	
	//自带12306账号错误信息处理
	public static String HANDLE_BIND_ACCOUNT;//自带12306账号错误信息处理
	
	//IP服务
	public static String GET_IP_BY_ID;
	public static String GET_IP_BY_TYPE;
}
