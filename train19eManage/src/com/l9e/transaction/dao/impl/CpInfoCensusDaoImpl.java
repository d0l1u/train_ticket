package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CpInfoCensusDao;
@Repository("cpInfoCensusDao")
public class CpInfoCensusDaoImpl extends BaseDao implements CpInfoCensusDao{

	public int queryPre_day_order_succeed(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("cpInfoCensus.queryPre_day_order_succeed",query_Map);
	}

	public int queryPre_day_order_defeated(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("cpInfoCensus.queryPre_day_order_defeated",query_Map);
	}

	public double queryPre_succeed_money(Map<String, String> query_Map) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("cpInfoCensus.queryPre_succeed_money",query_Map);
	}

	public double queryPre_defeated_money(Map<String, String> query_Map) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("cpInfoCensus.queryPre_defeated_money",query_Map);
	}

	public int queryPre_ticket_count(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("cpInfoCensus.queryPre_ticket_count",query_Map);
	}

	public void addCensusTocp_statInfo_19e(Map<String, Object> add_Map_19e) {
		this.getSqlMapClientTemplate().insert("cpInfoCensus.addCensusTocp_statInfo_19e",add_Map_19e);
	}

	public void addCensusTocp_statInfo_qunar(Map<String, Object> add_Map_qunar) {
		this.getSqlMapClientTemplate().insert("cpInfoCensus.addCensusTocp_statInfo_qunar",add_Map_qunar);
	}

	public int query_table_count() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("cpInfoCensus.query_table_count");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryDateList() {
		return this.getSqlMapClientTemplate().queryForList("cpInfoCensus.queryDateList");
	}

}
