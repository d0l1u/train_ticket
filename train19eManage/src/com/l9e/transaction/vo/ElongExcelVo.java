package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class ElongExcelVo {
	//结算类型 1：出票成功；2：出票不成功；3：线上退票；4：线下退票；5：改签退票；6：赔偿退款；7：异常扣款；
	//8：异常加款；9：手续费；10：改签扣款；11：线下改签；12：财务充值；13：期末余额；14：期初余额；
	public static final Integer type_1 = 1;
	public static final Integer type_2 = 2;
	public static final Integer type_3 = 3;
	public static final Integer type_4 = 4;
	public static final Integer type_5 = 5;
	public static final Integer type_6 = 6;
	public static final Integer type_7 = 7;
	public static final Integer type_8 = 8;
	public static final Integer type_9 = 9;
	public static final Integer type_10 = 10;
	public static final Integer type_11 = 11;
	public static final Integer type_12 = 12;
	public static final Integer type_13 = 13;
	public static final Integer type_14 = 14;
	//结算类型 1：出票成功；2：出票不成功；3：线上退票；4：线下退票；5：改签退票；6：赔偿退款；7：异常扣款；
	//8：异常加款；9：手续费；10：改签扣款；11：线下改签；12：财务充值；13：期末余额；14：期初余额；
	private static Map<Integer, String> Type = new LinkedHashMap<Integer, String>();
	public static Map<Integer, String> getSettlementType() {
		if(Type.isEmpty()) {
			Type.put(type_1, "出票成功");
			Type.put(type_2, "出票失败");
			Type.put(type_3, "线上退票");
			Type.put(type_4, "线下退票");
			Type.put(type_5, "改签退票");
			Type.put(type_6, "赔偿退款");
			Type.put(type_7, "异常扣款");
			Type.put(type_8, "异常加款");
			Type.put(type_9, "手续费");
			Type.put(type_10, "改签扣款");
			Type.put(type_11, "线下改签");
			Type.put(type_12, "财务充值");
			Type.put(type_13, "期末余额");
			Type.put(type_14, "期初余额");
		}
		return Type;
	}
	
	
	public static final String notify_status_00 = "00";
	public static final String notify_status_11 = "11";
	public static final String notify_status_22 = "22";
	public static final String notify_status_33 = "33";
//	通知状态: 00、未通知 11、正在通知 22、通知完成 33、通知失败
	private static Map<String, String> NOTIFYSTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getNotifyStatus() {
		if(NOTIFYSTATUS.isEmpty()) {
			NOTIFYSTATUS.put(notify_status_00, "未通知");
			NOTIFYSTATUS.put(notify_status_11, "正在通知");
			NOTIFYSTATUS.put(notify_status_22, "通知完成");
			NOTIFYSTATUS.put(notify_status_33, "通知失败");
		}
		return NOTIFYSTATUS;
	}
}
