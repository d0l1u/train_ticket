package com.nineteen.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class AlipayUser {

	private int card_id;//卡编号
	private String card_no;//卡号
	private String card_pwd;//支付密码
	private String pay_type;//支付类型：00、借记卡网银支付 11、借记卡快捷支付  22、中铁银通卡 33、信用卡快捷支付 44、信用卡网银支付 55、支付宝
	private String card_phone;//绑定手机号
	private String card_status;//卡状态 00：正在付款 11：等待付款 22：暂停付款
	private BigDecimal decimal;//余额
	private String bank_type;//银行类型 00、中国银行 11、建设银行 22、中铁银通卡 33、支付宝
	private String card_ext;//卡扩展
	private String card_login_pwd;//登录密码
	private String create_time;//时间类型
	private int worker_id;
	public int getCard_id() {
		return card_id;
	}
	public void setCard_id(int cardId) {
		card_id = cardId;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String cardNo) {
		card_no = cardNo;
	}
	public String getCard_pwd() {
		return card_pwd;
	}
	public void setCard_pwd(String cardPwd) {
		card_pwd = cardPwd;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String payType) {
		pay_type = payType;
	}
	public String getCard_phone() {
		return card_phone;
	}
	public void setCard_phone(String cardPhone) {
		card_phone = cardPhone;
	}
	public String getCard_status() {
		return card_status;
	}
	public void setCard_status(String cardStatus) {
		card_status = cardStatus;
	}
	public BigDecimal getDecimal() {
		return decimal;
	}
	public void setDecimal(BigDecimal decimal) {
		this.decimal = decimal;
	}
	public String getBank_type() {
		return bank_type;
	}
	public void setBank_type(String bankType) {
		bank_type = bankType;
	}
	public String getCard_ext() {
		return card_ext;
	}
	public void setCard_ext(String cardExt) {
		card_ext = cardExt;
	}
	public String getCard_login_pwd() {
		return card_login_pwd;
	}
	public void setCard_login_pwd(String cardLoginPwd) {
		card_login_pwd = cardLoginPwd;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public int getWorker_id() {
		return worker_id;
	}
	public void setWorker_id(int workerId) {
		worker_id = workerId;
	}
	public AlipayUser(int cardId, String cardNo, String cardPwd,
			String payType, String cardPhone, String cardStatus,
			BigDecimal decimal, String bankType, String cardExt,
			String cardLoginPwd, String createTime, int workerId) {
		super();
		card_id = cardId;
		card_no = cardNo;
		card_pwd = cardPwd;
		pay_type = payType;
		card_phone = cardPhone;
		card_status = cardStatus;
		this.decimal = decimal;
		bank_type = bankType;
		card_ext = cardExt;
		card_login_pwd = cardLoginPwd;
		create_time = createTime;
		worker_id = workerId;
	}
	public AlipayUser() {
		super();
	}
	
	
}
