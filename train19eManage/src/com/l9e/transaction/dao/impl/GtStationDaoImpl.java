package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.GtStationDao;

@Repository("gtStationDao")
public class GtStationDaoImpl extends BaseDao implements GtStationDao{

	@Override
	public int queryGtStationCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("gtStation.queryGtStationCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtStationInfoExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("gtStation.queryGtStationInfoExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtStationList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("gtStation.queryGtStationList",paramMap);
	}

	@Override
	public int queryGtStationInfoCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("gtStation.queryGtStationInfoCounts",paramMap);
	}

	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("gtStation.queryCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtStationInfoList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("gtStation.queryGtStationInfoList",paramMap);
	}

	@Override
	public void addGtStation(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("gtStation.addGtStation", paramMap);
	}

	@Override
	public void addGtStationTj(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("gtStation.addGtStationTj", paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("gtStation.queryRefundFinishCount",orderId);
	}


}
