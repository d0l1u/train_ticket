package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * qunar接口返回信息
 * @author zhangjun
 *
 */
public class QunarResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean ret;
	
	private String errMsg;
	
	private String errCode;

	public boolean isRet() {
		return ret;
	}

	public void setRet(boolean ret) {
		this.ret = ret;
	}

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
	
}
