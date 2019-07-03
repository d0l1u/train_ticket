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
	private String product_id;
	private String cp_id;//车票id
	private String from_name;//出发地址
	private String to_name;//到达地址
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
	private String effect_date;//生效日期
	private String train_no;//车次
	private String bx_channel;//保险渠道: 1、快保 2、合众
	private String order_channel;//订单渠道
	private String merchant_id;//商户id
	
	public String getOrder_channel() {
		return order_channel;
	}
	public void setOrder_channel(String orderChannel) {
		order_channel = orderChannel;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchantId) {
		merchant_id = merchantId;
	}
	public String getBx_id() {
		return bx_id;
	}
	public void setBx_id(String bxId) {
		bx_id = bxId;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String productId) {
		product_id = productId;
	}
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cpId) {
		cp_id = cpId;
	}
	public String getFrom_name() {
		return from_name;
	}
	public void setFrom_name(String fromName) {
		from_name = fromName;
	}
	public String getTo_name() {
		return to_name;
	}
	public void setTo_name(String toName) {
		to_name = toName;
	}
	public String getIds_type() {
		return ids_type;
	}
	public void setIds_type(String idsType) {
		ids_type = idsType;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(String userIds) {
		user_ids = userIds;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modifyTime) {
		modify_time = modifyTime;
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
	public void setBx_status(String bxStatus) {
		bx_status = bxStatus;
	}
	public String getBx_code() {
		return bx_code;
	}
	public void setBx_code(String bxCode) {
		bx_code = bxCode;
	}
	public String getBx_billno() {
		return bx_billno;
	}
	public void setBx_billno(String bxBillno) {
		bx_billno = bxBillno;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String payMoney) {
		pay_money = payMoney;
	}
	public String getBuy_money() {
		return buy_money;
	}
	public void setBuy_money(String buyMoney) {
		buy_money = buyMoney;
	}
	public String getEffect_date() {
		return effect_date;
	}
	public void setEffect_date(String effectDate) {
		effect_date = effectDate;
	}
	public String getTrain_no() {
		return train_no;
	}
	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}
	public String getBx_channel() {
		return bx_channel;
	}
	public void setBx_channel(String bxChannel) {
		bx_channel = bxChannel;
	}
}
