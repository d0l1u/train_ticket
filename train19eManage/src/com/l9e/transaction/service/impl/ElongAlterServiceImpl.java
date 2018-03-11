package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ElongAlterDao;
import com.l9e.transaction.service.ElongAlterService;

@Service("elongAlterService")
public class ElongAlterServiceImpl implements ElongAlterService{
	@Resource
	private	ElongAlterDao elongAlterDao;
	
	@Override
	public HashMap<String, String> queryAlterInfo(String changeId) {
		return elongAlterDao.queryAlterInfo(changeId);
	}

	@Override
	public List<Map<String, String>> queryAlterTicketList(
			HashMap<String, Object> paramMap) {
		return elongAlterDao.queryAlterTicketList(paramMap);
	}

	@Override
	public int queryAlterTicketListCounts(HashMap<String, Object> paramMap) {
		return elongAlterDao.queryAlterTicketListCounts(paramMap);
	}

	@Override
	public List<HashMap<String, String>> queryLogById(String order_id) {
		return elongAlterDao.queryLogById(order_id);
	}

	@Override
	public List<HashMap<String, String>>  queryCpInfo(String changeId) {
		return elongAlterDao.queryCpInfo(changeId);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		elongAlterDao.insertLog(map);
	}

	@Override
	public void updateAlertCpInfo(HashMap<String, Object> paramMap) {
		elongAlterDao.updateAlertCpInfo(paramMap);
	}

	@Override
	public void updateAlterOrder(HashMap<String, Object> ordermap) {
		elongAlterDao.updateAlterOrder(ordermap);
	}

	@Override
	public void updateAlter(HashMap<String, Object> paramMap) {
		elongAlterDao.updateAlter(paramMap);
	}

}
