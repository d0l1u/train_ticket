package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AllInsuranceDao;
@Repository("allInsuranceDao")
public class AllInsuranceDaoImpl extends BaseDao implements AllInsuranceDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("allInsurance.queryInsuranceList",query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return getTotalRows("allInsurance.queryInsuranceListCount",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInsuranceInfo(Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("allInsurance.queryInsuranceInfo",query_Map);
	}

	public int updateInsuranceStatusSendAgain(Map<String, Object> update_Map) {
		return (Integer)this.getSqlMapClientTemplate().update("allInsurance.updateInsuranceStatusSendAgain", update_Map);
	}
	
	public int updateInsuranceStatusNeedCancel(Map<String, Object> update_Map) {
		return (Integer)this.getSqlMapClientTemplate().update("allInsurance.updateInsuranceStatusNeedCancel", update_Map);
	}

	public void addLog(Map<String, Object> log_Map) {
		this.getSqlMapClientTemplate().insert("allInsurance.addLog", log_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLog(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("allInsurance.queryLog", order_id);
	}

	// 查询预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllBookOrderInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("allInsurance.queryAllBookOrderInfo", order_id);
	}

	@Override
	public void plUpdateAgain() {
		this.getSqlMapClientTemplate().update("allInsurance.plUpdateAgain");
	}
}
