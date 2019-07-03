package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.PassWhiteListVo;
import com.l9e.transaction.vo.Passenger;

public interface AccountDao {
	List<String> queryAccountIdForVerify(Map<String, Object> paramMap);

	Account queryAccountForVerify(Map<String, Object> paramMap);

	int udpateAccountStatusByAccId(Map<String, String> beginMap);

	int queryAccoutExistInFilter(List<Passenger> userList);

	void updateAccountActive(Map<String, Object> map);

	int updateAccoutFree(Account account);

	int queryAccountFilterCountById(String ids_card);

	void addUserToAccountFilter(AccountFilter af);

	List<Account> queryAliveAccouts(Map<String, Object> map);

	void updateAccoutLivetimeBatch(List<Account> accounts);

	int queryAliveAccoutsCount(String channel);

	List<Account> querySupplyAccouts(Map<String, Object> paramMap);

	int updateSupplyAccoutsAlive(List<Account> accounts);

	int updateLimitAccountStop(Account account);

	void updateWorkerinfoNum();

	String queryOrderStatus(String orde_id);
	
	Map<String, String> queryAccountByName(Map<String, Object> map);

	void updateCPOrderStatusAndMoney(Map<String, Object> paramMap);
	
	int queryAccoutsCount(Map<String, Object> map);
	
	List<Account> queryAccount(Map<String, Object> map);
	
	void updateModifyStatus(Account account);

	int queryAccoutIsNotInFilter(Passenger p);
	
	int queryAccountPassWhiteListCount(Map queryMap);
	
	void addUserToPassWhiteList(PassWhiteListVo passWhiteListVo);

	Map<String, String> queryCtripCard();

	void updateCardInfo(Map<String, String> map);

	void addCtripAccount(Map<String, String> insertMap);

	String queryCtripAccDegree(String banlance);

	List<Map<String, String>> queryCtripAccount(Map<String, String> paramMap);

	Map<String, String> queryRegEmail();

	void updateRegEmail(Map<String, String> map);
	//查询未修改过密码的邮箱 
	Map<String, String> queryModifyEmail();
	void updateModifyEmail(Map<String, Object> updateMailMap);
	//新增4个携程接口
	/**
	 * 随机获取一个携程账号
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> selectRandomOneCtripAcc(Map<String, Object> paramMap);
	
	/**
	 * 随机获取一个礼品卡账号
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> selectRandomOneCtripCard(Map<String, Object> paramMap);
	
	/**
	 * 更新携程账号信息
	 * @param paramMap
	 * @return
	 */
	int updateCtripAccountInfo(Map<String, Object> paramMap);
	
	/**
	 * 更新携程礼品卡状态 
	 * @param paramMap
	 * @return
	 */
	int updateCtripCardInfo(Map<String, Object> paramMap);

	List<Map<String, String>> queryCtripAccInfo();

	Map<String, Object> queryCtripAccInfoByBack();

	void updateAccountById(Map<String, Object> map);

	void updateAccountInfoById(Map<String, String> map);

	List<Map<String, Object>> queryCtripMailInfo(Map<String, Object> map);
}
