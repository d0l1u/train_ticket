package com.l9e.train.po;

public class AccountLog {
	
	public String acc_name;
	public String order_id;
	public String cz_type;
	public String opter;   
	//操作类型 11、改签 22、退票 33、取消  44、预订(暂不统计)
	//11取消 22退票 33改签 44预订(暂不统计)
	public static String CANCEL_TYPE = "11"; //取消
	public static String REFUND_TYPE = "22"; //退票
	public static String ALTER_TYPE = "33"; //改签
	public static String ORDER_TYPE = "44"; //预订
	public String getAcc_name() {
		return acc_name;
	}
	public void setAcc_name(String accName) {
		acc_name = accName;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getCz_type() {
		return cz_type;
	}
	public void setCz_type(String czType) {
		cz_type = czType;
	}
	public String getOpter() {
		return opter;
	}
	public void setOpter(String opter) {
		this.opter = opter;
	}
	
}
