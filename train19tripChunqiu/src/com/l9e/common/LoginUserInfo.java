package com.l9e.common;

import java.io.Serializable;

/**
 * 员工登陆存放session的实体类
 * 
 * @author
 */
public class LoginUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sid;
	
	private String userId;
	
	private String itemNo;
	
	private String cityName;
	
	private String cellPhone;
	
	private String terminal;//web、wap、pc

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
}
