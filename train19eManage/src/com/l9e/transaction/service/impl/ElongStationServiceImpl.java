package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ElongStationDao;
import com.l9e.transaction.service.ElongStationService;


@Service("elongStationService")
public class ElongStationServiceImpl implements ElongStationService{
	@Resource
	private ElongStationDao elongStationDao ;

	@Override
	public int queryElongStationCounts(Map<String, Object> paramMap) {
		return elongStationDao.queryElongStationCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryElongStationInfoExcel(
			Map<String, Object> paramMap) {
		return elongStationDao.queryElongStationInfoExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryElongStationList(
			Map<String, Object> paramMap) {
		return elongStationDao.queryElongStationList(paramMap);
	}

	@Override
	public int queryElongStationInfoCounts(Map<String, Object> paramMap) {
		return elongStationDao.queryElongStationInfoCounts(paramMap);
	}

	
	@Override
	public int queryCounts(Map<String, Object> paramMap) {
		return elongStationDao.queryCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryElongStationInfoList(
			Map<String, Object> paramMap) {
		return elongStationDao.queryElongStationInfoList(paramMap);
	}

	@Override
	public void addElongStation(Map<String, Object> paramMap) {
		 elongStationDao.addElongStation(paramMap);
	}


	@Override
	public void addElongStationTj(Map<String, Object> paramMap) {
		 elongStationDao.addElongStationTj(paramMap);
	}

	@Override
	public int queryRefundFinishCount(String orderId) {
		return elongStationDao.queryRefundFinishCount(orderId);
	}


	
}
