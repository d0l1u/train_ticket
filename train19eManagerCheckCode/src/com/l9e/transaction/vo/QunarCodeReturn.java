package com.l9e.transaction.vo;

import java.util.Map;

public class QunarCodeReturn {
	private boolean ret;//请求是否成功，true成功，false失败
	private String errcode;//ret为false时存在
	private String errmsg;//Ret为false时存在
	public Map<String, String> data;//返回成功标示
	
	public boolean isRet() {
		return ret;
	}
	public void setRet(boolean ret) {
		this.ret = ret;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
}
