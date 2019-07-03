package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CtripService {

	int queryCtripCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryCtripList(Map<String, Object> paramMap);

	Map<String, String> queryCtripOrderInfo(String orderId);

	List<Map<String, Object>> queryCtripOrderInfoCp(String orderId);

	List<Map<String, Object>> queryHistroyByOrderId(String orderId);

	void insertCtriplogs(Map<String, String> logs);

	HashMap<String, String> queryCtripAccount(Map<String, String> map);

}
