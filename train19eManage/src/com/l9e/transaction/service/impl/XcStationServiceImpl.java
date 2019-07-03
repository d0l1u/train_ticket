package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.XcStationDao;
import com.l9e.transaction.service.XcStationService;


@Service("xcStationService")
public class XcStationServiceImpl implements XcStationService{
	@Resource
	private XcStationDao xcStationDao ;

	@Override
	public int queryXcStationCounts(Map<String, Object> paramMap) {
		return xcStationDao.queryXcStationCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryXcStationInfoExcel(
			Map<String, Object> paramMap) {
		return xcStationDao.queryXcStationInfoExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryXcStationList(
			Map<String, Object> paramMap) {
		return xcStationDao.queryXcStationList(paramMap);
	}

	@Override
	public int queryXcStationInfoCounts(Map<String, Object> paramMap) {
		return xcStationDao.queryXcStationInfoCounts(paramMap);
	}

	
	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return xcStationDao.queryCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryXcStationInfoList(
			Map<String, Object> paramMap) {
		return xcStationDao.queryXcStationInfoList(paramMap);
	}

	@Override
	public void addXcStation(Map<String, Object> paramMap) {
		 xcStationDao.addXcStation(paramMap);
	}


	@Override
	public void addXcStationTj(Map<String, Object> paramMap) {
		 xcStationDao.addXcStationTj(paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return xcStationDao.queryRefundFinishCount(orderId);
	}


	
}
