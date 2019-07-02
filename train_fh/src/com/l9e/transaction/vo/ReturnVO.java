package com.l9e.transaction.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ReturnVO {
	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<Order> errorInfo;
	
	
	
	
	public Integer getErrorCode() {
		return errorCode;
	}
	
	
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	
	
	public List<Order> getErrorInfo() {
		return errorInfo;
	}
	
	
	public void setErrorInfo(List<Order> errorInfo) {
		this.errorInfo = errorInfo;
	}
}
