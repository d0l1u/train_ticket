package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface ComplainService {

	List<Map<String, String>> queryComplainList(String agentId);

	void addComplainInfo(Map<String, String> paramMap);

	int queryDailyCount(String agentId);

}
