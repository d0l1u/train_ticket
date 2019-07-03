package com.l9e.transaction.dao;

import java.util.Map;

public interface Tj_FailOrder_Dao {
	
	void addToTj_FailOrder(Map<String, Object> map);
	
	void updateToTj_FailOrder(Map<String, Object> map);
	
	int queryTodayCount(Map<String, Object> map);
	
	int queryFailOrderCount(Map<String, Object> paramMap);
}
