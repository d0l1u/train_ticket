package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OldOrderDao;

@Repository("oldOrderDao")
public class OldOrderDaoImpl extends BaseDao implements OldOrderDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOldOrderList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("oldOrder.queryOldOrderList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOldOrderListCp(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("oldOrder.queryOldOrderListCp", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOldOrderExcel(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("oldOrder.queryOldOrderExcel", paramMap);
	}
	
	@Override
	public int queryOldOrderCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("oldOrder.queryOldOrderCount", paramMap);
	}
	@Override
	public int queryOldOrderCountCp(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("oldOrder.queryOldOrderCountCp", paramMap);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryOldOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("oldOrder.queryOldOrderInfo", order_id);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryOldOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("oldOrder.queryOldOrderInfoCp", order_id);
	}
}
