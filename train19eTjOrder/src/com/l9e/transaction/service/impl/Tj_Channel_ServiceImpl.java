package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_Channel_Dao;
import com.l9e.transaction.service.Tj_Channel_Service;

@Service("tj_Channel_Service")
public class Tj_Channel_ServiceImpl implements Tj_Channel_Service {
	@Resource
	private Tj_Channel_Dao tj_Channel_Dao;
	
	public void addToTj_Channel(Map<String, Object> map) {
		tj_Channel_Dao.addToTj_Channel(map);
	}

	public int alter_fail(Map<String, Object> paramMap) {
		return tj_Channel_Dao.alter_fail(paramMap);
	}

	public int alter_success(Map<String, Object> paramMap) {
		return tj_Channel_Dao.alter_success(paramMap);
	}

	public int msg_count(Map<String, Object> paramMap) {
		return tj_Channel_Dao.msg_count(paramMap);
	}

	public int query19eTodayCount(Map<String, Object> map) {
		return tj_Channel_Dao.query19eTodayCount(map);
	}

	public int refund_fail(Map<String, Object> paramMap) {
		return tj_Channel_Dao.refund_fail(paramMap);
	}

	public int refund_success(Map<String, Object> paramMap) {
		return tj_Channel_Dao.refund_success(paramMap);
	}

	public int search_count(Map<String, Object> paramMap) {
		return tj_Channel_Dao.search_count(paramMap);
	}

	public int search_fail(Map<String, Object> paramMap) {
		return tj_Channel_Dao.search_fail(paramMap);
	}

	public int search_success(Map<String, Object> paramMap) {
		return tj_Channel_Dao.search_success(paramMap);
	}

	public void updateToTj_Channel(Map<String, Object> map) {
		tj_Channel_Dao.updateToTj_Channel(map);		
	}

}
