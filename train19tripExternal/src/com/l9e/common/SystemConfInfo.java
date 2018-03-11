package com.l9e.common;

import java.io.Serializable;

/**
 * 火车票系统配置信息
 * @author zhangjun
 *
 */
public class SystemConfInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String config_id;//配置ID	
	private String province_id;//省份
	private String is_open;//开通状态：00、关闭 11、开通
	private String is_cost;//是否付费：00、不  11、是
	private String is_ps;//是否开通配送 00、否 11、是
	private String is_buyable;//是否可以购买 00、否 11、是
	private String rule_content;//查询规则
	
	public String getConfig_id() {
		return config_id;
	}
	public void setConfig_id(String config_id) {
		this.config_id = config_id;
	}
	public String getProvince_id() {
		return province_id;
	}
	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}
	public String getIs_open() {
		return is_open;
	}
	public void setIs_open(String is_open) {
		this.is_open = is_open;
	}
	public String getIs_cost() {
		return is_cost;
	}
	public void setIs_cost(String is_cost) {
		this.is_cost = is_cost;
	}
	public String getIs_ps() {
		return is_ps;
	}
	public void setIs_ps(String is_ps) {
		this.is_ps = is_ps;
	}
	public String getRule_content() {
		return rule_content;
	}
	public void setRule_content(String rule_content) {
		this.rule_content = rule_content;
	}
	public String getIs_buyable() {
		return is_buyable;
	}
	public void setIs_buyable(String is_buyable) {
		this.is_buyable = is_buyable;
	}
	
	
}
