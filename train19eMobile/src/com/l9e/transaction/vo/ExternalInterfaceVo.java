package com.l9e.transaction.vo;

import java.util.List;

public class ExternalInterfaceVo {
	private List<ExternalTrainData> trainData;

	private String return_code;
	
	private String message;
	
	private String left_limit;
	
	public List<ExternalTrainData> getTrainData() {
		return trainData;
	}

	public void setTrainData(List<ExternalTrainData> trainData) {
		this.trainData = trainData;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String returnCode) {
		return_code = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLeft_limit() {
		return left_limit;
	}

	public void setLeft_limit(String left_limit) {
		this.left_limit = left_limit;
	}

}
