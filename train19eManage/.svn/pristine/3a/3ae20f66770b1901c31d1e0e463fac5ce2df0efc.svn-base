package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OpterStatDao;
import com.l9e.transaction.service.OpterStatService;
@Service("opterStatService")
public class OpterStatServiceImpl implements OpterStatService{
	@Resource
	private OpterStatDao opterStatDao;

	public int queryOpterStatCount(Map<String, Object> query_Map) {
		return opterStatDao.queryOpterStatCount(query_Map);
	}

	public List<Map<String, Object>> queryOpterStatList(
			Map<String, Object> query_Map) {
		return opterStatDao.queryOpterStatList(query_Map);
	}
	 
	public HashMap<String, Object> queryOpterInfo(String tj_id) { 
		return opterStatDao.queryOpterInfo(tj_id);
	}
}
