package com.l9e.transaction.service;

import java.util.List;

import com.l9e.transaction.vo.Account;

/**
 * 12306账号业务接口
 * 
 * @author licheng
 *
 */
public interface AccountService {

	/**
	 * 填充账号
	 * 
	 * @param channel
	 * @param startWith 
	 * @param lLen 
	 */
	void setAccount(String channel, Integer passengerSize, String startWith, int limit);

	/**
	 * 按渠道获取一个12306账号
	 * 
	 * @param channel
	 * @return
	 */
	Account getAccount(String channel, Integer passengerSize);

	/**
	 * 从数据库中查询12306账号
	 * 
	 * @param accountId
	 *            账号主键id
	 * @return
	 */
	Account queryAccountById(Integer accountId);

	/**
	 * 更新账号信息
	 * 
	 * @param account
	 */
	void updateAccount(Account account);

	/**
	 * 停用账号 并返回账号实体
	 * 
	 * @param accountId
	 * @param reason
	 * @return
	 */
	Account stopAccount(Integer accountId, String reason);

	/**
	 * 匹配适用账号
	 * 
	 * @param passportNo
	 *            乘客证件号码
	 * @return
	 */
	// Account filterAccount(String passportNo);

	/**
	 * 匹配适用账号列表 换成白名单表
	 * 
	 * @param passportNus
	 *            乘客证件号码List集合
	 * @return
	 */
	Account filterAccount(List<String> passportNus, String channel);

	/**
	 * 账号预登录，返回预登录匹配机器人id
	 * 
	 * @param account
	 * @return
	 */
	@Deprecated
	Integer preparedSign(Account account);

	/**
	 * 白名单更改账号表信息
	 * 
	 * @param id:账号ID
	 *            realCheckStatus：账号核验状态 realReceive：手机核验状态
	 *            realContactNum：真实联系人个数 status:账号状态 contact：常用联系人个数
	 */

	void updateAccountInfo(Integer id, Byte realCheckStatus, String realReceive, Integer realContactNum, String status,
			Integer contact);

	/**
	 * 白名单表匹配到历史账号的ID和次数
	 * 
	 * @param passportNus
	 *            乘客证件号码List集合
	 * @return
	 */
	Account queryAccountMappingNum(List<String> passportNus);

	/**
	 * 对自带12306账号订单的错误信息处理
	 * 
	 * @return 错误码
	 */
	String bindAccountErrorCode(String userName, String pass, List<String> passportNus);

	/**
	 * 自带账号订单改签退票时，实时更新账号密码
	 * 
	 * @param params
	 */
	void updateAccountPwd(String userName, String passWord);

	/**
	 * @Title: reset2Free
	 * @Description: 重置账号为空闲
	 * @author: taokai
	 * @date: 2017年8月10日 下午3:44:04
	 * @return int
	 */
	int reset2Free();
}
