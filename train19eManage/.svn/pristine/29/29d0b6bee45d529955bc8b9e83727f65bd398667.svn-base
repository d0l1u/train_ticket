package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ElongAlterDao {
	
	int queryAlterTicketListCounts(HashMap<String, Object> paramMap);

	List<Map<String, String>> queryAlterTicketList(HashMap<String, Object> paramMap);

	List<HashMap<String, String>> queryLogById(String order_id);

	HashMap<String, String> queryAlterInfo(String changeId);

	List<HashMap<String, String>>  queryCpInfo(String changeId);

	void insertLog(HashMap<String, Object> map);

	void updateAlertCpInfo(HashMap<String, Object> paramMap);

	void updateAlterOrder(HashMap<String, Object> ordermap);

	void updateAlter(HashMap<String, Object> paramMap);
}
