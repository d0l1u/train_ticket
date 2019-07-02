package com.l9e.common;

import java.io.Serializable;

/**
 * 员工登陆存放session的实体类
 * 
 * @author
 */
public class LoginUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user_phone;
	private String user_id;
	private String terminal;// web、wap、pc

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
