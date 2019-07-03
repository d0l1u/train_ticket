package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ChanneltjDao;
import com.l9e.transaction.service.ChanneltjService;


@Service("channeltjService")
public class ChanneltjServiceImpl implements ChanneltjService{
	@Resource
	private ChanneltjDao channeltjDao ;

	@Override
	public int queryChanneltjCounts(Map<String, Object> paramMap) {
		return channeltjDao.queryChanneltjCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryChanneltjExcel(
			Map<String, Object> paramMap) {
		return channeltjDao.queryChanneltjExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryChanneltjList(
			Map<String, Object> paramMap) {
		return channeltjDao.queryChanneltjList(paramMap);
	}

	
}
