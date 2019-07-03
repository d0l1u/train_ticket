package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Outer12306NewData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<OuterSoukdNewData> errorInfo;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<OuterSoukdNewData> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<OuterSoukdNewData> errorInfo) {
		this.errorInfo = errorInfo;
	}

}
