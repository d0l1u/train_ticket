package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RefundStationDao;
import com.l9e.transaction.service.RefundStationService;


@Service("refundStationService")
public class RefundStationServiceImpl implements RefundStationService{
	@Resource
	private RefundStationDao RefundStationDao ;

	@Override
	public int queryRefundStationCounts(Map<String, Object> paramMap) {
		return RefundStationDao.queryRefundStationCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryRefundStationInfoExcel(
			Map<String, Object> paramMap) {
		return RefundStationDao.queryRefundStationInfoExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryRefundStationList(
			Map<String, Object> paramMap) {
		return RefundStationDao.queryRefundStationList(paramMap);
	}

	@Override
	public int queryRefundStationInfoCounts(Map<String, Object> paramMap) {
		return RefundStationDao.queryRefundStationInfoCounts(paramMap);
	}

	
	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return RefundStationDao.queryCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryRefundStationInfoList(
			Map<String, Object> paramMap) {
		return RefundStationDao.queryRefundStationInfoList(paramMap);
	}

	@Override
	public void addRefundStation(Map<String, Object> paramMap) {
		 RefundStationDao.addRefundStation(paramMap);
	}


	@Override
	public void addRefundStationTj(Map<String, Object> paramMap) {
		 RefundStationDao.addRefundStationTj(paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return RefundStationDao.queryRefundFinishCount(orderId);
	}


	
}
