package com.l9e.common;

import java.io.Serializable;

/**
 * 员工登陆存放session的实体类
 * 
 * @author
 */
public class LoginUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String plat_order_url; //train通知19PAY下单时使用该地址。
	
	private String user_name;	//用户姓名
	
	private String terminal;//web、wap、pc
	
	private String user_id; //用户id
	
	

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String userId) {
		user_id = userId;
	}

	

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getPlat_order_url() {
		return plat_order_url;
	}

	public void setPlat_order_url(String platOrderUrl) {
		plat_order_url = platOrderUrl;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String userName) {
		user_name = userName;
	}

}
