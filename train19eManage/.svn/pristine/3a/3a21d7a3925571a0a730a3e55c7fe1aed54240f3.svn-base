package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.XcStationDao;

@Repository("xcStationDao")
public class XcStationDaoImpl extends BaseDao implements XcStationDao{

	@Override
	public int queryXcStationCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("xcStation.queryXcStationCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcStationInfoExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("xcStation.queryXcStationInfoExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcStationList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("xcStation.queryXcStationList",paramMap);
	}

	@Override
	public int queryXcStationInfoCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("xcStation.queryXcStationInfoCounts",paramMap);
	}

	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("xcStation.queryCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcStationInfoList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("xcStation.queryXcStationInfoList",paramMap);
	}

	@Override
	public void addXcStation(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("xcStation.addXcStation", paramMap);
	}

	@Override
	public void addXcStationTj(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("xcStation.addXcStationTj", paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("xcStation.queryRefundFinishCount",orderId);
	}


}
