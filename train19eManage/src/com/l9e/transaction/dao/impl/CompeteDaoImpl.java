package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CompeteDao;
@Repository("competeDao")
public class CompeteDaoImpl extends BaseDao implements CompeteDao{

	@Override
	public void addCompete(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("compete.addCompete",paramMap);
	}
	@Override
	public int queryCompeteCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("compete.queryCompeteCounts",paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCompeteExcel(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("compete.queryCompeteExcel",paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCompeteInfo(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("compete.queryCompeteInfo",paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCompeteList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("compete.queryCompeteList",paramMap);
	}
	@Override
	public void updateCompete(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("compete.updateCompete", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryCompeteListJob() {
		return this.getSqlMapClientTemplate().queryForList("compete.queryCompeteListJob");
	}
	@Override
	public int qunar_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("compete.qunar_ticket_count",paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCompeteHistory(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("compete.queryCompeteHistory",paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> querynow_compete(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("compete.querynow_compete",paramMap);
	}
	@Override
	public void addhistory(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("compete.addhistory",paramMap);
	}
	
	@Override
	public int queryCompeteHistoryCounts() {
	 return (Integer)this.getSqlMapClientTemplate().queryForObject("compete.queryCompeteHistoryCounts");
	}


}
