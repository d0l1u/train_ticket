package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.GtStationDao;
import com.l9e.transaction.service.GtStationService;


@Service("gtStationService")
public class GtStationServiceImpl implements GtStationService{
	@Resource
	private GtStationDao gtStationDao ;

	@Override
	public int queryGtStationCounts(Map<String, Object> paramMap) {
		return gtStationDao.queryGtStationCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryGtStationInfoExcel(
			Map<String, Object> paramMap) {
		return gtStationDao.queryGtStationInfoExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryGtStationList(
			Map<String, Object> paramMap) {
		return gtStationDao.queryGtStationList(paramMap);
	}

	@Override
	public int queryGtStationInfoCounts(Map<String, Object> paramMap) {
		return gtStationDao.queryGtStationInfoCounts(paramMap);
	}

	
	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return gtStationDao.queryCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryGtStationInfoList(
			Map<String, Object> paramMap) {
		return gtStationDao.queryGtStationInfoList(paramMap);
	}

	@Override
	public void addGtStation(Map<String, Object> paramMap) {
		 gtStationDao.addGtStation(paramMap);
	}


	@Override
	public void addGtStationTj(Map<String, Object> paramMap) {
		 gtStationDao.addGtStationTj(paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return gtStationDao.queryRefundFinishCount(orderId);
	}


	
}
