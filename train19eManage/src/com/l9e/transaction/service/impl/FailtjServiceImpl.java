package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.FailtjDao;
import com.l9e.transaction.service.FailtjService;


@Service("failtjService")
public class FailtjServiceImpl implements FailtjService{
	@Resource
	private FailtjDao failtjDao ;

	@Override
	public int queryFailtjCounts(Map<String, Object> paramMap) {
		return failtjDao.queryFailtjCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryFailtjExcel(
			Map<String, Object> paramMap) {
		return failtjDao.queryFailtjExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryFailtjList(
			Map<String, Object> paramMap) {
		return failtjDao.queryFailtjList(paramMap);
	}

	
}
