package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ManualfindDao;
import com.l9e.transaction.service.ManualfindService;

@Service("manualfindService")
public class ManualfindServiceImpl implements ManualfindService {
	@Resource
	private  ManualfindDao manualfindDao;

	@Override
	public int queryManualfindCount(Map<String, Object> paramMap) {
		return manualfindDao.queryManualfindCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryManualfindList(
			Map<String, Object> paramMap) {
		return manualfindDao.queryManualfindList(paramMap);
	}

}
