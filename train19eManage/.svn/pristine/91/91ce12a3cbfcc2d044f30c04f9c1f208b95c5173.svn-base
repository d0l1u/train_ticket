package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.InsuranceDao;
@Repository("insuranceDao")
public class InsuranceDaoImpl extends BaseDao implements InsuranceDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("insurance.queryInsuranceList",query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return getTotalRows("insurance.queryInsuranceListCount",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceInfo(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("insurance.queryInsuranceInfo",query_Map);
	}

	public void updateInsuranceStatusSendAgain(Map<String, Object> update_Map) {
		this.getSqlMapClientTemplate().update("insurance.updateInsuranceStatusSendAgain", update_Map);
	}
	
	public void updateInsuranceStatusNeedCancel(Map<String, Object> update_Map) {
		this.getSqlMapClientTemplate().update("insurance.updateInsuranceStatusNeedCancel", update_Map);
	}

	public void addLog(Map<String, Object> log_Map) {
		this.getSqlMapClientTemplate().insert("insurance.addLog", log_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLog(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("insurance.queryLog", order_id);
	}
}
