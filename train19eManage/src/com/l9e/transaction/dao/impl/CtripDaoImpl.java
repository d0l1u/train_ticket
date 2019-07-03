package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CtripDao;

@Repository("ctripDao")
public class CtripDaoImpl extends BaseDao implements CtripDao{

	@Override
	public void insertCtriplogs(Map<String, String> logs) {
		this.getSqlMapClientTemplate().insert("ctrip.insertCtriplogs",logs);
	}

	@Override
	public int queryCtripCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("ctrip.queryCtripCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCtripList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("ctrip.queryCtripList",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryCtripOrderInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("ctrip.queryCtripOrderInfo",orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryCtripOrderInfoCp(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("ctrip.queryCtripOrderInfoCp",orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryHistroyByOrderId(String orderId) {
		String sql = (String) this.getSqlMapClientTemplate().queryForObject("acquire2.querysqlSetting");
		List<Map<String, Object>> list=null;
		if("2".equals(sql)){
			list = this.getSqlMapClient2().queryForList("acquire2.queryOrderInfoHistroy", orderId);
		}else if("1".equals(sql)){
			list = this.getSqlMapClientTemplate().queryForList("acquire2.queryOrderInfoHistroy", orderId);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> queryCtripAccount(Map<String, String> map) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("ctrip.queryCtripAccount",map);
	}

}
