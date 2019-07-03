package com.l9e.util;

public class StrUtil {

	public static String getAccountQueue(String channel) {
		return "com.train.account.channel." + channel;
	}
	
	public static String getAccountLock(String channel) {
		return "com.train.account.lock." + channel;
	}
}
