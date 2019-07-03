package com.l9e.transaction.vo;

public class PassWhiteListVo {
	
	private String contact_name;
	private String contact_status;
	private String cert_no;
	private String cert_type;
	private String acc_username;
	private Integer acc_id;
	private String acc_status;
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contactName) {
		contact_name = contactName;
	}
	public String getContact_status() {
		return contact_status;
	}
	public void setContact_status(String contactStatus) {
		contact_status = contactStatus;
	}
	public String getCert_no() {
		return cert_no;
	}
	public void setCert_no(String certNo) {
		cert_no = certNo;
	}
	public String getCert_type() {
		return cert_type;
	}
	public void setCert_type(String certType) {
		cert_type = certType;
	}
	public String getAcc_username() {
		return acc_username;
	}
	public void setAcc_username(String accUsername) {
		acc_username = accUsername;
	}

	public String getAcc_status() {
		return acc_status;
	}
	public void setAcc_status(String accStatus) {
		acc_status = accStatus;
	}
	public Integer getAcc_id() {
		return acc_id;
	}
	public void setAcc_id(Integer accId) {
		acc_id = accId;
	}
	
}
