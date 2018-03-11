package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface CpInfoCensusDao {

	int queryPre_day_order_succeed(Map<String, String> query_Map);

	int queryPre_day_order_defeated(Map<String, String> query_Map);

	double queryPre_succeed_money(Map<String, String> query_Map);

	double queryPre_defeated_money(Map<String, String> query_Map);

	int queryPre_ticket_count(Map<String, String> query_Map);

	void addCensusTocp_statInfo_19e(Map<String, Object> add_Map_19e);

	void addCensusTocp_statInfo_qunar(Map<String, Object> add_Map_qunar);

	int query_table_count();

	List<Map<String, String>> queryDateList();

}
