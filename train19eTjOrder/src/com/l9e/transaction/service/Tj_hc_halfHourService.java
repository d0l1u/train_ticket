package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface Tj_hc_halfHourService {
	List<Map<String, String>> queryDayTimeBefore(String createTime);

	List<Map<String, String>> queryDayTimeAfter(String createTime);
	
	void addToTj_hc_halfHour(Map<String, Object> map);
	
	int queryTable_Count();
	
	public List<String> queryDate_List();

	int queryCount(Map map);

	void updateTj_hc_halfHour(Map map);
	
}
