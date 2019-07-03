package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QunarStatFirstDao;
@Repository("qunarStatFirstDao")
public class QunarStatFirstDaoImpl extends BaseDao implements QunarStatFirstDao {

	// 查询总数
	public int query_Hc_StatInfo() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.query_Hc_StatInfo");
	}

	// 查询最大和最小时间
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryDate() {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarstatfirst.queryDate");
	}

	// 查询时间
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> createDateList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarstatfirst.createDateList", map);
	}

	// 插入统计完成的信息
	public void addOrderInfoToCpStat(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert(
				"qunarstatfirst.addOrderInfoToCpStat", map);
	}

	// 查询当天总条数
	public int queryPre_day_order_count(String date_time) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.queryPre_day_order_count", date_time);
	}

	// 查询当天订单失败的条数
	public int queryPre_day_out_ticket_defeated(String date_time) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.queryPre_day_out_ticket_defeated", date_time);
	}

	// 查询当天订单成功的条数
	public int queryPre_day_out_ticket_succeed(String date_time) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.queryPre_day_out_ticket_succeed", date_time);
	}

	// 查询当天订单失败的总价钱
	public double queryPre_defeated_money(String date_time) {
		return (Double) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.queryPre_defeated_money", date_time);
	}

	// 统计同意退款的个数
	public int queryPre_refund_count(String date_time) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.queryPre_refund_count", date_time);
	}

	// 查询当天订单成功的总价钱
	public double queryPre_succeed_money(String date_time){
		return (Double) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.queryPre_succeed_money", date_time);
	}

	// 查询当天的票数
	public int queryPre_ticket_count(String date_time) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarstatfirst.queryPre_ticket_count", date_time);
	}

}
