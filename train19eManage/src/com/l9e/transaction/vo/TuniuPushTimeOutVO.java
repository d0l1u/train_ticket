package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class TuniuPushTimeOutVO {
	
	//00、未处理 11、已处理 22、人工处理 33、超时告警
	public static final String deal_status_00 = "00";
	public static final String deal_status_11 = "11";
	public static final String deal_status_22 = "22";
	public static final String deal_status_33 = "33";
	
	//1、预定占位超时告警2、	出票超时告警3、	改签占位超时告警 4、	改签确认超时告警 5、预定占位失败 6、出票失败 7、改签占位失败 8、改签确认失败  
	public static final String status_1 = "1";
	public static final String status_2 = "2";
	public static final String status_3 = "3";
	public static final String status_4 = "4";
	public static final String status_5 = "5";
	public static final String status_6 = "6";
	public static final String status_7 = "7";
	public static final String status_8 = "8";
	
	private static Map<String, String> status = new LinkedHashMap<String, String>();
	private static Map<String, String> deal_status = new LinkedHashMap<String, String>();
	
	public static Map<String, String> getPushTimeOutStatus(){
		if (status.isEmpty()) {
			status.put(status_1,"预定占位超时告警");
			status.put(status_2,"出票超时告警");
			status.put(status_3,"改签占位超时告警");
			status.put(status_4,"改签确认超时告警");
			status.put(status_5,"预定占位失败");
			status.put(status_6,"出票失败");
			status.put(status_7,"改签占位失败");
			status.put(status_8,"改签确认失败");
		}
		return  status;
	}
	
	public static Map<String, String> getPushTimeOutDealStatus(){
		if(deal_status.isEmpty()){
			deal_status.put(deal_status_00,"未处理");
			deal_status.put(deal_status_11,"已处理");
			deal_status.put(deal_status_22,"人工处理");
			deal_status.put(deal_status_33,"超时告警");
		}
		return  deal_status;
	}
	
	
	
	
	

}
