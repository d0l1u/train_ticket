package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CtripAccountDao;

@Repository("ctripAccountDao")
public class CtripAccountDaoImpl extends BaseDao implements CtripAccountDao  {

	@Override
	public void insertCtripAccount(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("ctripAccount.insertCtripAccount",paramMap);
	}

	@Override
	public void insertCtriplogs(Map<String, String> logs) {
		this.getSqlMapClientTemplate().insert("ctripAccount.insertCtriplogs",logs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryCtripAccount(String ctripId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("ctripAccount.queryCtripAccount",ctripId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCtripAccountList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("ctripAccount.queryCtripAccountList",paramMap);
	}

	@Override
	public int queryCtripAccountListCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("ctripAccount.queryCtripAccountListCount",paramMap);
	}

	@Override
	public void updateCtripAccount(Map<String, String> paramMap)  {
		this.getSqlMapClientTemplate().update("ctripAccount.updateCtripAccount",paramMap);
	}

	@Override
	public String queryCtrip_name(String ctripName) {
		return (String)this.getSqlMapClientTemplate().queryForObject("ctripAccount.queryCtrip_name",ctripName);
	}

	@Override
	public int queryCtripAccountLogCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("ctripAccount.queryCtripAccountLogCount");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCtripAccountLogList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("ctripAccount.queryCtripAccountLogList",paramMap);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAmountArea() {
		return this.getSqlMapClientTemplate().queryForList("ctripAccount.queryAmountArea");
	}

	@Override
	public int updateAmountArea(Map<String, Object> param) {
		
		return this.getSqlMapClientTemplate().update("ctripAccount.updateAmountArea", param);
	}


}
