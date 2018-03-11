package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongAlterDao;

@Repository("elongAlterDao")
public class ElongAlterDaoImpl extends BaseDao implements ElongAlterDao {

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> queryAlterInfo(String changeId) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("elongAlter.queryAlterInfo",changeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAlterTicketList(HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("elongAlter.queryAlterTicketList",paramMap);
	}

	@Override
	public int queryAlterTicketListCounts(HashMap<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("elongAlter.queryAlterTicketListCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HashMap<String, String>> queryLogById(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("elongAlter.queryLogById", order_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HashMap<String, String>>  queryCpInfo(String changeId) {
		return this.getSqlMapClientTemplate().queryForList("elongAlter.queryCpInfo",changeId);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().insert("elongAlter.insertLog",map);
	}

	@Override
	public void updateAlertCpInfo(HashMap<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("elongAlter.updateAlertCpInfo" ,paramMap);
	}

	@Override
	public void updateAlter(HashMap<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("elongAlter.updateAlter" ,paramMap);
	}

	@Override
	public void updateAlterOrder(HashMap<String, Object> ordermap) {
		this.getSqlMapClientTemplate().update("elongAlter.updateAlterOrder" ,ordermap);
	}

}
