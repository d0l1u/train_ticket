package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RefundDao;

@Repository("refundDao")
public class RefundDaoImpl extends BaseDao implements RefundDao{

	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		return (List<Map<String, String>>)this.getSqlMapClientTemplate().queryForList("refund.queryCanRefundStreamList");
	}

	@Override
	public void updateCPAlterInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("refund.updateCPAlterInfo",map);
	}

	@Override
	public void updateRefundInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("refund.updateRefundInfo",map);
	}

	@Override
	public Map<String, Object> queryAccountOrderInfo(Map<String, String> param) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("refund.queryAccountOrderInfo",param);
	}

	@Override
	public Map<String, String> queryRefundCpOrderInfo(Map<String, String> param) {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("refund.queryRefundCpOrderInfo",param);
	}

	@Override
	public int updateOrderRefundStatus(Map<String, String> map) {
		return (Integer)this.getSqlMapClientTemplate().update("refund.updateOrderRefundStatus",map);
	}

	@Override
	public void updateCPRefundInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("refund.updateCPRefundInfo",map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryChangeRefundCpOrderInfo(Map<String, String> param) {
		
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("refund.queryChangeRefundCpOrderInfo", param);
	}


}
