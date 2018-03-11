package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QunarStatFirstDao;
import com.l9e.transaction.service.QunarStatFirstService;

@Service("qunarStatFirstService")
public class QunarStatFirstServiceImpl implements QunarStatFirstService {

	@Resource
	private QunarStatFirstDao qunarStatFirstDao;

	// 插入统计完成的信息
	public void addOrderInfoToCpStat(Map<String, Object> map) {
		qunarStatFirstDao.addOrderInfoToCpStat(map);
	}

	// 查询时间
	public List<Map<String, Object>> createDateList(Map<String, Object> map) {
		return qunarStatFirstDao.createDateList(map);
	}

	// 查询最大和最小时间
	public List<Map<String, Object>> queryDate() {
		return qunarStatFirstDao.queryDate();
	}

	// 查询当天总条数
	public int queryPre_day_order_count(String date_time) {
		return qunarStatFirstDao.queryPre_day_order_count(date_time);
	}

	// 查询当天订单失败的条数
	public int queryPre_day_out_ticket_defeated(String date_time) {
		return qunarStatFirstDao.queryPre_day_out_ticket_defeated(date_time);
	}

	// 查询当天订单成功的条数
	public int queryPre_day_out_ticket_succeed(String date_time) {
		return qunarStatFirstDao.queryPre_day_out_ticket_succeed(date_time);
	}

	// 查询当天订单失败的总价钱
	public double queryPre_defeated_money(String date_time) {
		return qunarStatFirstDao.queryPre_defeated_money(date_time);
	}

	// 统计同意退款的个数
	public int queryPre_refund_count(String date_time) {
		return qunarStatFirstDao.queryPre_refund_count(date_time);
	}

	// 查询当天订单成功的总价钱
	public double queryPre_succeed_money(String date_time) {
		return qunarStatFirstDao.queryPre_succeed_money(date_time);
	}

	// 查询当天的票数
	public int queryPre_ticket_count(String date_time) {
		return qunarStatFirstDao.queryPre_ticket_count(date_time);
	}

	// 查询总数
	public int query_Hc_StatInfo() {
		return qunarStatFirstDao.query_Hc_StatInfo();
	}

}
