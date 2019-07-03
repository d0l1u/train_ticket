package com.l9e.transaction.vo;

import java.util.Date;

/**
 * <p>
 * Title: PrepaidCard.java
 * </p>
 * <p>
 * Description: 预付卡
 * </p>
 * 
 * @author taokai
 * @date 2017年2月24日
 */

public class PrepaidCard {

	private int cardId;
	/** 预付卡账号 */
	private String cardNo;
	/** 预付卡密码 */
	private String cardPwd;
	/** 预付卡余额 */
	private String cardMoney;
	private String cardStatus;
	private Date createTime;
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
