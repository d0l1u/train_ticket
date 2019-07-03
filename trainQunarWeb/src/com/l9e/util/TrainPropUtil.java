package com.l9e.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class TrainPropUtil {
	
	private static Map<String, String> to19eSeatMap = new HashMap<String, String>();
	
	private static Map<String, String> toQunarSeatMap = new HashMap<String, String>();
	
	private static Map<String, String> hanziTo19eSeatMap = new HashMap<String, String>();//汉字坐席转换
	
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
		to19eSeatMap.put("qunar_12", "1");//特等座
		to19eSeatMap.put("qunar_13", "0");//商务座
		
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
		toQunarSeatMap.put("19e_1", "12");//特等座
		toQunarSeatMap.put("19e_0", "13");//商务座
		
		//汉字坐席转换成19e坐席
		//无座,硬座,软座,一等座,二等座,硬卧上,硬卧中,硬卧下,软卧上,软卧下,高级软卧上,高级软卧下,
		//特等座,商务座,一等软座,二等软座,高级动卧上,高级动卧下,动卧上,动卧下,一人软包,包厢软座,
		//特等软座,包厢硬卧上,包厢硬卧下,混编硬座,混编软座,混编硬卧,混编软卧,一等双软,二等双软,观光座,一等包座
		
		//19e坐席：座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座|动车无座为二等座  10、其他 11、包厢硬卧15动卧 16高级动卧 
		
		hanziTo19eSeatMap.put("qunar_无座", "9");//站票
		hanziTo19eSeatMap.put("qunar_硬座", "8");//硬座
		hanziTo19eSeatMap.put("qunar_软座", "7");//软座
		hanziTo19eSeatMap.put("qunar_一等座", "2");//一等软座
		hanziTo19eSeatMap.put("qunar_二等座", "3");//二等软座
		hanziTo19eSeatMap.put("qunar_硬卧上", "61");//硬卧上
		hanziTo19eSeatMap.put("qunar_硬卧中", "62");//硬卧中
		hanziTo19eSeatMap.put("qunar_硬卧下", "63");//硬卧下
		hanziTo19eSeatMap.put("qunar_软卧上", "51");//软卧上
		hanziTo19eSeatMap.put("qunar_软卧下", "52");//软卧下
		hanziTo19eSeatMap.put("qunar_高级软卧上", "41");//高级软卧上
		hanziTo19eSeatMap.put("qunar_高级软卧下", "42");//高级软卧下
		hanziTo19eSeatMap.put("qunar_特等座", "1");//特等座
		hanziTo19eSeatMap.put("qunar_商务座", "0");//商务座
		hanziTo19eSeatMap.put("qunar_一等软座", "10");//一等软座
		hanziTo19eSeatMap.put("qunar_二等软座", "10");//二等软座
		hanziTo19eSeatMap.put("qunar_高级动卧上", "16");//高级动卧上
		hanziTo19eSeatMap.put("qunar_高级动卧下", "16");//高级动卧下
		hanziTo19eSeatMap.put("qunar_动卧上", "15");//动卧上
		hanziTo19eSeatMap.put("qunar_动卧下", "15");//动卧下
		hanziTo19eSeatMap.put("qunar_一人软包", "10");//一人软包
		hanziTo19eSeatMap.put("qunar_包厢软座", "10");//包厢软座
		hanziTo19eSeatMap.put("qunar_特等软座", "10");//特等软座
		hanziTo19eSeatMap.put("qunar_包厢硬卧上", "10");//包厢硬卧上
		hanziTo19eSeatMap.put("qunar_包厢硬卧下", "10");//包厢硬卧下
		hanziTo19eSeatMap.put("qunar_混编硬座", "10");//混编硬座
		hanziTo19eSeatMap.put("qunar_混编软座", "10");//混编软座
		hanziTo19eSeatMap.put("qunar_混编硬卧", "10");//混编硬卧
		hanziTo19eSeatMap.put("qunar_混编软卧", "10");//混编软卧
		hanziTo19eSeatMap.put("qunar_一等双软", "10");//一等双软
		hanziTo19eSeatMap.put("qunar_二等双软", "10");//二等双软
		hanziTo19eSeatMap.put("qunar_观光座", "10");//观光座
		hanziTo19eSeatMap.put("qunar_一等包座", "10");//一等包座
		
	}
	
	public static String get19eSeatType(String seat){
		return to19eSeatMap.get("qunar_"+seat);
	}
	
	public static String getQunarSeatType(String seat){
		return toQunarSeatMap.get("19e_"+seat);
	}
	
	public static String getHanzi19eSeatType(String seat){
		return hanziTo19eSeatMap.get("qunar_"+seat);
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
	
	public static String getQunarIdsType(String certType){
		if(StringUtils.isEmpty(certType)){
			return "1";
		}else if("2".equals(certType)){
			return "1";
		}else if("3".equals(certType)){
			return "C";
		}else if("4".equals(certType)){
			return "G";
		}else if("5".equals(certType)){
			return "B";
		}else{
			return "1";
		}
	}
	
	public static String getQunarIdsTypeName(String certType){
		if(StringUtils.isEmpty(certType)){
			return "二代身份证";
		}else if("2".equals(certType)){
			return "二代身份证";
		}else if("3".equals(certType)){
			return "港澳通行证";
		}else if("4".equals(certType)){
			return "台湾通行证";
		}else if("5".equals(certType)){
			return "护照";
		}else{
			return "二代身份证";
		}
	}
	
	
	/**
	 * 将qunar车票类型转换成19e车票类型
	 * 19e:车票类型  			0成人票   1儿童票   3学生票
	 * qunar:车票类型			0儿童票   1成人票   2学生票
	 * */
	public static String get19eTicketType(String tcTicketType){
		if(StringUtils.isEmpty(tcTicketType)){
			return "0";
		}else if("0".equals(tcTicketType)){
			return "1";
		}else if("2".equals(tcTicketType)){
			return "3";
		}else{
			return "0";//成人
		}
	}
	
	//将19e车票类型转换成qunar车票类型
	public static String getQunarTicketType(String ticketType){
		if(StringUtils.isEmpty(ticketType)){
			return "1";//成人票
		}else if("1".equals(ticketType)){
			return "0";
		}else if("3".equals(ticketType)){
			return "2";
		}else{
			return "1";
		}
	}
	
	//qunar--1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通行证，B:护照
	//women--１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
	public static String getIdstype(String certType){
		if(StringUtils.isEmpty(certType) || "1".equals(certType)){
			return "2";
		}else if("2".equals(certType)){
			return "1";
		}else if("C".equals(certType)){
			return "3";
		}else if("G".equals(certType)){
			return "4";
		}else if("B".equals(certType)){
			return "5";
		}else{
			return "2";
		}
	}
	

	
	public static String beautifySeatNo(String seatNo){
		//String seatNo = "02车厢车13D号 上铺";
		seatNo = seatNo.replace(" ", "").replaceAll(" ", "").replace("车厢车", "车");
		seatNo = seatNo.replace("车上", "车").replace("车下", "车");
//		if(seatNo.startsWith("0")){
//			seatNo = seatNo.substring(1);
//		}
//		System.out.println(seatNo);
		return seatNo;
	}
	
}
