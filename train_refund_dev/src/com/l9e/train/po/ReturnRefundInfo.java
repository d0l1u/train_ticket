package com.l9e.train.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReturnRefundInfo {

	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<RefundResultEntity> errorInfo;
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<RefundResultEntity> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<RefundResultEntity> errorInfo) {
		this.errorInfo = errorInfo;
	}
}
