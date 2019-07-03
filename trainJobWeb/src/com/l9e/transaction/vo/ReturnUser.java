package com.l9e.transaction.vo;

import java.io.Serializable;

public class ReturnUser implements Serializable{

	//{"idType":"1","ticketType":"1","name":"张俊","id":"320481198702116616","message":"","status":"exist"}
	
	private static final long serialVersionUID = 1L;

	private String idType;
	
	private String ticketType;
	
	private String name;
	
	private String id;
	
	private String message;
	
	private String status;
	
	public static final String STATUS_FAILUE = "failure";
	
	public static final String STATUS_EXIST = "exist";
	
	public static final String STATUS_CHECKING = "checking";
	
	public static final String STATUS_NOPASS = "nopass";
	
	public static final String STATUS_LIMIT = "limit";

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
