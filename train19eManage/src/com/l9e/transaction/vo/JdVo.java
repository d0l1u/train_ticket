package com.l9e.transaction.vo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 京东
 * @author wangsf
 *
 */
public class JdVo {
	
	//获取京东预付卡状态
	//预付卡状态:  00：空闲  11：使用中  22：停用

	private static String CARD_STATUS_FREE = "00";//空闲
	private static String CARD_STATUS_WORKING = "11";//使用中
	private static String CARD_STATUS_STOP = "22";//停用
	private static Map<String, String> JDCARDSTATUS = new LinkedHashMap<String, String>();
	
	public static Map<String, String> getJdCardStatus(){
		if(JDCARDSTATUS.isEmpty()){
			JDCARDSTATUS.put(CARD_STATUS_FREE, "空闲");
			JDCARDSTATUS.put(CARD_STATUS_WORKING, "使用中");
			JDCARDSTATUS.put(CARD_STATUS_STOP, "停用");
		}
		return JDCARDSTATUS;
	}
	
}
