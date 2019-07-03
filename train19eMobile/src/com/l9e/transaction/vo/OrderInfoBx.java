package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * hc_orderinfo_bx
 * @author zhangjun
 *
 */
public class OrderInfoBx implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bx_id;//保险单号
	private String order_id;//订单号
	private String cp_id;//车票id
	private String from_name;//出发地址
	private String to_name;//到达地址
	private String bx_type;//车票类型1：成人票 0：儿童票
	private String ids_type;//证件类型1、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
	private String user_name;//用户姓名
	private String user_ids;//用户证件号
	private String create_time;//创建时间
	private String modify_time;
	private String telephone;//联系电话
	private String bx_status;//状态：0、未发送 1、正在发送 2、发送完成 3、正在撤销 4、撤销完成
	private String bx_code;//保险单号
	private String bx_billno;//服务器端订单号
	private String pay_money;//支付金额
	private String buy_money;//进货金额
	private String product_id;//产品id
	private String effect_date;//生效日期
	private String train_no;//车次
	private String bx_channel;//保险渠道: 1、快保 2、合众
	
	public String getBx_channel() {
		return bx_channel;
	}
	public void setBx_channel(String bx_channel) {
		this.bx_channel = bx_channel;
	}
	public String getTrain_no() {
		return train_no;
	}
	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}
	public String getEffect_date() {
		return effect_date;
	}
	public void setEffect_date(String effect_date) {
		this.effect_date = effect_date;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getBx_id() {
		return bx_id;
	}
	public void setBx_id(String bx_id) {
		this.bx_id = bx_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getFrom_name() {
		return from_name;
	}
	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}
	public String getTo_name() {
		return to_name;
	}
	public void setTo_name(String to_name) {
		this.to_name = to_name;
	}
	public String getIds_type() {
		return ids_type;
	}
	public void setIds_type(String ids_type) {
		this.ids_type = ids_type;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
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
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getBx_status() {
		return bx_status;
	}
	public void setBx_status(String bx_status) {
		this.bx_status = bx_status;
	}
	public String getBx_code() {
		return bx_code;
	}
	public void setBx_code(String bx_code) {
		this.bx_code = bx_code;
	}
	public String getBx_billno() {
		return bx_billno;
	}
	public void setBx_billno(String bx_billno) {
		this.bx_billno = bx_billno;
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
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cp_id) {
		this.cp_id = cp_id;
	}
	public String getBx_type() {
		return bx_type;
	}
	public void setBx_type(String bx_type) {
		this.bx_type = bx_type;
	}

}
