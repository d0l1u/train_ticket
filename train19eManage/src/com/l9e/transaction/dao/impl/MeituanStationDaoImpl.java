package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.MeituanStationDao;

@Repository("meituanStationDao")
public class MeituanStationDaoImpl extends BaseDao implements MeituanStationDao{

	@Override
	public int queryMeituanStationCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("meituanStation.queryMeituanStationCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryMeituanStationInfoExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("meituanStation.queryMeituanStationInfoExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryMeituanStationList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("meituanStation.queryMeituanStationList",paramMap);
	}

	@Override
	public int queryMeituanStationInfoCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("meituanStation.queryMeituanStationInfoCounts",paramMap);
	}

	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("meituanStation.queryCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryMeituanStationInfoList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("meituanStation.queryMeituanStationInfoList",paramMap);
	}

	@Override
	public void addMeituanStation(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("meituanStation.addMeituanStation", paramMap);
	}

	@Override
	public void addMeituanStationTj(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("meituanStation.addMeituanStationTj", paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("meituanStation.queryRefundFinishCount",orderId);
	}


}
