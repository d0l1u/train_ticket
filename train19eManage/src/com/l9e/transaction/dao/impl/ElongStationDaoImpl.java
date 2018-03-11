package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongStationDao;

@Repository("elongStationDao")
public class ElongStationDaoImpl extends BaseDao implements ElongStationDao{

	@Override
	public int queryElongStationCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("elongStation.queryElongStationCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryElongStationInfoExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("elongStation.queryElongStationInfoExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryElongStationList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("elongStation.queryElongStationList",paramMap);
	}

	@Override
	public int queryElongStationInfoCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("elongStation.queryElongStationInfoCounts",paramMap);
	}

	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("elongStation.queryCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryElongStationInfoList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("elongStation.queryElongStationInfoList",paramMap);
	}

	@Override
	public void addElongStation(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("elongStation.addElongStation", paramMap);
	}

	@Override
	public void addElongStationTj(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("elongStation.addElongStationTj", paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("elongStation.queryRefundFinishCount",orderId);
	}


}
