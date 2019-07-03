package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface Tj_OpterDao {

	List<String> queryAllOpter();
	
	int queryCountTodayByName(Map<String,Object> map);
	
	void addNumTodayByName(Map<String,Object> map);
	
	void updateNumTodayByName(Map<String,Object> map);

}
