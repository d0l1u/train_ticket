package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_UserInfoDao;
import com.l9e.transaction.service.Tj_UserInfoService;

@Service("tj_UserInfoService")
public class Tj_UserInfoServiceImpl implements Tj_UserInfoService{
	@Resource
	private Tj_UserInfoDao tj_UserInfoDao;

	public int queryTable_count() {
		return tj_UserInfoDao.queryTable_count();
	}

	public List<Map<String, String>> queryAllArea_nameAndArea_no() {
		return tj_UserInfoDao.queryAllArea_nameAndArea_no();
	}

	public List<Map<String,String>> queryAll_date() {
		return tj_UserInfoDao.queryAll_date();
	}

	public int queryUser_total(Map<String, String> query_Map) {
		return tj_UserInfoDao.queryUser_total(query_Map);
	}

	public int queryActiveUserNow(Map<String, String> query_Map) {
		return tj_UserInfoDao.queryActiveUserNow(query_Map);
	}

	public int queryUser_total_Add(Map<String, String> query_Map) {
		return tj_UserInfoDao.queryUser_total_Add(query_Map);
	}

	public int queryActiveUserAll(Map<String, String> query_Map) {
		return tj_UserInfoDao.queryActiveUserAll(query_Map);
	}

	public int queryActiveUser_Add(Map<String, String> query_Map) {
		return tj_UserInfoDao.queryActiveUser_Add(query_Map);
	}

	public void addTj_User(Map<String, Object> add_Map) {
		tj_UserInfoDao.addTj_User(add_Map);
	}
}
