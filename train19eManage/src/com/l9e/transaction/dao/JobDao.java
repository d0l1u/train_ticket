package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface JobDao {

	List<Map<String, String>> queryScanedOrderList();

	List<Map<String, String>> queryCpInfoList(String order_id);

	int updateScanInfoById(Map<String, String> map);

}
