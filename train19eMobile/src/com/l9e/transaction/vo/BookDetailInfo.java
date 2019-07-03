package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * 预订明细信息vo
 * @author zhangjun
 *
 */
public class BookDetailInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String user_name;//姓名
	
	private String ticket_type;//车票类型0：成人票 1：儿童票
	
	private String ids_type;//证件类型1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
	
	private String user_ids;//证件号码
	
	private String product_id;//保险id
	
	private String bx_name;//保险名称
	
	private String sale_price;//保险售价
	
	private String bx_id;//保险单号
	
	private String cp_id;//车票明细单号

	public String getBx_id() {
		return bx_id;
	}

	public void setBx_id(String bx_id) {
		this.bx_id = bx_id;
	}

	public String getCp_id() {
		return cp_id;
	}

	public void setCp_id(String cp_id) {
		this.cp_id = cp_id;
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

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getBx_name() {
		return bx_name;
	}

	public void setBx_name(String bx_name) {
		this.bx_name = bx_name;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

}
