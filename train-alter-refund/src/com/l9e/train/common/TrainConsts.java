package com.l9e.train.common;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 常量
 * @author zhangjun
 *
 */
public class TrainConsts {
	public static String RETURN_G1 = "G1"; //已出票
	public static String RETURN_G2 = "G2"; //已改签
	public static String RETURN_G5 = "G5"; //改签待支付
	public static String RETURN_G6 = "G6"; //行程冲突
	public static String RETURN_G7 = "G7"; //改签票
	public static String REFUNN_G8 = "G8"; //旅游旺季
	
	public static String ORDER_ERROR = "1001"; 
	public static String ORDER_SUCCESS = "0000"; 
	
	public static final String ROBOT_REFUND_STATUS00="00";	//等待机器改签
	public static final String ROBOT_REFUND_STATUS02="02";	//开始机器改签
	public static final String ROBOT_REFUND_STATUS03="03";	//人工改签
	public static final String ROBOT_REFUND_STATUS04="04";	//等待机器退票
	public static final String ROBOT_REFUND_STATUS05="05";	//重新机器退票
	public static final String ROBOT_REFUND_STATUS06="06";	//开始机器退票
	public static final String ROBOT_REFUND_STATUS07="07";	//人工退票
	public static final String ROBOT_REFUND_STATUS22="22";	//拒绝退票
	public static final String ROBOT_REFUND_STATUS33="33";	//人工审核机器退款结果
	
	
	public static final String SEAT_TYPE0="9";	//商务座
	public static final String SEAT_TYPE1="P";	//特等座
	public static final String SEAT_TYPE2="M";	//一等座
	public static final String SEAT_TYPE3="O";	//二等座
	public static final String SEAT_TYPE4="6";	//高级软座
	public static final String SEAT_TYPE5="4";	//软卧
	public static final String SEAT_TYPE6="3";	//硬卧
	public static final String SEAT_TYPE7="2";	//软座
	public static final String SEAT_TYPE8="1";	//硬座
	public static final String SEAT_TYPE10="";	//无座 动车无座为二等座
	public static final String SEAT_TYPE11="5";	//包厢硬卧
	
	public static final String SEATTYPE0="0";	//商务座
	public static final String SEATTYPE1="1";	//特等座
	public static final String SEATTYPE2="2";	//一等座
	public static final String SEATTYPE3="3";	//二等座
	public static final String SEATTYPE4="4";	//高级软座
	public static final String SEATTYPE41="41";	//高级软座上
	public static final String SEATTYPE42="42";	//高级软座下
	public static final String SEATTYPE5="5";	//软卧
	public static final String SEATTYPE51="51";	//软卧上
	public static final String SEATTYPE52="52";	//软卧下
	public static final String SEATTYPE6="6";	//硬卧
	public static final String SEATTYPE61="61";	//硬卧上
	public static final String SEATTYPE62="62";	//硬卧中
	public static final String SEATTYPE63="63";	//硬卧下
	public static final String SEATTYPE7="7";	//软座
	public static final String SEATTYPE8="8";	//硬座
	public static final String SEATTYPE9="9";	//无座 动车无座为二等座
	public static final String SEATTYPE10="10";	//其他
	
	public static final Integer WORKER_TYPE_ALTER = 7;
	public static final Integer WORKER_TYPE_REFUND = 8;

	private static Map<String, String> qunar_seattype = new LinkedHashMap<String, String>();//坐席
	
	public static Map<String, String> getQunarSeatType() {
		return qunar_seattype;
	}

	private static Map<String, String> seattype = new LinkedHashMap<String, String>();//坐席
	
	public static Map<String, String> getSeatType() {
		return seattype;
	}
	  
	static{
		qunar_seattype.put(SEAT_TYPE0, "0");
		qunar_seattype.put(SEAT_TYPE1, "1");
		qunar_seattype.put(SEAT_TYPE2, "2");
		qunar_seattype.put(SEAT_TYPE3, "3");
		qunar_seattype.put(SEAT_TYPE4, "4");
		qunar_seattype.put(SEAT_TYPE5, "5");
		qunar_seattype.put(SEAT_TYPE6, "6");
		qunar_seattype.put(SEAT_TYPE7, "7");
		qunar_seattype.put(SEAT_TYPE8, "8");
		qunar_seattype.put(SEAT_TYPE10, "");
		qunar_seattype.put(SEAT_TYPE11, "5");
		
		
		seattype.put(SEATTYPE0, "商务座");
		seattype.put(SEATTYPE1, "特等座");
		seattype.put(SEATTYPE2, "一等座");
		seattype.put(SEATTYPE3, "二等座");
		seattype.put(SEATTYPE4, "高级软座");
		seattype.put(SEATTYPE41, "高级软座上");
		seattype.put(SEATTYPE42, "高级软座下");
		seattype.put(SEATTYPE5, "软卧");
		seattype.put(SEATTYPE51, "软卧上");
		seattype.put(SEATTYPE52, "软卧下");
		seattype.put(SEATTYPE6, "硬卧");
		seattype.put(SEATTYPE61, "硬卧上");
		seattype.put(SEATTYPE62, "硬卧中");
		seattype.put(SEATTYPE63, "硬卧下");
		seattype.put(SEATTYPE7, "软座");
		seattype.put(SEATTYPE8, "硬座");
		seattype.put(SEATTYPE9, "无座");
		seattype.put(SEATTYPE10, "其他");
	}

}
