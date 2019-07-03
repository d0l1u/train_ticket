package com.l9e.transaction.vo;

public class ReturnSuccess {
	private String ret;
	private String verify_code;
	private String pic_id;
	public static String SUCCESS="success";//返回成功标示
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getVerify_code() {
		return verify_code;
	}
	public void setVerify_code(String verifyCode) {
		verify_code = verifyCode;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String picId) {
		pic_id = picId;
	}
	
}
