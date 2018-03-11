package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface CtripAccountDao {
	
	int queryCtripAccountListCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryCtripAccountList(Map<String, Object> paramMap);

	Map<String, String> queryCtripAccount(String ctripId);

	void insertCtripAccount(Map<String, String> paramMap);

	void updateCtripAccount(Map<String, String> paramMap);

	void insertCtriplogs(Map<String, String> logs);

	String queryCtrip_name(String ctripName);
	
	int queryCtripAccountLogCount();

	List<Map<String, String>> queryCtripAccountLogList(
			Map<String, Object> paramMap);
	List<Map<String, String>> queryAmountArea();
	int updateAmountArea(Map<String,Object> param);
}