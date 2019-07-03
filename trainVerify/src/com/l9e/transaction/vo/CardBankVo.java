package com.l9e.transaction.vo;

/**
 * @author daiqh
 *
 */

public class CardBankVo {
	
	private String card_id;//卡编号
	private String card_no;//卡号
	private String worker_id;
	private String card_pwd;//密码
	private String pay_type;//支付类型：00、借记卡网银支付 11、借记卡快捷支付 22、中铁银通卡 33、信用卡快捷支付 44、信用卡网银支付 55、支付宝
	private String card_phone;//绑定手机号
	private String card_status;//卡状态 00：正在付款 11：等待付款 22：暂停付款
	private String card_remain;//余额
	private String bank_type;//银行类型 00、中国银行 11、建设银行 22、中铁银通卡 33、支付宝 44、招商银行
	private String card_ext;//卡扩展
	private String create_time;//余额更新时间
	private String ids_card;//身份证号
	private String com_no;//短信猫COM口号
	
	
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
	public String getCard_remain() {
		return card_remain;
	}
	public void setCard_remain(String cardRemain) {
		card_remain = cardRemain;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getIds_card() {
		return ids_card;
	}
	public void setIds_card(String idsCard) {
		ids_card = idsCard;
	}
	public String getCom_no() {
		return com_no;
	}
	public void setCom_no(String comNo) {
		com_no = comNo;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String cardId) {
		card_id = cardId;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String cardNo) {
		card_no = cardNo;
	}
	public String getWorker_id() {
		return worker_id;
	}
	public void setWorker_id(String workerId) {
		worker_id = workerId;
	}
	
}
