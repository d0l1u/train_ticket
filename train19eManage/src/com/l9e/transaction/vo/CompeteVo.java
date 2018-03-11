package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompeteVo {
	
	public static String CHANNEL_1 = "qunar";//去哪儿
	private static Map<String, String> COMPETECHANNEL = new LinkedHashMap<String, String>();
	public static Map<String, String> getCompeteChannel() {
		if(COMPETECHANNEL.isEmpty()) {
			COMPETECHANNEL.put(CHANNEL_1, "去哪");
		}
		return COMPETECHANNEL;
	}
	
	public static String GOODS_1 = "qunar1";//19旅行
	public static String GOODS_2 = "qunar2";//九九商旅
	private static Map<String, String> COMPETEGOODS = new LinkedHashMap<String, String>();
	public static Map<String, String> getCompeteGoods() {
		if(COMPETEGOODS.isEmpty()) {
			COMPETEGOODS.put(GOODS_1, "19旅行");
			COMPETEGOODS.put(GOODS_2, "九九商旅");
		}
		return COMPETEGOODS;
	}
}
