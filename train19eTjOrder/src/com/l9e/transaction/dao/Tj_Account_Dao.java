package com.l9e.transaction.dao;

import java.util.Map;

public interface Tj_Account_Dao {
	void addToTj_Account(Map<String, Object> map);
	
	void updateToTj_Account(Map<String, Object> map);
	
	int query19eTodayCount(Map<String, Object> map);
	
	int regist_num(Map<String, Object> paramMap);
	
	int account_num(Map<String, Object> paramMap);
	
	int account_can_use(Map<String, Object> paramMap);

	String setting_value(String settingName);
}
