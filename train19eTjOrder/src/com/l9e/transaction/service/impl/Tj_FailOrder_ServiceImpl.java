package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_FailOrder_Dao;
import com.l9e.transaction.service.Tj_FailOrder_Service;

@Service("tj_FailOrder_Service")
public class Tj_FailOrder_ServiceImpl implements Tj_FailOrder_Service {
	@Resource
	private Tj_FailOrder_Dao tj_FailOrder_Dao;
	
	public void addToTj_FailOrder(Map<String, Object> map) {
		tj_FailOrder_Dao.addToTj_FailOrder(map);
	}

	public int queryTodayCount(Map<String, Object> map) {
		return tj_FailOrder_Dao.queryTodayCount(map);
	}

	public void updateToTj_FailOrder(Map<String, Object> map) {
		tj_FailOrder_Dao.updateToTj_FailOrder(map);		
	}

	public int queryFailOrderCount(Map<String, Object> paramMap) {
		return tj_FailOrder_Dao.queryFailOrderCount(paramMap);	
	}

}
