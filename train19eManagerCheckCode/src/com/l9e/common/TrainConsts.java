package com.l9e.common;

import java.util.Random;

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
	//00、预下单 11、支付成功 22、正在出票 33、出票失败 44、出票成功 55、准备退款 66、正在退款 77、退票完成 88、订单完成
	public static final String PRE_ORDER = "00";//预下单
	
	public static final String PAY_SUCCESS = "11";//支付成功
	
	public static final String OUTING_TICKET = "22";//正在出票
	
	public static final String OUT_FAIL = "33";//出票失败
	
	public static final String OUT_SUCCESS = "44";//出票成功
	
	public static final String PRE_REFUND = "55";//准备退款
	
	public static final String REFUNDING = "66";//正在退款
	
	public static final String REFUND_FINISH = "77";//退票完成
	
	public static final String ORDER_FINISH = "88";//订单完成
	
	
	/*******************验证码验证渠道***********************/
	//验证码 验证渠道 11、人工验证 22、联众验证 99、人工+联众验证 33、打码兔打码 44、人工+打码兔打码
	public static final String CODE_RG = "11";//人工打码
	public static final String CODE_LZ= "22";//联众打码
	public static final String CODE_LZANDRG = "99";//人工+联众打码
	public static final String CODE_DAMATU = "33";//33、打码兔打码
	public static final String CODE_DAMATUANDRG = "44";//44、人工+打码兔打码
	
	/********************联众 系统参数 ***********************/
	public static final String OPTREN_NAME = "lzdm";//用户名参数
	public static final String UPDATE_SUCCESS= "11";//上传图片成功
	public static final String UPDATE_FAILL = "22";//上传失败 、未上传
	public static final String BACK_SYS="00";//默认
	public static final String BACK_AGIN ="11";//再次反馈
	public static final String BACK_FAILL="22";//反馈失败
	public static final String BACK_SUCCESS="33";//反馈成功
	
	/*******************联众打码 用户参数配置*************************/
	public static final String USERNAME="hcp_19e";//	联众打码 用户名
	public static final String PASSWORD="kuyou19ehuochepiao";//联众打码 密码 
	
	/*******************打码系统 参数*************************/
	public static final String USER_PIC_OUT="0";//0 闲置图片
	public static final String USER_PIC_ON="1";// 1正在处理中图片  
	
	public static final String DAMATU_NAME = "damatu";//用户名参数 damatu
	
	public static final Random random = new Random();//随机数
	public static final String ROBOT_CODE="com.robotmanager.robotcode01";//打码队列
	public static final String ROBOT_CODE02="com.robotmanager.robotcode02";//打码团队02的队列
	public static final String ROBOT_CODE03="com.robotmanager.robotcode03";//打码团队03的队列
	public static final String ROBOT_CODE04="com.robotmanager.robotcode04";//打码团队04的队列
	
	public static final String ROBOT_CODENO="com.robotmanager.robotcodeno";//打码团队no的队列--权重设置关闭后放置的队列
	
	
}
