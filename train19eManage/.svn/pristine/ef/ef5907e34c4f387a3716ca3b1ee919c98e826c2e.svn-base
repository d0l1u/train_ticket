package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CtripAccountDao;
import com.l9e.transaction.service.CtripAccountService;

@Service("ctripAccountService")
public class CtripAccountServiceImpl implements CtripAccountService{

	@Resource
	private CtripAccountDao ctripAccountDao;
	
	
	public void insertCtripAccount(Map<String, String> paramMap) {
		ctripAccountDao.insertCtripAccount(paramMap);
	}

	
	public Map<String, String> queryCtripAccount(String ctripId) {
		return ctripAccountDao.queryCtripAccount(ctripId);
	}

	
	public List<Map<String, String>> queryCtripAccountList(
			Map<String, Object> paramMap) {
		return ctripAccountDao.queryCtripAccountList(paramMap);
	}

	
	public int queryCtripAccountListCount(Map<String, Object> paramMap) {
		return ctripAccountDao.queryCtripAccountListCount(paramMap);
	}

	
	public void updateCtripAccount(Map<String, String> paramMap) {
		ctripAccountDao.updateCtripAccount(paramMap);
	}

	
	public void insertCtriplogs(Map<String, String> logs) {
		ctripAccountDao.insertCtriplogs(logs);
	}

	
	public String queryCtrip_name(String ctripName) {
		return ctripAccountDao.queryCtrip_name(ctripName);
	}


	@Override
	public int queryCtripAccountLogCount() {
		return ctripAccountDao.queryCtripAccountLogCount();
	}


	@Override
	public List<Map<String, String>> queryCtripAccountLogList(
			Map<String, Object> paramMap) {
		return ctripAccountDao.queryCtripAccountLogList(paramMap);
	}


	@Override
	public List<Map<String, String>> queryAmountArea() {
		return ctripAccountDao.queryAmountArea();
	}


	@Override
	public int updateAmountArea(Map<String, Object> param) {
		return ctripAccountDao.updateAmountArea(param);
	}

}
