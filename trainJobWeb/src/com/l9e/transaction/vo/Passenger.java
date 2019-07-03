package com.l9e.transaction.vo;

import java.io.Serializable;

public class Passenger implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String cert_no;
	
	private String cert_type;
	
	private String user_name;
	
	private String user_type;//0：成人 1：儿童
	
	private String check_status;//12306身份核验状态 0、已通过 1、审核中 2、未通过
	
	public static final String CHECK_PASSED = "0";//已通过
	
	public static final String CHECKING = "1";//审核中
	
	public static final String CHECK_NOTPASSED = "2";//未通过
	
	public String getCert_no() {
		return cert_no;
	}

	public void setCert_no(String cert_no) {
		this.cert_no = cert_no;
	}

	public String getCert_type() {
		return cert_type;
	}

	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCheck_status() {
		return check_status;
	}

	public void setCheck_status(String check_status) {
		this.check_status = check_status;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

}
