package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface ManualfindDao {
	int queryManualfindCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryManualfindList(Map<String, Object> paramMap);

}
