package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.FailtjDao;

@Repository("failtjDao")
public class FailtjDaoImpl extends BaseDao implements FailtjDao{

	@Override
	public int queryFailtjCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("failtj.queryFailtjCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryFailtjExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("failtj.queryFailtjExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryFailtjList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("failtj.queryFailtjList",paramMap);
	}


}
