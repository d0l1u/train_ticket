package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OpterStatService {

	int queryOpterStatCount(Map<String, Object> query_Map);

	List<Map<String, Object>> queryOpterStatList(Map<String, Object> query_Map);
	
	HashMap<String, Object> queryOpterInfo(String tj_id);
}
