package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.List;

public class ReturnData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//{"loginId":"qiufm030411","contacts":[{"idType":"1","ticketType":"1","name":"张俊","id":"320481198702116616","message":"","status":"exist"}],"password":"123456a"}]
	
	private String loginId;
	
	private String password;
	
	private List<ReturnUser> contacts;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ReturnUser> getContacts() {
		return contacts;
	}

	public void setContacts(List<ReturnUser> contacts) {
		this.contacts = contacts;
	}

}
