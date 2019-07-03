package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface Tj_OpterService {

	int queryTableTotal();

	List<String> queryAllDate(String table_name);

	List<String> queryAllOpter();

	int queryRefund_total(Map<String, Object> query_Map);

	List<Map<String, String>> queryOrder_List(Map<String, Object> query_Map);

	int queryRefund_totalQunar(Map<String, Object> query_Map);

	void addStatToTj_Opter(Map<String, Object> add_Map);

	int queryRefund_totalExt(Map<String, Object> queryMap);

	int queryRefund_totalApp(Map<String, Object> queryMap);
	
	int queryRefund_totalInner(Map<String, Object> queryMap);

	int queryRefund_totalElong(Map<String, Object> queryMap);
	
	int queryRefund_totaltongcheng(Map<String, Object> queryMap);

	int queryRefundnew(Map<String, Object> queryMap);

	List<Map<String, String>> queryExceptionConfig();

	int queryExceptionNum(Map<String, Object> paramMap);

	int queryTjExceptionCount(Map<String, Object> paramMap);

	void addToTjException(Map<String, Object> paramMap);

	void updateToTjException(Map<String, Object> paramMap);

	List<Map<String, Object>> queryMatchList(Map<String, Object> paramMap);

	Map<String, Object> queryStatusMatch(Map<String, Object> paramMap);

	void insertTjMatch(Map<String, Object> addMap);

}
