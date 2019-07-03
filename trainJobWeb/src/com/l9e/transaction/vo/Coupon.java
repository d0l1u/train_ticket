package com.l9e.transaction.vo;

import java.util.Date;

/**
 * <p>
 * Title: Coupon.java
 * </p>
 * <p>
 * Description: 优惠劵
 * </p>
 * 
 * @author taokai
 * @date 2017年3月7日
 */

public class Coupon {


	/** 优惠劵号 */
	private String couponNo;

	/** 优惠金额 */
	private Double price;

	/** 使用条件金额 */
	private Double limitPrice;

	/** 领取时间 */
	private Date getTime;

	/** 到期时间 */
	private Date endTime;

	/** 渠道 */
	private String channal;
	
	private int accountId;
	
	
	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getChannal() {
		return channal;
	}

	public void setChannal(String channal) {
		this.channal = channal;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}

	public Date getGetTime() {
		return getTime;
	}

	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
