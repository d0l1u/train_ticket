package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class PsOrderVo {
	private static final String STATUS_00 = "00"; 
	private static final String STATUS_11 = "11"; 
	private static final String STATUS_12 = "12"; 
	private static final String STATUS_21 = "21"; 
	private static final String STATUS_22 = "22"; 
	private static final String STATUS_23 = "23"; 
	
	
public static Map<String, String> PS_STATUS = new LinkedHashMap<String, String>();
	
	static{
		PS_STATUS.put(STATUS_00, "等待出票");
		PS_STATUS.put(STATUS_11, "出票成功");
		PS_STATUS.put(STATUS_12, "出票失败");
		PS_STATUS.put(STATUS_21, "正在配送");
		PS_STATUS.put(STATUS_22, "配送成功");
		PS_STATUS.put(STATUS_23, "配送失败");
	}
	public static Map<String, String> getPsOrderStatus(){
		//出票状态 00、开始出票 11、出票成功 12、出票失败 21、正在配送 22、配送成功 23、配送失败
		return PS_STATUS;
	}
}
