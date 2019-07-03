package com.l9e.transaction.vo;

public class ReturnOptlog {
	public static String ROBOTEXCP = "66";
	public static String NOTICKET = "08";
	public static String TIMEOUT = "AA";
	public static String NORESULT = "BB";
	public static String SOS = "SS";
	
	private String return_id;
	private String return_name;
	private String return_type;	//处理方式 00:重发 11:失败 22：人工
	private String return_join;	//是否参与
	private String fail_reason;	//失败错误原因
	
	
	public String getFail_reason() {
		return fail_reason;
	}
	public void setFail_reason(String failReason) {
		fail_reason = failReason;
	}
	public String getReturn_id() {
		return return_id;
	}
	public void setReturn_id(String returnId) {
		return_id = returnId;
	}
	public String getReturn_name() {
		return return_name;
	}
	public void setReturn_name(String returnName) {
		return_name = returnName;
	}
	public String getReturn_type() {
		return return_type;
	}
	public void setReturn_type(String returnType) {
		return_type = returnType;
	}
	public String getReturn_join() {
		return return_join;
	}
	public void setReturn_join(String returnJoin) {
		return_join = returnJoin;
	}
}
