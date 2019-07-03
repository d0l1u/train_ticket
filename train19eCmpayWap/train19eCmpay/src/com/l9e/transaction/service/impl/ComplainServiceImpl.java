package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ComplainDao;
import com.l9e.transaction.service.ComplainService;

@Service("complainService")
public class ComplainServiceImpl implements ComplainService {
	
	@Resource
	private ComplainDao complainDao;

	public List<Map<String, String>> queryComplainList(String cm_phone) {
		return complainDao.queryComplainList(cm_phone);
	}

	public void addComplainInfo(Map<String, String> paramMap) {
		complainDao.addComplainInfo(paramMap);
	}

	public int queryDailyCount(String agentId) {
		return complainDao.queryDailyCount(agentId);
	}
}
