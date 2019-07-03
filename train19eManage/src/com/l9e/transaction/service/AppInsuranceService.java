package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface AppInsuranceService {
	
	int queryInsuranceListCount(Map<String, Object> query_Map);
	
	List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map);
	
	List<Map<String, Object>> queryInsuranceInfo(Map<String, Object> query_Map);
	
	List<Map<String, Object>> queryLog(String order_id);
	
	void updateInsuranceStatusSendAgain(Map<String, Object> update_Map);
	
	void addLog(Map<String, Object> log_Map);
	
	void updateInsuranceStatusNeedCancel(Map<String, Object> update_Map);


}
