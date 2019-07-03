package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.PassWhiteListVo;
import com.l9e.transaction.vo.Passenger;

public interface AccountService {

	Account queryAccountForVerify(Map<String, Object> paramMap);

	boolean queryAccoutsDBExist(List<Passenger> userList);

	int updateAccoutFree(Account account);

	void addUserToAccountFilter(List<AccountFilter> afs);

	List<Account> queryAliveAccouts(Map<String, Object> map);

	void updateAccountActive(Map<String, Object> map);

	int queryAliveAccoutsCount(String channel);

	void updateSupplyAccountNum(Map<String, Object> paramMap);

	int updateLimitAccountStop(Account account);

	void updateWorkerinfoNum();

	String queryOrderStatus(String order_id);

	void updateCPOrderStatusAndMoney(Map<String, Object> paramMap);

	Map<String, String> queryAccountByName(Map<String, Object> map);

	int queryAccoutsCount(Map<String, Object> map);

	List<Account> queryAccount(Map<String, Object> map);

	void updateModifyStatus(Account account);
	
	void addUserToPassWhiteList(List<PassWhiteListVo> passWhiteListVoList);
	
	/**
	 * 按账号id获取账号
	 * @param id
	 * @return
	 */
	public Account getAccountById(Integer id);
	
	/**
	 * 白名单表匹配到历史账号的ID和次数  
	 * @param passportNo 乘客证件号码串
	 * @return
	 */
	public Account queryAccountMappingNum(String passportNo);

	Map<String, String> queryCtripCard();

	void updateCardInfo(Map<String, String> map);

	List<Map<String, String>> queryCtripAccount(Map<String, String> paramMap);

	String queryCtripAccDegree(String banlance);

	void addCtripAccount(Map<String, String> insertMap);
	
	
	/**
	 * 随机获取一个携程账号
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> queryRandomOneCtripAcc(Map<String, Object> paramMap);
	
	/**
	 * 随机获取一个礼品卡账号
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> queryRandomOneCtripCard(Map<String, Object> paramMap);
	
	/**
	 * 更新携程账号信息
	 * @param paramMap
	 * @return
	 */
	int modifyCtripAccountInfo(Map<String, Object> paramMap);
	
	/**
	 * 更新携程礼品卡状态 
	 * @param paramMap
	 * @return
	 */
	int modifyCtripCardInfo(Map<String, Object> paramMap);

	Map<String, String> queryRegEmail();
	//查询未修改过密码的邮箱 
	Map<String, String> queryModifyEmail();
	
	void updateModifyEmail(Map<String, Object> updateMailMap);
	
	void updateRegEmail(Map<String, String> updateMailMap);

	/**
	 * 从队列中按渠道获取账号
	 * @param channel
	 * @return
	 */
	public Account getChannelAccount(String channel,Integer passengerSize);

	List<Map<String, String>> queryCtripAccInfo();

	Map<String, Object> queryCtripAccInfoByBack();

	void updateAccountById(Map<String, Object> map);

	void updateAccountInfoById(Map<String, String> map);

	List<Map<String, Object>> queryCtripMailInfo(Map<String, Object> map);
	
}
