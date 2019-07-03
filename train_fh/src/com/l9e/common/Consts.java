package com.l9e.common;

import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.IpInfoService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.WorkerService;


public class Consts {
	public static String ORDERURL="";
	public static String GETACCURL="";
	
	public static String READYPAYURL="";//支付预登入
	public static final String ACCLOCK="ACCLOCK";
	public static final String ORDER_NUM="com.19e.train.ORDER_NUM";
	
	public static final String QUEUE_NAME = "com.19e.mq.order";
	
	public static RedisDao redisDao;
	public static AccountService accountService;
	public static OrderService orderService;
	public static WorkerService workerService;
	public static IpInfoService ipInfoService;
	
	public static String GET_WORKER_BY_ID;
	public static String GET_WORKER_BY_TYPE;
	public static String RELEASE_WORKER;
	public static String STOP_WORKER;
	/**
	 * 随机获取一个java脚本的机器
	 */
	public static String GET_JAVA_WORKER;
	
	public static String GET_CHANNEL_ACCOUNT;
	public static String GET_ORDER_ACCOUNT;
	public static String STOP_ACCOUNT;
	public static String UPDATE_ACCOUNT;
	public static String FILTER_ACCOUNT;
	public static String RELEASE_ACCOUNT;
	
	public static String GET_ORDER_PASSENGER;
	public static String UPDATE_ORDER;
	
	
	public static String HANDLE_BIND_ACCOUNT;//自带12306账号错误信息处理
	
	public static String GET_IP_BY_ID;
	public static String GET_IP_BY_TYPE;
	
	public static final int max_jq_num = 60;
	
	public static final String LOCK="true";
}
