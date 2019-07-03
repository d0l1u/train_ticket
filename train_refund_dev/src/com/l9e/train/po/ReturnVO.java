package com.l9e.train.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReturnVO {
	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<OrderCP> errorInfo;
	
	
	
	
	public Integer getErrorCode() {
		return errorCode;
	}
	
	
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	
	
	public List<OrderCP> getErrorInfo() {
		return errorInfo;
	}
	
	
	public void setErrorInfo(List<OrderCP> errorInfo) {
		this.errorInfo = errorInfo;
	}
}
