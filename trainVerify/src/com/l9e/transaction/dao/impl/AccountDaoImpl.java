package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.PassWhiteListVo;
import com.l9e.transaction.vo.Passenger;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDao implements AccountDao {

	
	public Account queryAccountForVerify(Map<String, Object> paramMap) {
		return (Account) this.getSqlMapClientTemplate().queryForObject("account.queryAccountForVerify", paramMap);
	}

	
	public int udpateAccountStatusByAccId(Map<String, String> beginMap) {
		return this.getSqlMapClientTemplate().update("account.udpateAccountStatusByAccId", beginMap);
	}

	
	public int queryAccoutExistInFilter(List<Passenger> userList) {
		return this.getTotalRows("account.queryAccoutFilterCount", userList);
	}

	
	public int updateAccoutFree(Account account) {
		return this.getSqlMapClientTemplate().update("account.updateAccoutFree", account);
	}

	
	public int queryAccountFilterCountById(String ids_card) {
		return this.getTotalRows("account.queryAccountFilterCountById", ids_card);
	}

	
	public void addUserToAccountFilter(AccountFilter af) {
		this.getSqlMapClientTemplate().insert("account.addUserToAccountFilter", af);
		
	}

	@SuppressWarnings("unchecked")
	
	public List<Account> queryAliveAccouts(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAliveAccouts",map);
	}

	
	public void updateAccoutLivetimeBatch(List<Account> accounts) {
		this.getSqlMapClientTemplate().update("account.updateAccoutLivetimeBatch", accounts);
	}

	
	public int queryAliveAccoutsCount(String channel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("channel", channel);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("account.queryAliveAccoutsCount", map);
	}

	@SuppressWarnings("unchecked")
	
	public List<Account> querySupplyAccouts(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("account.querySupplyAccouts", paramMap);
	}

	
	public int updateSupplyAccoutsAlive(List<Account> accounts) {
		return this.getSqlMapClientTemplate().update("account.updateSupplyAccoutsAlive", accounts);
	}

	
	public int updateLimitAccountStop(Account account) {
		return this.getSqlMapClientTemplate().update("account.updatelimitAccountStop", account);
	}

	
	public void updateWorkerinfoNum() {
		this.getSqlMapClientTemplate().update("account.updateWorkerinfoNum");
	}

	
	public List<String> queryAccountIdForVerify(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccountIdForVerify", paramMap);
	}

	
	public void updateCPOrderStatusAndMoney(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("account.updateCPOrderStatusAndMoney", paramMap);
	}

	
	public String queryOrderStatus(String orde_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("account.queryOrderStatus", orde_id);
	}

	
	public void updateAccountActive(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("account.updateAccountActive", map);
	}

	
	public Map<String, String> queryAccountByName(Map<String, Object> map) {
		List<Map<String, String>> accountList = this.getSqlMapClientTemplate().queryForList("account.queryAccountByName", map);
		if(accountList.size() > 0) {
			return accountList.get(0);
		}
		return null;
	}

	
	public int queryAccoutsCount(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("account.queryAccoutsCount", map);
	}
	
	public List<Account> queryAccount(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccount", map);
	}

	
	public void updateModifyStatus(Account account) {
		this.getSqlMapClientTemplate().update("account.updateModifyStatus", account);
	}

	
	public int queryAccoutIsNotInFilter(Passenger p) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("account.queryAccoutIsNotInFilter",p);
	}

	
	public int queryAccountPassWhiteListCount(Map queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("account.queryAccountPassWhiteListCount",queryMap);
	}

	
	public void addUserToPassWhiteList(PassWhiteListVo passWhiteListVo) {
		this.getSqlMapClientTemplate().insert("account.addUserToPassWhiteList",passWhiteListVo);
		
	}


	@Override
	public Map<String, String> queryCtripCard() {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("account.queryCtripCard");
	}


	@Override
	public void updateCardInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("account.updateCardInfo", map);
	}


	@Override
	public void addCtripAccount(Map<String, String> insertMap) {
		this.getSqlMapClientTemplate().insert("account.addCtripAccount", insertMap);
	}


	@Override
	public String queryCtripAccDegree(String banlance) {
		// TODO Auto-generated method stub
		return (String) this.getSqlMapClientTemplate().queryForObject("account.queryCtripAccDegree", banlance);
	}


	@Override
	public List<Map<String, String>> queryCtripAccount(
			Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("account.queryCtripAccount", paramMap);
	}


	@Override
	public Map<String, String> queryRegEmail() {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("account.queryRegEmail");
	}
	
	@Override
	public Map<String, String> queryModifyEmail() {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("account.queryModifyEmail");
	}
	@Override
	public void updateModifyEmail(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("account.updateModifyEmail", map);
	}

	@Override
	public void updateRegEmail(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("account.updateRegEmail", map);
	}
	
	@Override
	public Map<String, Object> selectRandomOneCtripAcc(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("account.selectRandomOneCtripAcc", paramMap);
	}


	@Override
	public Map<String, Object> selectRandomOneCtripCard(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("account.selectRandomOneCtripCard", paramMap);
	}


	@Override
	public int updateCtripAccountInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("account.updateCtripAccountInfo", paramMap);
	}


	@Override
	public int updateCtripCardInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("account.updateCtripCardInfo", paramMap);
	}


	@Override
	public List<Map<String, String>> queryCtripAccInfo() {
		return this.getSqlMapClientTemplate().queryForList("account.queryCtripAccInfo");
	}
	
	@Override
	public Map<String, Object> queryCtripAccInfoByBack() {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("account.queryCtripAccInfoByBack");
	}


	@Override
	public void updateAccountById(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("account.updateAccountById", map);
	}


	@Override
	public void updateAccountInfoById(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("account.updateAccountInfoById", map);
		
	}


	@Override
	public List<Map<String, Object>> queryCtripMailInfo(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("account.queryCtripMailInfo", map);
	}

}
