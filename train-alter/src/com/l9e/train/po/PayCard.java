package com.l9e.train.po;

public class PayCard {

	private String cardId;
	private String cardNo;
	private String cardPwd;
	private String cardPhone;
	private String payStatus;
	private String bankType;
	private String cardRemain;
	private String payType;
	private String cardExt;
	
	
	public static String PAYTYPE_JJ_NETBANK = "00"; //网银支付
	public static String PAYTYPE_JJ_FAST = "11"; //借记卡快捷支付
	public static String PAYTYPE_ZT = "22"; //中铁银通卡支付
	public static String PAYTYPE_XY_FAST = "33"; //信用卡快捷支付
	public static String PAYTYPE_XY_NETBANK = "33"; //信用卡快捷支付
	
	
	public static String STATUS_USERING = "00"; //正在支付
	public static String STATUS_WAITUSER = "11"; //等待支付
	public static String STATUS_STOP = "22"; //暂停支付

	
	public static String BANKTYPE_BOC = "00"; //中国银行
	public static String BANKTYPE_CCB = "11"; //建设银行
	public static String BANKTYPE_ZT = "33"; //中铁银通卡
	
	
	
	
	public String getCardExt() {
		return cardExt;
	}
	public void setCardExt(String cardExt) {
		this.cardExt = cardExt;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardPwd() {
		return cardPwd;
	}
	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}
	public String getCardPhone() {
		return cardPhone;
	}
	public void setCardPhone(String cardPhone) {
		this.cardPhone = cardPhone;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getCardRemain() {
		return cardRemain;
	}
	public void setCardRemain(String cardRemain) {
		this.cardRemain = cardRemain;
	}
	
	
	
	
	
}
