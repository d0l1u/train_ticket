package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class RefundReasonVo {
	//拒绝退款原因
	/*
		2001	车票过期 或者12306限制的热门车次不能网上退票
		2002	抱歉，退票失败，请前往就近火车站售票窗口办理。
	 */
	
	public static final String REFUSEREASON_2001 = "2001";//车票过期 或者12306限制的热门车次不能网上退票
	public static final String REFUSEREASON_2002 = "2002";//抱歉，退票失败，请前往就近火车站售票窗口办理。
	public static final String REFUSEREASON_2005 = "2005";//已取票
	
	private static Map<String, String> REFUSEREASON = new LinkedHashMap<String, String>();
	public static Map<String, String> getRefuseReason(){
		if(REFUSEREASON.isEmpty()) {
			REFUSEREASON.put(REFUSEREASON_2001, "车票过期 或者12306限制的热门车次不能网上退票。");
			REFUSEREASON.put(REFUSEREASON_2002, "抱歉，退票失败，请前往就近火车站售票窗口办理。");
			REFUSEREASON.put(REFUSEREASON_2005, "已取票");
		}
		return REFUSEREASON;
	}
}
