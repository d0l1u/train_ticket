package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface OldOrderDao {
	List<Map<String, String>> queryOldOrderList(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryOldOrderListCp(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryOldOrderExcel(Map<String, Object> paramMap);
	
	int queryOldOrderCount(Map<String, Object> paramMap);
	
	int queryOldOrderCountCp(Map<String, Object> paramMap);

	Map<String, String> queryOldOrderInfo(String order_id);

	List<Map<String, Object>> queryOldOrderInfoCp(String order_id);

}
