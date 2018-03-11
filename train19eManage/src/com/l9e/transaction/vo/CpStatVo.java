package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class CpStatVo {
	public static String CHANNEL_19E = "19e";
	public static String CHANNEL_QUNAR = "qunar";
	public static String CHANNEL_19PAY = "19pay";
	public static String CHANNEL_CMPAY = "cmpay";
	public static String CHANNEL_APP = "app";
	public static String CHANNEL_CBCPAY = "ccb";
	public static String CHANNEL_WEIXIN = "weixin";
	
	private static Map<String,String>CHANNELS = new LinkedHashMap<String,String>();
	
	public static Map<String,String>getCHANNELS(){
		if(CHANNELS.isEmpty()){
			CHANNELS.put(CHANNEL_19E, "19E");
			CHANNELS.put(CHANNEL_QUNAR, "去哪");
			CHANNELS.put(CHANNEL_19PAY, "19pay");
			CHANNELS.put(CHANNEL_CMPAY, "cmpay");
			CHANNELS.put(CHANNEL_APP, "app");
			CHANNELS.put(CHANNEL_CBCPAY, "建行");
			CHANNELS.put(CHANNEL_WEIXIN, "微信");
		}
		return CHANNELS;
	}

}
