package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CtripDao;
import com.l9e.transaction.service.CtripService;

@Service("ctripService")
public class CtripServiceImpl implements CtripService{
	@Resource
	private CtripDao ctripDao;
	
	public void insertCtriplogs(Map<String, String> logs) {
		ctripDao.insertCtriplogs(logs);
	}

	
	public int queryCtripCount(Map<String, Object> paramMap) {
		return ctripDao.queryCtripCount(paramMap);
	}

	
	public List<Map<String, String>> queryCtripList(Map<String, Object> paramMap) {
		return ctripDao.queryCtripList(paramMap);
	}

	
	public Map<String, String> queryCtripOrderInfo(String orderId) {
		return ctripDao.queryCtripOrderInfo(orderId);
	}

	
	public List<Map<String, Object>> queryCtripOrderInfoCp(String orderId) {
		return ctripDao.queryCtripOrderInfoCp(orderId);
	}

	
	public List<Map<String, Object>> queryHistroyByOrderId(String orderId) {
		return ctripDao.queryHistroyByOrderId(orderId);
	}


	@Override
	public HashMap<String, String> queryCtripAccount(Map<String, String> map) {
		return ctripDao.queryCtripAccount(map);
	}

}
