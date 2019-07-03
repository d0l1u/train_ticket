package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RefundStationDao;

@Repository("refundStationDao")
public class RefundStationDaoImpl extends BaseDao implements RefundStationDao{

	@Override
	public int queryRefundStationCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("refundStation.queryRefundStationCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundStationInfoExcel(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("refundStation.queryRefundStationInfoExcel",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundStationList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("refundStation.queryRefundStationList",paramMap);
	}

	@Override
	public int queryRefundStationInfoCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("refundStation.queryRefundStationInfoCounts",paramMap);
	}

	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("refundStation.queryCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundStationInfoList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("refundStation.queryRefundStationInfoList",paramMap);
	}

	@Override
	public void addRefundStation(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("refundStation.addRefundStation", paramMap);
	}

	@Override
	public void addRefundStationTj(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("refundStation.addRefundStationTj", paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("refundStation.queryRefundFinishCount",orderId);
	}


}
