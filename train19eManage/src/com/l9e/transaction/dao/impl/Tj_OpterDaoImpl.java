package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_OpterDao;
@Repository("tj_OpterDao")
public class Tj_OpterDaoImpl extends BaseDao implements Tj_OpterDao{

	@SuppressWarnings("unchecked")
	public List<String> queryAllOpter() {
		return this.getSqlMapClientTemplate().queryForList("tj_Opter.queryAllOpter");
	}

	@Override
	public void addNumTodayByName(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_Opter.addNumTodayByName",map);
	}

	@Override
	public int queryCountTodayByName(Map<String,Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryCountTodayByName",map);
	}

	@Override
	public void updateNumTodayByName(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("tj_Opter.updateNumTodayByName",map);
	}
	

	
}
