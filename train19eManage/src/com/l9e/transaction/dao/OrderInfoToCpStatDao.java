package com.l9e.transaction.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public interface OrderInfoToCpStatDao {

	int queryPre_day_order_count();

	int queryPre_day_out_ticket_defeated();

	int queryPre_day_out_ticket_succeed();

	int queryPre_bx_count();
	
	double queryPre_bx_countMoney();
	
	double queryPre_defeated_money();

	double queryPre_succeed_money();

	void addOrderInfoToCpStat(Map<String, Object> map);

	int queryPre_preparative_count();

	int queryPre_refund_count();

	int queryPre_ticket_count();
	
	int queryPre_pay_defeated();
	
	//////////////////

	int query_Hc_StatInfo();

	int queryPre_activeAgent();




	
}
