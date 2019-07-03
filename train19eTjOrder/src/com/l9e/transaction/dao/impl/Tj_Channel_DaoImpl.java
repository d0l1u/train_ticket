package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_Channel_Dao;

@Repository("tj_Channel_Dao")
public class Tj_Channel_DaoImpl extends BaseDao implements Tj_Channel_Dao{

	public void addToTj_Channel(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_Channel.addToTj_Channel",map);
	}
	public void updateToTj_Channel(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("tj_Channel.updateToTj_Channel", map);
	}
	public int query19eTodayCount(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.query19eTodayCount", map);
	}

	public int alter_fail(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.alter_fail", paramMap);
	}

	public int alter_success(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.alter_success", paramMap);
	}

	public int msg_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.msg_count", paramMap);
	}

	public int refund_fail(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.refund_fail", paramMap);
	}

	public int refund_success(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.refund_success", paramMap);
	}

	public int search_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.search_count", paramMap);
	}

	public int search_fail(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.search_fail", paramMap);
	}

	public int search_success(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Channel.search_success", paramMap);
	}

	
	
}
