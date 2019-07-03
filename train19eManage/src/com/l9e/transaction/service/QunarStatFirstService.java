package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface QunarStatFirstService {
	// 查询总数
	int query_Hc_StatInfo();

	// 查询最大和最小时间
	List<Map<String, Object>> queryDate();

	// 查询时间
	List<Map<String, Object>> createDateList(Map<String, Object> map);

	// 插入统计完成的信息
	void addOrderInfoToCpStat(Map<String, Object> map);

	// 查询当天总条数
	int queryPre_day_order_count(String date_time);

	// 查询当天订单失败的条数
	int queryPre_day_out_ticket_defeated(String date_time);

	// 查询当天订单成功的条数
	int queryPre_day_out_ticket_succeed(String date_time);

	// 查询当天订单失败的总价钱
	double queryPre_defeated_money(String date_time);

	// 统计同意退款的个数
	int queryPre_refund_count(String date_time);

	// 查询当天订单成功的总价钱
	double queryPre_succeed_money(String date_time);

	// 查询当天的票数
	int queryPre_ticket_count(String date_time);

}
