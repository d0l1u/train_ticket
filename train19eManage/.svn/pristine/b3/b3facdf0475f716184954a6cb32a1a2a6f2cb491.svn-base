package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ExtInsuranceDao;
@Repository("extInsuranceDao")
public class ExtInsuranceDaoImpl extends BaseDao implements ExtInsuranceDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("extInsurance.queryInsuranceList",query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return getTotalRows("extInsurance.queryInsuranceListCount",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceInfo(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("extInsurance.queryInsuranceInfo",query_Map);
	}

	public void updateInsuranceStatusSendAgain(Map<String, Object> update_Map) {
		this.getSqlMapClientTemplate().update("extInsurance.updateInsuranceStatusSendAgain", update_Map);
	}
	
	public void updateInsuranceStatusNeedCancel(Map<String, Object> update_Map) {
		this.getSqlMapClientTemplate().update("extInsurance.updateInsuranceStatusNeedCancel", update_Map);
	}

	public void addLog(Map<String, Object> log_Map) {
		this.getSqlMapClientTemplate().insert("extInsurance.addLog", log_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLog(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("extInsurance.queryLog", order_id);
	}
}
