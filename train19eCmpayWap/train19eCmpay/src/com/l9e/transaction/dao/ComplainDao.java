package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface ComplainDao {

	List<Map<String, String>> queryComplainList(String agentId);

	void addComplainInfo(Map<String, String> paramMap);

	int queryDailyCount(String agentId);

}
