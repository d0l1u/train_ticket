package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ManualfindDao;

@Repository("manualfindDao")
public class ManualfindDaoImpl extends BaseDao implements ManualfindDao {

	@Override
	public int queryManualfindCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("manualfind.queryManualfindCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryManualfindList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("manualfind.queryManualfindList",paramMap);
	}

}
