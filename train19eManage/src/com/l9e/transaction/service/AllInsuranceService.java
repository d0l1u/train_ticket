package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AllInsuranceService {
	int queryInsuranceListCount(Map<String, Object> query_Map);
	
	List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map);
	
	List<Map<String, Object>> queryInsuranceInfo(Map<String, Object> query_Map);
	
	List<Map<String, Object>> queryLog(String order_id);
	void addLog(Map<String, Object> log_Map);
	
	void updateInsuranceStatusSendAgain(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> update_Map);
	
	void updateInsuranceStatusNeedCancel(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> update_Map);

	// 查询预订订单信息
	public Map<String, String> queryAllBookOrderInfo(String order_id);

	void plUpdateAgain();
	
}
