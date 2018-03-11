package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AreaVo;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDao implements AccountDao{
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAccountList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccountList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAccountExcel(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("account.queryAccountExcel", paramMap);
	}
	
	@Override
	public int queryAccountListCount(Map<String, Object> paramMap) {
		return getTotalRows("account.queryAccountListCount", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> queryAccount(String acc_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("account.queryAccount", acc_id);
	}

	@Override
	public void updateAccount(AccountVo  account) {
		this.getSqlMapClientTemplate().update("account.updateAccount", account);
	}
	
	@Override
	public void insertAccount(AccountVo  account) {
		this.getSqlMapClientTemplate().insert("account.insertAccount", account);
	}
	
	@Override
	public void deleteAccount(AccountVo account) {
		this.getSqlMapClientTemplate().update("account.deleteAccount", account);
	}
	
	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("account.getProvince");
	}
	
	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("account.getCity", provinceid);
	}
	
	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("account.getArea", cityid);
	}

	public String queryAcc_username(String acc_username) {
		return (String) this.getSqlMapClientTemplate().queryForObject("account.queryAcc_username",acc_username);
	}

	@Override
	public void insertAcc_logs(AccountVo account) {
		this.getSqlMapClientTemplate().insert("account.insertAcc_logs",account);
	}


	@Override
	public void addAccounts(List<Map<String, Object>> queryRegisters) {
		this.getSqlMapClientTemplate().insert("account.addAccounts",queryRegisters);
		
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRegisters(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("account.queryRegisterList",paramMap);
	}

	@Override
	public void updateRegisters(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("account.updateRegisters", map);
	}

	@Override
	public void updateStopStatus() {
		this.getSqlMapClientTemplate().update("account.updateStopStatus");
	}

	@Override
	public void updateAbatchStop(AccountVo account) {
		this.getSqlMapClientTemplate().update("account.updateAbatchStop",account);
	}
	
	@Override
	public int startAccounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().update("account.startAccounts" ,paramMap);
	}
	
}
