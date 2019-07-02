package com.l9e.util;

public class StrUtil {

	public static String getWorkerQueue(Integer type) {
		return "com.train.worker.type." + type;
	}
	
}
