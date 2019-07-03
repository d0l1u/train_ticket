package com.l9ea.train.service;

import com.l9e.train.po.Account;


/**
 * 12306账号接入服务
 * @author licheng
 *
 */
public interface AccountService {

	/**
	 * 按渠道获取账号
	 * @param channel
	 * @return
	 */
	public Account getChannelAccount(String channel,Integer passengerSize);
	
	/**
	 * 按账号id获取账号
	 * @param id
	 * @return
	 */
	public Account getOrderAccount(Integer id);
	
	/**
	 * 修改账号(主键id必备，赋值属性，全部修改)
	 * @param account
	 */
	void updateAccount(Account account);
	
	/**
	 * 停用账号
	 * @param id
	 * @param reason 停用原因
	 */
	void stopAccount(Integer id, String reason);
	
	/**
	 * 乘客证件号码匹配账号
	 * @param passportNo
	 * @return
	 */
	Account filterAccount(String passportNo);
	
	/**
	 * 释放账号
	 * @param id
	 */
	void releaseAcoount(Integer id);
	
	/**
	 * 对自带12306账号登录验证的结果处理
	 * @param userName，pass,passportNo
	 * @return errorCode
	 */
	String handleBindAccErrorCode(String userName,String pass,String passportNo);
}
