package com.l9e.util.hcpjar.bean;



public class QueryTicketNumBackBean {
	String validateMessagesShowId;
	String status;
	String httpstatus;
	TicketDataBean data;
	String []messages;
	ValidateMessagesBean validateMessages;
	
	
	public ValidateMessagesBean getValidateMessages() {
		return validateMessages;
	}
	public void setValidateMessages(ValidateMessagesBean validateMessages) {
		this.validateMessages = validateMessages;
	}
	public String[] getMessages() {
		return messages;
	}
	public void setMessages(String[] messages) {
		this.messages = messages;
	}
	public String getValidateMessagesShowId() {
		return validateMessagesShowId;
	}
	public void setValidateMessagesShowId(String validateMessagesShowId) {
		this.validateMessagesShowId = validateMessagesShowId;
	}
	public String getStatus() {
		return status;
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
	public TicketDataBean getData() {
		return data;
	}
	public void setData(TicketDataBean data) {
		this.data = data;
	}
	
}
