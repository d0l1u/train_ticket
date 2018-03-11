package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class RefundVo {

	private String refund_memo;
	private String order_id;
	private String refund_money;
	private String refund_limit;
	private String refund_12306_seq;
	
	public static String PRE_REFUND = "55";//准备退款
	public static String REFUNDING = "66";//正在退款
	public static String EOP_REFUNDING ="67";//EOP正在退款
	public static String REFUND_FINISH ="77";//退款完成
	public static String REFUSE_REFUNDING = "88";
	
	private static Map<String,String> refundStatus= new LinkedHashMap<String,String>();
	
	public static Map<String,String> getStatus(){
		if(refundStatus.isEmpty()){
			refundStatus.put(PRE_REFUND, "准备退款");
			refundStatus.put(REFUNDING, "正在退款");
			refundStatus.put(EOP_REFUNDING, "EOP正在退款");
			refundStatus.put(REFUND_FINISH, "退款完成");
			refundStatus.put(REFUSE_REFUNDING, "拒绝退款");
		}
		return refundStatus;
	}
	
	public String getRefund_12306_seq() {
		return refund_12306_seq;
	}
	public void setRefund_12306_seq(String refund_12306_seq) {
		this.refund_12306_seq = refund_12306_seq;
	}
	public String getRefund_limit() {
		return refund_limit;
	}
	public void setRefund_limit(String refund_limit) {
		this.refund_limit = refund_limit;
	}
	public String getRefund_memo() {
		return refund_memo;
	}
	public void setRefund_memo(String refund_memo) {
		this.refund_memo = refund_memo;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getRefund_money() {
		return refund_money;
	}
	public void setRefund_money(String refund_money) {
		this.refund_money = refund_money;
	}
	
}
