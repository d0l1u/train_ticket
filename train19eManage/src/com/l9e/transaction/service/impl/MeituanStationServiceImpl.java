package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.MeituanStationDao;
import com.l9e.transaction.service.MeituanStationService;


@Service("meituanStationService")
public class MeituanStationServiceImpl implements MeituanStationService{
	@Resource
	private MeituanStationDao meituanStationDao ;

	@Override
	public int queryMeituanStationCounts(Map<String, Object> paramMap) {
		return meituanStationDao.queryMeituanStationCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryMeituanStationInfoExcel(
			Map<String, Object> paramMap) {
		return meituanStationDao.queryMeituanStationInfoExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryMeituanStationList(
			Map<String, Object> paramMap) {
		return meituanStationDao.queryMeituanStationList(paramMap);
	}

	@Override
	public int queryMeituanStationInfoCounts(Map<String, Object> paramMap) {
		return meituanStationDao.queryMeituanStationInfoCounts(paramMap);
	}

	
	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return meituanStationDao.queryCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryMeituanStationInfoList(
			Map<String, Object> paramMap) {
		return meituanStationDao.queryMeituanStationInfoList(paramMap);
	}

	@Override
	public void addMeituanStation(Map<String, Object> paramMap) {
		 meituanStationDao.addMeituanStation(paramMap);
	}


	@Override
	public void addMeituanStationTj(Map<String, Object> paramMap) {
		 meituanStationDao.addMeituanStationTj(paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return meituanStationDao.queryRefundFinishCount(orderId);
	}


	
}
