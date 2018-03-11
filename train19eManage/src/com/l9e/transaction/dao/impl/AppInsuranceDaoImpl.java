package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AppInsuranceDao;
@Repository("appInsuranceDao")
public class AppInsuranceDaoImpl extends BaseDao implements AppInsuranceDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("appInsurance.queryInsuranceList",query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return getTotalRows("appInsurance.queryInsuranceListCount",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceInfo(Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("appInsurance.queryInsuranceInfo",query_Map);
	}

	public void updateInsuranceStatusSendAgain(Map<String, Object> update_Map) {
		this.getSqlMapClientTemplate().update("appInsurance.updateInsuranceStatusSendAgain", update_Map);
	}
	
	public void updateInsuranceStatusNeedCancel(Map<String, Object> update_Map) {
		this.getSqlMapClientTemplate().update("appInsurance.updateInsuranceStatusNeedCancel", update_Map);
	}

	public void addLog(Map<String, Object> log_Map) {
		this.getSqlMapClientTemplate().insert("appInsurance.addLog", log_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLog(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("appInsurance.queryLog", order_id);
	}
}
