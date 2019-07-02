package com.l9e.transaction.vo;

import java.io.Serializable;

public class VerifySuccPassenger implements Serializable{

	private static final long serialVersionUID = 1L;
//    "verification_status": "1",
//    "passenger_type_name": "成人",
//    "passenger_id_type_code": "1",
//    "passenger_id_no": "130105199210291211",
//    "passenger_type": "1",
//    "verification_status_name": "已通过",
//    "passenger_id_type_name": "二代身份证",
//    "passenger_name": "闫益恒"

	private String verification_status;
	
	private String passenger_type_name;
	
	private String passenger_id_type_code;
	
	private String passenger_id_no;
	
	private String passenger_type;
	
	private String verification_status_name;
	
	private String passenger_id_type_name;
	
	private String passenger_name;

	public String getVerification_status() {
		return verification_status;
	}

	public void setVerification_status(String verification_status) {
		this.verification_status = verification_status;
	}

	public String getPassenger_type_name() {
		return passenger_type_name;
	}

	public void setPassenger_type_name(String passenger_type_name) {
		this.passenger_type_name = passenger_type_name;
	}

	public String getPassenger_id_type_code() {
		return passenger_id_type_code;
	}

	public void setPassenger_id_type_code(String passenger_id_type_code) {
		this.passenger_id_type_code = passenger_id_type_code;
	}

	public String getPassenger_id_no() {
		return passenger_id_no;
	}

	public void setPassenger_id_no(String passenger_id_no) {
		this.passenger_id_no = passenger_id_no;
	}

	public String getPassenger_type() {
		return passenger_type;
	}

	public void setPassenger_type(String passenger_type) {
		this.passenger_type = passenger_type;
	}

	public String getVerification_status_name() {
		return verification_status_name;
	}

	public void setVerification_status_name(String verification_status_name) {
		this.verification_status_name = verification_status_name;
	}

	public String getPassenger_name() {
		return passenger_name;
	}

	public void setPassenger_name(String passenger_name) {
		this.passenger_name = passenger_name;
	}

	public String getPassenger_id_type_name() {
		return passenger_id_type_name;
	}

	public void setPassenger_id_type_name(String passenger_id_type_name) {
		this.passenger_id_type_name = passenger_id_type_name;
	}
	
	
	

}
