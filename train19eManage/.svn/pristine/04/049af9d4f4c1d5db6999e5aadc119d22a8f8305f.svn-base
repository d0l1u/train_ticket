package com.l9e.transaction.service;

import java.util.Date;
import java.util.List;
import java.util.Map;



public interface OrderInfoToCpStatOneService {

	int query_Hc_StatInfo();

	List<Map<String,Object>> queryDate();


	List<Map<String, Object>> createDateList(Map<String, Object> map);

	int queryPre_day_out_ticket_succeed(String date_time);

	int queryPre_day_out_ticket_defeated(String date_time);

	int queryPre_day_order_count(String date_time);

	int queryPre_preparative_count(String date_time);

	int queryPre_refund_count(String date_time);

	double queryPre_succeed_money(String date_time);

	double queryPre_defeated_money(String date_time);

	int queryPre_bx_count(String date_time);

	int queryPre_ticket_count(String date_time);

	double queryPre_bx_countMoney(String date_time);

	void addOrderInfoToCpStat(Map<String, Object> map);

	int queryPre_pay_defeated(String date_time);

	int queryPre_activeAgent(String date_time);

}
