package com.l9e.transaction.vo;

import java.io.Serializable;

public class QunarPassenger implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String certNo;	
	private String certType;	
	private String name;
	private String ticketType;//0:儿童 1:成人 与我们定义的相反
	private PassengerStudentExt passengerStudentExt;
	
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PassengerStudentExt getPassengerStudentExt() {
		return passengerStudentExt;
	}
	public void setPassengerStudentExt(PassengerStudentExt passengerStudentExt) {
		this.passengerStudentExt = passengerStudentExt;
	}
}
