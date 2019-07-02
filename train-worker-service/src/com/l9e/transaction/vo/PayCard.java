package com.l9e.transaction.vo;

/**
 * 支付账户信息实体
 * 
 * @author licheng
 * 
 */
public class PayCard {

	/**
	 * 主键id
	 */
	private Integer cardId;
	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 支付类型
	 */
	private String payType;
	/**
	 * 绑定手机
	 */
	private String phone;
	/**
	 * 状态
	 */
	private String payStatus;
	/**
	 * 机器人id
	 */
	private Integer workerId;

	public PayCard() {
		super();
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

}
