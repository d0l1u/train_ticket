package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class RefundReasonVo {
	//拒绝退款原因
	public static final String TICKETOUT = "1";//已取票
	public static final String TIMEOUT = "2";//已过时间
	public static final String CANOTREFUND1 = "3"; //该车次无法通过12306在线退票，请前往就近火车站售票窗口办理。
	public static final String CANOTREFUND2 = "4"; //抱歉，退票失败，请前往就近火车站售票窗口办理。
	
	private static Map<String, String> REFUSEREASON = new LinkedHashMap<String, String>();
	public static Map<String, String> getRefuseReason(){
		if(REFUSEREASON.isEmpty()) {
			REFUSEREASON.put(CANOTREFUND1, "该车次无法通过12306在线退票，请前往就近火车站售票窗口办理。");
			REFUSEREASON.put(CANOTREFUND2, "抱歉，退票失败，请前往就近火车站售票窗口办理。");
			REFUSEREASON.put(TICKETOUT, "已取票");
			REFUSEREASON.put(TIMEOUT, "已过时间");
		}
		return REFUSEREASON;
	}
}
