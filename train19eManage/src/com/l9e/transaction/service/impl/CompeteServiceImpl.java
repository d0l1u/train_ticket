package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CompeteDao;
import com.l9e.transaction.service.CompeteService;

@Service("competeService")
public class CompeteServiceImpl implements CompeteService{
	@Resource
	private CompeteDao competeDao ;

	@Override
	public void addCompete(Map<String, Object> paramMap) {
		competeDao.addCompete(paramMap);
	}
	
	@Override
	public void addhistory(Map<String, Object> paramMap) {
		competeDao.addhistory(paramMap);
	}

	@Override
	public int queryCompeteCounts(Map<String, Object> paramMap) {
		return competeDao.queryCompeteCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCompeteExcel(
			Map<String, Object> paramMap) {
		return competeDao.queryCompeteExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCompeteInfo(
			Map<String, Object> paramMap) {
		return competeDao.queryCompeteInfo(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCompeteList(
			Map<String, Object> paramMap) {
		return competeDao.queryCompeteList(paramMap);
	}

	@Override
	public void updateCompete(Map<String, Object> paramMap) {
		competeDao.updateCompete(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryCompeteListJob() {
		return competeDao.queryCompeteListJob();
	}

	@Override
	public int qunar_ticket_count(Map<String, Object> paramMap) {
		return competeDao.qunar_ticket_count(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCompeteHistory(Map<String, Object> paramMap) {
		return competeDao.queryCompeteHistory(paramMap);
	}

	@Override
	public List<Map<String, String>> querynow_compete(
			Map<String, Object> paramMap) {
		return competeDao.querynow_compete(paramMap);
	}

	@Override
	public int queryCompeteHistoryCounts() {
		return competeDao.queryCompeteHistoryCounts();
	}


}
