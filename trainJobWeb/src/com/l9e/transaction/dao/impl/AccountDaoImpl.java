package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.Passenger;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDao implements AccountDao {

	@Override
	public Account queryAccountForVerify(Map<String, Object> paramMap) {
		return (Account) this.getSqlMapClientTemplate().queryForObject("account.queryAccountForVerify", paramMap);
	}

	@Override
	public int udpateAccountStatusByAccId(Map<String, String> beginMap) {
		return this.getSqlMapClientTemplate().update("account.udpateAccountStatusByAccId", beginMap);
	}

	@Override
	public int queryAccoutExistInFilter(List<Passenger> userList) {
		return this.getTotalRows("account.queryAccoutFilterCount", userList);
	}

	@Override
	public int updateAccoutFree(Account account) {
		return this.getSqlMapClientTemplate().update("account.updateAccoutFree", account);
	}

	@Override
	public int queryAccountFilterCountById(String ids_card) {
		return this.getTotalRows("account.queryAccountFilterCountById", ids_card);
	}

	@Override
	public void addUserToAccountFilter(AccountFilter af) {
		this.getSqlMapClientTemplate().insert("account.addUserToAccountFilter", af);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> queryAliveAccouts(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAliveAccouts", map);
	}

	@Override
	public void updateAccoutLivetimeBatch(List<Account> accounts) {
		this.getSqlMapClientTemplate().update("account.updateAccoutLivetimeBatch", accounts);
	}

	@Override
	public int queryAliveAccoutsCount(String channel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("channel", channel);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("account.queryAliveAccoutsCount", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> querySupplyAccouts(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("account.querySupplyAccouts", paramMap);
	}

	@Override
	public int updateSupplyAccoutsAlive(List<Account> accounts) {
		return this.getSqlMapClientTemplate().update("account.updateSupplyAccoutsAlive", accounts);
	}

	@Override
	public int updateLimitAccountStop(Account account) {
		return this.getSqlMapClientTemplate().update("account.updatelimitAccountStop", account);
	}

	@Override
	public void updateWorkerinfoNum() {
		this.getSqlMapClientTemplate().update("account.updateWorkerinfoNum");
	}

	@Override
	public String queryAccountIdForVerify(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("account.queryAccountIdForVerify", paramMap);
	}

	@Override
	public void updateCPOrderStatusAndMoney(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("account.updateCPOrderStatusAndMoney", paramMap);
	}

	@Override
	public String queryOrderStatus(String orde_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("account.queryOrderStatus", orde_id);
	}

	@Override
	public void updateAccountActive(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("account.updateAccountActive", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryAccountByName(Map<String, Object> map) {
		List<Map<String, String>> accountList = this.getSqlMapClientTemplate()
				.queryForList("account.queryAccountByName", map);
		if (accountList.size() > 0) {
			return accountList.get(0);
		}
		return null;
	}

	@Override
	public int queryAccoutsCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("account.queryAccoutsCount", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> queryAccount(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccount", map);
	}

	@Override
	public int updateAccount(Account account) {
		return this.getSqlMapClientTemplate().update("account.updateAccount", account);
	}

	@Override
	public void updateModifyStatus(Account account) {
		this.getSqlMapClientTemplate().update("account.updateModifyStatus", account);

	}

	@Override
	public int updateBookNum(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().update("account.updateBookNum", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAccoutPriority(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccoutPriority", map);
	}

	@Override
	public void updateAccountPriority(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("account.updateAccountPriority", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAccoutWaitList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccoutWaitList", map);
	}

	@Override
	public void updateAccountStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("account.updateAccountStatus", map);
	}

	@Override
	public int openAccountByAccId(String accId) {
		return (Integer) this.getSqlMapClientTemplate().update("account.openAccountByAccId", accId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> querySettingMap() {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("account.querySettingMap");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryWaitOpenAccountList(Map<String, Object> setMap) {
		return this.getSqlMapClientTemplate().queryForList("account.queryWaitOpenAccountList", setMap);
	}

	@Override
	public void addAccWhiteInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("account.addAccWhiteInfo", map);
	}

	@Override
	public List<Map<String, Object>> queryAccInfoList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccInfoList", paramMap);
	}

	@Override
	public int updateAccInfo(Map<String, Object> updateMap) {
		return (Integer) this.getSqlMapClientTemplate().update("account.updateAccInfo", updateMap);
	}

	@Override
	public void updateAccWhiteInfo(Map<String, Object> updatWhiteMap) {
		this.getSqlMapClientTemplate().update("account.updateAccWhiteInfo", updatWhiteMap);
	}

	@Override
	public Map<String, Object> queryAccInfoIsExist(Map<String, Object> paramMap) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("account.queryAccInfoIsExist",
				paramMap);
	}

	@Override
	public void addAccIncreInfo(Map<String, Object> addIncreMap) {
		this.getSqlMapClientTemplate().insert("account.addAccIncreInfo", addIncreMap);
	}

	@Override
	public Map<String, Object> queryAccIncreInfo(Map<String, Object> paramMap) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("account.queryAccIncreInfo",
				paramMap);
	}

	@Override
	public void updateAccIncreInfo(Map<String, Object> updatWhiteMap) {
		this.getSqlMapClientTemplate().update("account.updateAccIncreInfo", updatWhiteMap);
	}

	@Override
	public void updateAccountInfo(Map<String, Object> updateMap) {
		this.getSqlMapClientTemplate().update("account.updateAccountInfo", updateMap);
	}

	@Override
	public Account selectJdAccount(String status) {
		Account account = (Account) this.getSqlMapClientTemplate().queryForObject("account.selectJdAccount", status);
		return account;
	}

	@Override
	public void updateJdAccount(Integer accountId, String status) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", accountId);
		map.put("status", status);
		this.getSqlMapClientTemplate().update("account.updateJdAccount", map);
	}

	@Override
	public void disableJdAccount(Integer accountId, String reason) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", accountId);
		map.put("reason", reason);
		map.put("status", "99");
		this.getSqlMapClientTemplate().update("account.updateJdAccount", map);
	}

	@Override
	public void deleteWhiteListByAccount(Integer accountId) {
		this.getSqlMapClientTemplate().delete("account.deleteWhiteListByAccount", accountId);
	}

	@Override
	public Integer deleteWhiteList(Integer accountId, List<String> cardNoList) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("accountId", accountId);
		map.put("cardNoList", cardNoList);
		return this.getSqlMapClientTemplate().delete("account.deleteWhiteList", map);
	}

}
