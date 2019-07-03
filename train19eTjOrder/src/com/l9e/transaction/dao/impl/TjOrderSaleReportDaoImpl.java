package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TjOrderSaleReportDao;
@Repository("tjOrderSaleReportDao")
public class TjOrderSaleReportDaoImpl extends BaseDao implements TjOrderSaleReportDao {

	public void addToTjOrderSaleReportJob(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tjOrderSaleRepor.addToTjOrderSaleReportJob", map);
	}

	public List<String> queryDateList() {
		return this.getSqlMapClientTemplate().queryForList("tjOrderSaleRepor.queryDateList");
	}

	public List<String> queryDealeiIdList(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("tjOrderSaleRepor.queryDealeiIdList", createTime);
	}

	public double queryMonthBxcountMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryMonthBxcountMoney10", paramMap);
	}

	public double queryMonthBxcountMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryMonthBxcountMoney20", paramMap);
	}

	public int queryMonthOrdercount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryMonthOrdercount", paramMap);
	}

	public int queryMonthTicketcount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryMonthTicketcount", paramMap);
	}

	public int queryTableCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryTableCount");
	}

	public double queryThisBxcountMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryThisBxcountMoney10", paramMap);
	}

	public double queryThisBxcountMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryThisBxcountMoney20", paramMap);
	}

	public int queryThisOrdercount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryThisOrdercount", paramMap);
	}

	public double queryThisPaymoney(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryThisPaymoney", paramMap);
	}

	public double queryThisRefundmoney(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryThisRefundmoney", paramMap);
	}

	public int queryThisTicketcount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjOrderSaleRepor.queryThisTicketcount", paramMap);
	}

}
