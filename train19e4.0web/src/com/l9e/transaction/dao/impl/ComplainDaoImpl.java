package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ComplainDao;

@Repository("complainDao")
public class ComplainDaoImpl extends BaseDao implements ComplainDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryComplainList(String agentId) {
		return this.getSqlMapClientTemplate().queryForList("complain.queryComplainList", agentId);
	}

	public void addComplainInfo(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("complain.addComplainInfo", paramMap);
	}

	public int queryDailyCount(String agentId) {
		Integer count = (Integer) this.getSqlMapClientTemplate().queryForObject("complain.queryDailyCount", agentId);
		return count==null ? 0 : count;
	}

}
