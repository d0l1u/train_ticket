package com.l9e.common;

import java.io.Serializable;

/**
 * 员工登陆存放session的实体类
 * 
 * @author
 */
public class LoginUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String cm_phone;
	
	private String terminal;//web、wap、pc

	public String getCm_phone() {
		return cm_phone;
	}

	public void setCm_phone(String cm_phone) {
		this.cm_phone = cm_phone;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

}
