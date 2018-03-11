package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.PsOrderDao;
import com.l9e.transaction.vo.AcquireVo;

@Repository("psOrderDao")
public class PsOrderDaoImpl extends BaseDao implements PsOrderDao{

	@Override
	public String queryDbOrderStatus(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("psOrder.queryDbOrderStatus",orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryHistroyByOrderId(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("psOrder.queryHistroyByOrderId",orderId);
	}

	@Override
	public int queryPsOrderCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("psOrder.queryPsOrderCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryPsOrderInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("psOrder.queryPsOrderInfo",orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryPsOrderInfoCp(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("psOrder.queryPsOrderInfoCp",orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryPsOrderList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("psOrder.queryPsOrderList",paramMap);
	}

	@Override
	public void updatePsOrderStatus(AcquireVo acquire) {
		this.getSqlMapClientTemplate().update("psOrder.updatePsOrderStatus",acquire);
	}

	@Override
	public void updatePsOrderCpInfo(Map<String, String> cpInfo) {
		this.getSqlMapClientTemplate().update("psOrder.updatePsOrderCpInfo",cpInfo);
	}
	// TODO Auto-generated method stub
	@Override
	public void addRefundStream(Map<String, String> refundMap) {
		this.getSqlMapClientTemplate().insert("psOrder.addRefundStream", refundMap);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderDiffer(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("psOrder.queryOrderDiffer", order_id);
	}

	@Override
	public void updateBxStatusNotSend(String order_id) {
		this.getSqlMapClientTemplate().update("psOrder.updateBxStatusNotSend", order_id);
	}

	@Override
	public void updateCpOrderWithCpNotify(Map<String, String> cpMap) {
		this.getSqlMapClientTemplate().update("psOrder.updateCpOrderWithCpNotify", cpMap);
	}


	@Override
	public void updateOrderRefundTotal(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("psOrder.updateOrderRefundTotal", paramMap);
	}


	@Override
	public void updateOrderWithCpNotify(Map<String, String> paraMap) {
		this.getSqlMapClientTemplate().update("psOrder.updateOrderWithCpNotify", paraMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryPsOrderInfoPssm(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("psOrder.queryPsOrderInfoPssm",orderId);
	}

	

}
