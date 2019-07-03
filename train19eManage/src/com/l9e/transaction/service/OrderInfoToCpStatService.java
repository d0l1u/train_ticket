package com.l9e.transaction.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public interface OrderInfoToCpStatService {

	int queryPre_day_out_ticket_succeed();

	int queryPre_day_out_ticket_defeated();

	int queryPre_day_order_count();
	
	int queryPre_bx_count();

	int queryPre_preparative_count();
	
	int queryPre_refund_count();

	double queryPre_bx_countMoney();

	double queryPre_succeed_money();

	double queryPre_defeated_money();

	void addOrderInfoToCpStat(Map<String, Object> map);

	int queryPre_ticket_count();

	int queryPre_pay_defeated();
	
	
	
	//////////////////////////////////////////////
	int query_Hc_StatInfo();

	int queryPre_activeAgent();




	

	

}
