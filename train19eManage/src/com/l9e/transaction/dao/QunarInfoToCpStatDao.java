package com.l9e.transaction.dao;

import java.util.Map;

public interface QunarInfoToCpStatDao {

	int queryPre_day_order_count();

	int queryPre_day_out_ticket_defeated();

	int queryPre_day_out_ticket_succeed();

	void addOrderInfoToCpStat(Map<String, Object> map);

	int queryPre_refund_count();

	int queryPre_ticket_count();

	int query_Qunar_StatInfo();

	double queryPre_succeed_money();

	// 前一天失败的总价钱数
	double queryPre_defeated_money();

	// 统计下单的个数
	int queryPre_preparative_count();

}
