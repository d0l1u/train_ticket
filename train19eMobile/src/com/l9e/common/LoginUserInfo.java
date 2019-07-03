package com.l9e.common;

import java.io.Serializable;

/**
 * 员工登陆存放session的实体类
 * 
 * @author
 */
public class LoginUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String eop_order_url;//ASP通知EOP下单时使用该地址
	
	private String agentId;//网点账户ID
	
	private String agentName;//网点昵称
	
	private String eopAgentLevel;//EOP定义的网点等级
	
	private String provinceId;//网点所在省份ID
	
	private String cityId;//网点所在市ID
	
	private String districtId;//网点所在区ID
	
	private String terminal;//EOP提供（web、wap、pc）

	public String getEop_order_url() {
		return eop_order_url;
	}

	public void setEop_order_url(String eop_order_url) {
		this.eop_order_url = eop_order_url;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getEopAgentLevel() {
		return eopAgentLevel;
	}

	public void setEopAgentLevel(String eopAgentLevel) {
		this.eopAgentLevel = eopAgentLevel;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

}
