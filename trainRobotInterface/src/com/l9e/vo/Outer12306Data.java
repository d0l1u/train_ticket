package com.l9e.vo;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Outer12306Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<OuterSoukdData> errorInfo;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<OuterSoukdData> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<OuterSoukdData> errorInfo) {
		this.errorInfo = errorInfo;
	}

}
