package com.l9e.transaction.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderInfoToCpStatDao;
@Repository("orderInfoToCpStatDao")
public class OrderInfoToCpStatDaoImpl extends BaseDao implements OrderInfoToCpStatDao{

	public int queryPre_day_order_count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_day_order_count");
	}

	public int queryPre_day_out_ticket_defeated() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_day_out_ticket_defeated");
	}

	public int queryPre_day_out_ticket_succeed() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_day_out_ticket_succeed");
	}
	
	public int queryPre_bx_count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_bx_count");
	}

	public double queryPre_bx_countMoney() {
		return (Double) this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_bx_countMoney");
	}

	public double queryPre_defeated_money() {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_defeated_money");
	}

	public double queryPre_succeed_money() {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_succeed_money");
	}

	public void addOrderInfoToCpStat(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("orderInfoToCpStat.addOrderInfoToCpStat",map);
	}

	public int queryPre_preparative_count() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_preparative_count");
	}

	public int queryPre_refund_count() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_refund_count");
	}

	public int queryPre_ticket_count() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_ticket_count");
	}

	
	public int queryPre_pay_defeated() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_pay_defeated");
	}
	
	
	
	
	//////////////////////////
	public int query_Hc_StatInfo() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.query_Hc_StatInfo");
	}

	public int queryPre_activeAgent() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToCpStat.queryPre_activeAgent");
	}





}
