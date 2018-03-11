package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface ManualfindService {

	int queryManualfindCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryManualfindList(Map<String, Object> paramMap);

}
