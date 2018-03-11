package com.l9e.transaction.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderInfoToCpStatOneDao;
@Repository("orderInfoToCpStatOneDao")
public class OrderInfoToCpStatOneDaoImpl extends BaseDao implements OrderInfoToCpStatOneDao{

	public int query_Hc_StatInfo() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.query_Hc_StatInfo");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryDate() {
		return  this.getSqlMapClientTemplate().queryForList("orderInfoToCpStatOne.queryDate");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> createDateList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("orderInfoToCpStatOne.createDateList",map);
	}

	public void addOrderInfoToCpStat(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("orderInfoToCpStatOne.addOrderInfoToCpStat",map);
	}

	public int queryPre_bx_count(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_bx_count",date_time);
	}

	public double queryPre_bx_countMoney(String date_time) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_bx_countMoney",date_time);
	}

	public int queryPre_day_order_count(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_day_order_count",date_time);
	}

	public int queryPre_day_out_ticket_defeated(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_day_out_ticket_defeated",date_time);
	}

	public int queryPre_day_out_ticket_succeed(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_day_out_ticket_succeed",date_time);
	}

	public double queryPre_defeated_money(String date_time) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_defeated_money",date_time);
	}

	public int queryPre_preparative_count(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_preparative_count",date_time);
	}

	public int queryPre_refund_count(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_refund_count",date_time);
	}

	public double queryPre_succeed_money(String date_time) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_succeed_money",date_time);
	}

	public int queryPre_ticket_count(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_ticket_count",date_time);
	}

	public int queryPre_pay_defeated(String date_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_pay_defeated",date_time);
	}

	public int queryPre_activeAgent(String dateTime) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStatOne.queryPre_activeAgent",dateTime);
	}

}
