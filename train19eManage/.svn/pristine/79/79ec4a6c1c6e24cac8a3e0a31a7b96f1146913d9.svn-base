package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AllInsuranceDao {

	List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map);

	int queryInsuranceListCount(Map<String, Object> query_Map);

	List<Map<String, Object>> queryInsuranceInfo(Map<String, Object> query_Map);


	void addLog(Map<String, Object> log_Map);

	List<Map<String, Object>> queryLog(String order_id);


	//查询预订订单信息
	public List<Map<String, String>> queryAllBookOrderInfo(String order_id);

	int updateInsuranceStatusSendAgain(Map<String, Object> update_Map);
	
	int updateInsuranceStatusNeedCancel(Map<String, Object> update_Map);

	void plUpdateAgain();
}
