package com.l9e.transaction.vo;

import java.io.Serializable;

public class OrderInfoCp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cp_id;
	private String order_id;
	private String user_name;//用户名
	private String ticket_type;//车票类型0：成人票 1：儿童票
	private String ids_type;//证件类型1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
	private String user_ids;//证件号码
	private String telephone;//联系电话
	private String create_time;//创建时间
	private String pay_money;//订购价格
	private String buy_money;//购买价格
	private String modify_time;
	private String seat_type;//0：商务座 1：特等座 2：一等座 3：二等座 4：高级软卧 5：软卧 6：硬卧 7: 软座 8：硬座 9：无座 10：其他
	
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cp_id) {
		this.cp_id = cp_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getTicket_type() {
		return ticket_type;
	}
	public void setTicket_type(String ticket_type) {
		this.ticket_type = ticket_type;
	}
	public String getIds_type() {
		return ids_type;
	}
	public void setIds_type(String ids_type) {
		this.ids_type = ids_type;
	}
	public String getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
	public String getSeat_type() {
		return seat_type;
	}
	public void setSeat_type(String seat_type) {
		this.seat_type = seat_type;
	}
}
