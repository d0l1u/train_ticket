package com.l9e.train.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReturnAlterInfo {

	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<AlterResultEntity> errorInfo;
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<AlterResultEntity> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<AlterResultEntity> errorInfo) {
		this.errorInfo = errorInfo;
	}
}
