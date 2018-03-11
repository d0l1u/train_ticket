package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ChanneltjDao;

@Repository("channeltjDao")
public class ChanneltjDaoImpl extends BaseDao implements ChanneltjDao{

	@Override
	public int queryChanneltjCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("channeltj.queryChanneltjCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryChanneltjExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("channeltj.queryChanneltjExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryChanneltjList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("channeltj.queryChanneltjList",paramMap);
	}


}
