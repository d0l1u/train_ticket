package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_DealerDao;

@Repository
public class Tj_DealerDaoImpl extends BaseDao implements Tj_DealerDao{

	public void addToTj_Dealer(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_Dealer.addToTj_Dealer",map);
	}

	public String queryAreaNameByDealerId(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tj_Dealer.queryAreaNameByDealerId",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryDealerIdByMouth(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("tj_Dealer.queryDealerIdByMouth",paramMap);
	}

	public String queryOrderCountByDealerId(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tj_Dealer.queryOrderCountByDealerId",paramMap);
	}

	public String queryPayMoneyByDealerId(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tj_Dealer.queryPayMoneyByDealerId",paramMap);
	}

	public String queryRefundCountByDealerId(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tj_Dealer.queryRefundCountByDealerId",paramMap);
	}

	public String queryRefundMoneyByDealerId(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tj_Dealer.queryRefundMoneyByDealerId",paramMap);
	}
	

}
