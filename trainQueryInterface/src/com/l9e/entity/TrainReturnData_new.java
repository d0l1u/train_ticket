package com.l9e.entity;

import java.util.List;



public class TrainReturnData_new {
	
	
	
	private String validateMessagesShowId;
	
	private String status;
	
	private String httpstatus;

	private List<TrainNewMidData_new> data;

	public String getValidateMessagesShowId() {
		return validateMessagesShowId;
	}

	public void setValidateMessagesShowId(String validateMessagesShowId) {
		this.validateMessagesShowId = validateMessagesShowId;
	}

	public String getStatus() {
		return status;
	}

	public List<TrainNewMidData_new> getData() {
		return data;
	}

	public void setData(List<TrainNewMidData_new> data) {
		this.data = data;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHttpstatus() {
		return httpstatus;
	}

	public void setHttpstatus(String httpstatus) {
		this.httpstatus = httpstatus;
	}
	
	private List<Messages> messages;
	
	private ValidateMessages validateMessages;

	public List<Messages> getMessages() {
		return messages;
	}

	public void setMessages(List<Messages> messages) {
		this.messages = messages;
	}

	public ValidateMessages getValidateMessages() {
		return validateMessages;
	}

	public void setValidateMessages(ValidateMessages validateMessages) {
		this.validateMessages = validateMessages;
	}

//	public TrainNewMidData getData() {
//		return data;
//	}
//
//	public void setData(TrainNewMidData data) {
//		this.data = data;
//	}

}
