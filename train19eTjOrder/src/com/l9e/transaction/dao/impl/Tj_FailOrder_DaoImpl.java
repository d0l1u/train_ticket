package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_FailOrder_Dao;

@Repository("tj_FailOrder_Dao")
public class Tj_FailOrder_DaoImpl extends BaseDao implements Tj_FailOrder_Dao{

	public void addToTj_FailOrder(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_FailOrder.addToTj_FailOrder",map);
	}
	public void updateToTj_FailOrder(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("tj_FailOrder.updateToTj_FailOrder", map);
	}
	public int queryTodayCount(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_FailOrder.queryTodayCount", map);
	}
	
	public int queryFailOrderCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_FailOrder.queryFailOrderCount", paramMap);
	}

	
	
}
