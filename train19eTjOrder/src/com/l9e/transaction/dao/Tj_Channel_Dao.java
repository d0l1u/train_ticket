package com.l9e.transaction.dao;

import java.util.Map;

public interface Tj_Channel_Dao {
	void addToTj_Channel(Map<String, Object> map);
	
	void updateToTj_Channel(Map<String, Object> map);
	
	int query19eTodayCount(Map<String, Object> map);
	
	int search_count(Map<String, Object> paramMap);
	int search_success(Map<String, Object> paramMap);
	int search_fail(Map<String, Object> paramMap);
	int msg_count(Map<String, Object> paramMap);
	int alter_success(Map<String, Object> paramMap);
	int alter_fail(Map<String, Object> paramMap);
	int refund_success(Map<String, Object> paramMap);
	int refund_fail(Map<String, Object> paramMap);
}
