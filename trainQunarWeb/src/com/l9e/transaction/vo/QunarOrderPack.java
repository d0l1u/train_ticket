package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

public class QunarOrderPack implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean ret;
	private int total;
	private List<QunarOrder> data;
	private String errMsg;
	private String errCode;
	
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public boolean isRet() {
		return ret;
	}
	public void setRet(boolean ret) {
		this.ret = ret;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<QunarOrder> getData() {
		return data;
	}
	public void setData(List<QunarOrder> data) {
		this.data = data;
	}
}
