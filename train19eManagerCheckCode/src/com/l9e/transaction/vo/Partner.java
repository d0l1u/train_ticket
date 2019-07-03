package com.l9e.transaction.vo;

public class Partner {
	private String sign;
	private String partner_id;
	private String partner_key;
	private String partner_status;
	//用户状态  以后可扩张该功能
	public static final String UP="00";
	public static final String DOWN="00";
	public String getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(String partnerId) {
		partner_id = partnerId;
	}
	public String getPartner_key() {
		return partner_key;
	}
	public void setPartner_key(String partnerKey) {
		partner_key = partnerKey;
	}
	public String getPartner_status() {
		return partner_status;
	}
	public void setPartner_status(String partnerStatus) {
		partner_status = partnerStatus;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
