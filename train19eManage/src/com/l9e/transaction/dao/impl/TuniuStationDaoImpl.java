package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuStationDao;

@Repository("tuniuStationDao")
public class TuniuStationDaoImpl extends BaseDao implements TuniuStationDao{

	@Override
	public int queryTuniuStationCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tuniuStation.queryTuniuStationCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuStationInfoExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("tuniuStation.queryTuniuStationInfoExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuStationList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("tuniuStation.queryTuniuStationList",paramMap);
	}

	@Override
	public int queryTuniuStationInfoCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tuniuStation.queryTuniuStationInfoCounts",paramMap);
	}

	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tuniuStation.queryCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuStationInfoList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("tuniuStation.queryTuniuStationInfoList",paramMap);
	}

	@Override
	public void addTuniuStation(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("tuniuStation.addTuniuStation", paramMap);
	}

	@Override
	public void addTuniuStationTj(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("tuniuStation.addTuniuStationTj", paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tuniuStation.queryRefundFinishCount",orderId);
	}


}
