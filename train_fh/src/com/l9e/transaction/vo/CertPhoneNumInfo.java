package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * 携程手机号码实体类
 * 
 * @author wangsf
 * 
 */
public class CertPhoneNumInfo implements Serializable{
	
	private static final long serialVersionUID = 5279588438405002702L;
	
	/**
	 * 手机号ID：主键
	 */
	public  Integer phoneId;
	
	/**
	 * 创建时间
	 */
	public  String  createTime;
	
	/**
	 * 手机号码
	 */
	public  String  phoneNumber;

	
	public Integer getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(Integer phoneId) {
		this.phoneId = phoneId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return "CertPhoneNumInfo [phoneId=" + phoneId + ", createTime="
				+ createTime + ", phoneNumber=" + phoneNumber + "]";
	}

}
