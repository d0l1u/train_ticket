package com.l9e.train.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class JDReturnVO {
	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<JDOrderVo> errorInfo;


	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<JDOrderVo> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<JDOrderVo> errorInfo) {
		this.errorInfo = errorInfo;
	}


	
	

	
	
	
	
	
	

}
