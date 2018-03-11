package com.l9e.transaction.vo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 携程
 * @author zhangjc
 *
 */
public class CtripVo {
	
	//获取携程出票状态
	/*
	 * 处理状态 00:初始状态 11:请求出票成功 22:请求出票失败 33:出票流程结束 44:切入12306出票 55:开始查询结果
	 */
	private static String STATUS_00 ="00";
	private static String STATUS_11 ="11";
	private static String STATUS_22 ="22";
	private static String STATUS_33 ="33";
	private static String STATUS_44 ="44";
	private static String STATUS_55 ="55";
	
	private static Map<String, String> CTRIPSGOTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getCtripGoStatus(){
		if(CTRIPSGOTATUS.isEmpty()){
			CTRIPSGOTATUS.put(STATUS_00, "初始状态");
			CTRIPSGOTATUS.put(STATUS_11, "请求成功");
			CTRIPSGOTATUS.put(STATUS_22, "请求失败");
			CTRIPSGOTATUS.put(STATUS_33, "流程结束");
			CTRIPSGOTATUS.put(STATUS_44, "正常出票");
			CTRIPSGOTATUS.put(STATUS_55, "查询结果");
		}
		return CTRIPSGOTATUS;
	}

	//获取携程账号状态
	//00-启用 11-停用 22-因余额不足停用 33-注册成功绑定礼品卡成功，暂时停用 44-注册成功绑定礼品卡失败，暂时停用
	//55-注册成功绑定礼品卡成功，查询余额失败，暂时停用 12-注册成功
	private static String START ="00";
	private static String STOP = "11";
	private static String NOMONEY_TEMPSTOP = "22";
	private static String TEMPSTOP = "33";
	private static String CARDERROR_TEMPSTOP = "44";
	private static String GETMONEYERROR_TEMPSTOP = "55";
	private static String REGISTER_SUCCESS = "12";
	private static Map<String, String> CTRIPSTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getCtripStatus(){
		if(CTRIPSTATUS.isEmpty()){
			CTRIPSTATUS.put(START, "启用");
			CTRIPSTATUS.put(STOP, "停用");
			CTRIPSTATUS.put(NOMONEY_TEMPSTOP, "余额不足，临时停用");
			CTRIPSTATUS.put(TEMPSTOP, "绑定成功，临时停用");
			CTRIPSTATUS.put(CARDERROR_TEMPSTOP, "绑定失败，临时停用");
			CTRIPSTATUS.put(GETMONEYERROR_TEMPSTOP, "查询余额失败，临时停用");
			CTRIPSTATUS.put(REGISTER_SUCCESS, "注册成功");
		}
		return CTRIPSTATUS;
	}
	//获取携程账号级别 1:充值100  2:充值500  3:充值1000  4:充值2000  5:充值5000
	private static Integer DEGREE_1 = 1;
	private static Integer DEGREE_2 = 2;
	private static Integer DEGREE_3 = 3;
	private static Integer DEGREE_4 = 4;
	private static Integer DEGREE_5 = 5;
	private static Map<Integer, String> CTRIPDEGREE = new LinkedHashMap<Integer, String>();
	public static Map<Integer, String> getCtripDegree(){
		if(CTRIPDEGREE.isEmpty()){
			CTRIPDEGREE.put(DEGREE_1, "级别1");
			CTRIPDEGREE.put(DEGREE_2, "级别2");
			CTRIPDEGREE.put(DEGREE_3, "级别3");
			CTRIPDEGREE.put(DEGREE_4, "级别4");
			CTRIPDEGREE.put(DEGREE_5, "级别5");
		}
		return CTRIPDEGREE;
	}
	//获取出票渠道
	private static String MANUAL = "";
	private static String MANUAL_00 = "00";
	private static String MANUAL_11 = "11";
	private static String MANUAL_22 = "22";
	private static Map<String, String> MANUALORDER = new LinkedHashMap<String, String>();
	public static Map<String, String> getManualOrder(){
		if(MANUALORDER.isEmpty()){
			MANUALORDER.put(MANUAL, "12306");
			MANUALORDER.put(MANUAL_00, "12306");
			MANUALORDER.put(MANUAL_11, "人工");
			MANUALORDER.put(MANUAL_22, "携程");
		}
		return MANUALORDER;
	}
	
}
