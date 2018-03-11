package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 保险Vo
 * @author liht
 *
 */
public class InsuranceVo {
	/***********************保险状态************************/
	public static final Integer NOT_SENT = 0;//未发送
	public static final Integer SENDING = 1;//正在发送
	public static final Integer SEND_COMPLETED = 2; //发送完成
	public static final Integer IS_REVOKED = 3; //正在撤销
	public static final Integer CANCELLATION_COMPLETED = 4; //撤销完成
	public static final Integer SEND_FAILED = 5;//发送失败
	public static final Integer NEED_CANCEL = 6;//需要取消
	public static final Integer FAILURE_CANCEL = 7; //取消失败
	
	public static Map<Integer,String> insurance_Map = new LinkedHashMap<Integer,String>();
	
	public static Map<Integer,String> getInsurance_Status(){
		if(insurance_Map.isEmpty()){
			insurance_Map.put(NOT_SENT, "未发送");
			insurance_Map.put(SENDING, "正在发送");
			insurance_Map.put(SEND_COMPLETED, "发送完成");
			insurance_Map.put(IS_REVOKED, "正在撤销");
			insurance_Map.put(CANCELLATION_COMPLETED, "撤销完成");
			insurance_Map.put(SEND_FAILED, "发送失败");
			insurance_Map.put(NEED_CANCEL, "需要取消");
			insurance_Map.put(FAILURE_CANCEL, "取消失败");
		}
		return insurance_Map;
	}
	
	public static String CHANNEL_KUAIBAO = "1";
	public static String CHANNEL_HEZHONG = "2";
	public static String CHANNEL_PINGAN = "3";
	
	
	private static Map<String,String>BX_CHANNEL = new LinkedHashMap<String,String>();
	
	public static Map<String,String>getBx_Channel(){
		if(BX_CHANNEL.isEmpty()){
			BX_CHANNEL.put(CHANNEL_KUAIBAO, "快保");
			BX_CHANNEL.put(CHANNEL_HEZHONG, "合众");
			BX_CHANNEL.put(CHANNEL_PINGAN, "平安");
			
		}
		return BX_CHANNEL;
	}

}
