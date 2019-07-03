package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TuniuStationDao;
import com.l9e.transaction.service.TuniuStationService;


@Service("tuniuStationService")
public class TuniuStationServiceImpl implements TuniuStationService{
	@Resource
	private TuniuStationDao tuniuStationDao ;

	@Override
	public int queryTuniuStationCounts(Map<String, Object> paramMap) {
		return tuniuStationDao.queryTuniuStationCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryTuniuStationInfoExcel(
			Map<String, Object> paramMap) {
		return tuniuStationDao.queryTuniuStationInfoExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryTuniuStationList(
			Map<String, Object> paramMap) {
		return tuniuStationDao.queryTuniuStationList(paramMap);
	}

	@Override
	public int queryTuniuStationInfoCounts(Map<String, Object> paramMap) {
		return tuniuStationDao.queryTuniuStationInfoCounts(paramMap);
	}

	
	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return tuniuStationDao.queryCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryTuniuStationInfoList(
			Map<String, Object> paramMap) {
		return tuniuStationDao.queryTuniuStationInfoList(paramMap);
	}

	@Override
	public void addTuniuStation(Map<String, Object> paramMap) {
		 tuniuStationDao.addTuniuStation(paramMap);
	}


	@Override
	public void addTuniuStationTj(Map<String, Object> paramMap) {
		 tuniuStationDao.addTuniuStationTj(paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return tuniuStationDao.queryRefundFinishCount(orderId);
	}


	
}
