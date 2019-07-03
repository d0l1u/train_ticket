package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_Account_Dao;
import com.l9e.transaction.service.Tj_Account_Service;

@Service("tj_Account_Service")
public class Tj_Account_ServiceImpl implements Tj_Account_Service {
	@Resource
	private Tj_Account_Dao tj_Account_Dao;

	public int account_num(Map<String, Object> paramMap) {
		return tj_Account_Dao.account_num(paramMap);
	}

	public void addToTj_Account(Map<String, Object> map) {
		tj_Account_Dao.addToTj_Account(map);
	}

	public int query19eTodayCount(Map<String, Object> map) {
		return tj_Account_Dao.query19eTodayCount(map);
	}

	public int regist_num(Map<String, Object> paramMap) {
		return tj_Account_Dao.regist_num(paramMap);
	}

	public void updateToTj_Account(Map<String, Object> map) {
		tj_Account_Dao.updateToTj_Account(map);
	}

	public int account_can_use(Map<String, Object> paramMap) {
		return tj_Account_Dao.account_can_use(paramMap);
	}

	public String setting_value(String settingName) {
		return tj_Account_Dao.setting_value(settingName);
	}
	
	
}
