package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.Passenger;

public interface AccountDao {
	String queryAccountIdForVerify(Map<String, Object> paramMap);

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

	int updateAccount(Account account);

	void updateModifyStatus(Account account);

	int updateBookNum(Map<String, Object> map);

	List<Map<String,String>> queryAccoutPriority(Map<String, Object> map);
	
	List<Map<String,String>> queryAccoutWaitList(Map<String, Object> map);
	
	void updateAccountPriority(Map<String, String> map);
	
	void updateAccountStatus(Map<String, String> map);
	
	Map<String, String> querySettingMap();
	
	List<String> queryWaitOpenAccountList(Map<String, Object> setMap);
	
	int openAccountByAccId(String accId);

	void addAccWhiteInfo(Map<String, Object> map);

	List<Map<String, Object>> queryAccInfoList(Map<String, Object> paramMap);

	int updateAccInfo(Map<String, Object> updateMap);

	void updateAccWhiteInfo(Map<String, Object> updatWhiteMap);

	Map<String, Object> queryAccInfoIsExist(Map<String, Object> paramMap);

	void addAccIncreInfo(Map<String, Object> addIncreMap);

	Map<String, Object> queryAccIncreInfo(Map<String, Object> paramMap);

	void updateAccIncreInfo(Map<String, Object> updatWhiteMap);

	void updateAccountInfo(Map<String, Object> updateMap);

	Account selectJdAccount(String status);
	void updateJdAccount(Integer accountId, String status);

	void disableJdAccount(Integer accountId, String reason);

	 /**   
	 * 删除白名单    
	 * @author: taoka
	 * @date: 2017年11月29日 下午1:35:47
	 * @param accountId void
	 */
	void deleteWhiteListByAccount(Integer accountId);

	 /**   
	 * 删除集合外的白名单    
	 * @author: taoka
	 * @param accountId 
	 * @date: 2017年11月29日 下午4:32:18
	 * @param cardNoList
	 * @return Integer
	 */
	Integer deleteWhiteList(Integer accountId, List<String> cardNoList);
}
