package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * hc_orderinfo_ps
 *
 */
public class OrderInfoPs implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String order_id;//订单号	
	private String ps_billno;//配送单号
	private String ps_company;//配送公司
	private String pay_money;//配送费用
	private String buy_money;//成本价格
	private String ps_status;//配送状态：00、等待发送 22、正在配送 33、配送成功
	private String create_time;//创建时间
	private String modify_time;//修改时间
	private String link_name;//联系人姓名
	private String link_phone;//联系人电话
	private String link_address;//联系人地址
	private String link_mail;//联系人邮件
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getPs_billno() {
		return ps_billno;
	}
	public void setPs_billno(String ps_billno) {
		this.ps_billno = ps_billno;
	}
	public String getPs_company() {
		return ps_company;
	}
	public void setPs_company(String ps_company) {
		this.ps_company = ps_company;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}
	public String getBuy_money() {
		return buy_money;
	}
	public void setBuy_money(String buy_money) {
		this.buy_money = buy_money;
	}
	public String getPs_status() {
		return ps_status;
	}
	public void setPs_status(String ps_status) {
		this.ps_status = ps_status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
	public String getLink_name() {
		return link_name;
	}
	public void setLink_name(String link_name) {
		this.link_name = link_name;
	}
	public String getLink_phone() {
		return link_phone;
	}
	public void setLink_phone(String link_phone) {
		this.link_phone = link_phone;
	}
	public String getLink_address() {
		return link_address;
	}
	public void setLink_address(String link_address) {
		this.link_address = link_address;
	}
	public String getLink_mail() {
		return link_mail;
	}
	public void setLink_mail(String link_mail) {
		this.link_mail = link_mail;
	}

}
