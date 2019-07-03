package com.l9e.util;

import java.util.HashMap;
import java.util.Map;

public class TrainPropUtil {
	
	private static Map<String, String> to19eSeatMap = new HashMap<String, String>();
	
	private static Map<String, String> toQunarSeatMap = new HashMap<String, String>();
	
	static {
		to19eSeatMap.put("qunar_0", "9");//站票
		to19eSeatMap.put("qunar_1", "8");//硬座
		to19eSeatMap.put("qunar_2", "7");//软座
		to19eSeatMap.put("qunar_3", "2");//一等软座
		to19eSeatMap.put("qunar_4", "3");//二等软座
		to19eSeatMap.put("qunar_5", "61");//硬卧上
		to19eSeatMap.put("qunar_6", "62");//硬卧中
		to19eSeatMap.put("qunar_7", "63");//硬卧下
		to19eSeatMap.put("qunar_8", "51");//软卧上
		to19eSeatMap.put("qunar_9", "52");//软卧下
		to19eSeatMap.put("qunar_10", "41");//高级软卧上
		to19eSeatMap.put("qunar_11", "42");//高级软卧下
		
		toQunarSeatMap.put("19e_2", "3");//一等座
		toQunarSeatMap.put("19e_3", "4");//二等座
		toQunarSeatMap.put("19e_41", "10");//高级软卧上
		toQunarSeatMap.put("19e_42", "11");//高级软卧下
		toQunarSeatMap.put("19e_51", "8");//软卧上
		toQunarSeatMap.put("19e_52", "9");//软卧下
		toQunarSeatMap.put("19e_61", "5");//硬卧上
		toQunarSeatMap.put("19e_62", "6");//硬卧中
		toQunarSeatMap.put("19e_63", "7");//硬卧下
		toQunarSeatMap.put("19e_7", "2");//软座
		toQunarSeatMap.put("19e_8", "1");//硬座
		toQunarSeatMap.put("19e_9", "0");//无座
		
	}
	
	public static String get19eSeatType(String seat){
		return to19eSeatMap.get("qunar_"+seat);
	}
	
	public static String getQunarSeatType(String seat){
		return toQunarSeatMap.get("19e_"+seat);
	}
	
	public static String get19eIdsType(String certType, String certNo){
		if("1".equals(certType) && certNo.length()==15){//身份证
			return "1";
		}else if("1".equals(certType) && certNo.length()==18){
			return "2";
		}else if("C".equalsIgnoreCase(certType)){//港澳通行证
			return "3";
		}else if("G".equalsIgnoreCase(certType)){//台湾通行证
			return "4";
		}else if("B".equalsIgnoreCase(certType)){//护照
			return "5";
		}else{
			return null;
		}
	}

}
