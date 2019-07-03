package com.l9e.train.po;

/**
 * 京东预付卡实体类
 * @author wangsf01
 *
 */
public class JdPrePayCard {

	public static String CARD_STATUS_FREE = "00";//空闲
	public static String CARD_STATUS_WORKING = "11";//使用中
	public static String CARD_STATUS_STOP = "22";//停用

	public Integer jdCardId;//京东预付卡账号ID
	public String cardNo;//京东预付卡账号
	public String cardPwd;//京东预付卡密码
	public String cardMoney;//京东预付卡余额
	public String cardStatus;//京东预付卡状态
	
	
	public Integer getJdCardId() {
		return jdCardId;
	}
	public void setJdCardId(Integer jdCardId) {
		this.jdCardId = jdCardId;
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
	public String getCardMoney() {
		return cardMoney;
	}
	public void setCardMoney(String cardMoney) {
		this.cardMoney = cardMoney;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	
	
}
