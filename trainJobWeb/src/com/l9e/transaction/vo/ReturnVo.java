package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReturnVo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<ReturnData> errorInfo;
	
	public Integer getErrorCode() {
		return errorCode;
	}
	
	public List<ReturnData> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<ReturnData> errorInfo) {
		this.errorInfo = errorInfo;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
}
