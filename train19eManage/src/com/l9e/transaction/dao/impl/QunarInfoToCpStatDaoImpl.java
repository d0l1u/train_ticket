package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QunarInfoToCpStatDao;
@Repository("qunarInfoToCpStatDao")
public class QunarInfoToCpStatDaoImpl extends BaseDao implements
		QunarInfoToCpStatDao {

	public void addOrderInfoToCpStat(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert(
				"qunarInfoToCpStat.addOrderInfoToCpStat", map);
	}

	public int queryPre_day_order_count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_day_order_count");
	}

	public int queryPre_day_out_ticket_defeated() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_day_out_ticket_defeated");
	}

	public int queryPre_day_out_ticket_succeed() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_day_out_ticket_succeed");
	}

	public int queryPre_refund_count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_refund_count");
	}

	public int queryPre_ticket_count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_ticket_count");
	}

	public int query_Qunar_StatInfo() {
		return 0;
	}

	public double queryPre_succeed_money() {
		return (Double) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_succeed_money");
	}

	// 前一天失败的总价钱数
	public double queryPre_defeated_money() {
		return (Double) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_defeated_money");
	}

	public int queryPre_preparative_count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarInfoToCpStat.queryPre_preparative_count");
	}
}
