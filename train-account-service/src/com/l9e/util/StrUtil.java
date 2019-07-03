package com.l9e.util;

public class StrUtil {

	public static String getAccountQueue(String channel) {
		return "com.train.account.channel." + channel;
	}
	
	public static String getAccountLock(String channel) {
		return "com.train.account.lock." + channel;
	}
	
	public static String getAccountQueue(String channel,Integer passengerSize) {
		return "com.train.account.channel." + channel+"."+passengerSize;
	}
	
	public static String getAccountLock(String channel,Integer passengerSize) {
		return "com.train.account.lock." + channel+"."+passengerSize;
	}
}
