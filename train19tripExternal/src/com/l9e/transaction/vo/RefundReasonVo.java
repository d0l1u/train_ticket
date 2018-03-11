package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class RefundReasonVo {
	//拒绝退款原因
	public static final String TICKETOUT = "1";//已取票
	public static final String TIMEOUT = "2";//已过时间
	public static final String PHONECALL = "3"; //电话取消
	public static final String FENGHAO = "4"; //账号被封
	public static final String MONEYFAIL = "6"; //退款金额有损失	高诚信用户极速退款订单退款手续费有误	 (只针对去哪儿)
	public static final String CANNOT = "7"; //不可退票（只针对同程）
	
	private static Map<String, String> REFUSEREASON = new LinkedHashMap<String, String>();
	public static Map<String, String> getRefuseReason(){
		if(REFUSEREASON.isEmpty()) {
			REFUSEREASON.put(TICKETOUT, "已取票");
			REFUSEREASON.put(TIMEOUT, "已过时间");
			REFUSEREASON.put(PHONECALL, "来电取消");
			REFUSEREASON.put(FENGHAO, "账号被封");
			//REFUSEREASON.put(OTHER, "其他");
			REFUSEREASON.put(MONEYFAIL, "退款金额有损失");
			REFUSEREASON.put(CANNOT, "不可退票");
		}
		return REFUSEREASON;
	}
}
