package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ReceiveNotifyDao;

@Repository("receiveNotifyDao")
public class ReceiveNotifyDaoImpl extends BaseDao implements ReceiveNotifyDao{

	public void updateOrderWithCpNotify(Map<String, String> paraMap) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderWithCpNotify", paraMap);
	}

	public void updateCpOrderWithCpNotify(Map<String, String> cpMap) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateCpOrderWithCpNotify", cpMap);
	}
	@Override
	public void addOrderResultNotify(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("receiveNotify.addOrderResultNotify", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> findOrderResultNotify() {
		return this.getSqlMapClientTemplate().queryForList("receiveNotify.findOrderResultNotify");
	}

	@Override
	public void updateOrderResultNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderResultNotify", map);
	}

	@Override
	public void updateOrderResultNotifyStartNum(String order_id) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderResultNotifyStartNum", order_id);
	}

	@Override
	public void addPayResultNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("receiveNotify.addPayResultNotify", map);
	}

	@Override
	public void updatePayResultNotify(String order_id) {
		this.getSqlMapClientTemplate().update("receiveNotify.updatePayResultNotify", order_id);
	}

	@Override
	public void updatePayResultNotifyNum(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("receiveNotify.updatePayResultNotifyNum", param);
	}

	@Override
	public List<Map<String, String>> findPayResultNotify() {
		return this.getSqlMapClientTemplate().queryForList("receiveNotify.findPayResultNotify");
	}

	@Override
	public int queryEopRefundNotifyAlterNum(String refund_seq) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("receiveNotify.queryEopRefundNotifyAlterNum",refund_seq);
	}

	@Override
	public void updateEopRefundResult(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateEopRefundResult", paramMap);
	}

	@Override
	public void addEopRefundNotify(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("receiveNotify.addEopRefundNotify", paramMap);
	}

	@Override
	public void updateOrderReturnStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderReturnStatus", map);
	}

	@Override
	public List<Map<String, String>> findOrderBookNotify() {
		return this.getSqlMapClientTemplate().queryForList("receiveNotify.findOrderBookNotify");
	}

	@Override
	public void updateOrderBookStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderBookStatus", map);
	}

	@Override
	public void updateOrderBookNotifyStartNum(String order_id) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderBookNotifyStartNum", order_id);
	}

	@Override
	public void updatePayResultNotifyStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("receiveNotify.updatePayResultNotifyStatus", map);
	}

	@Override
	public void updateOrderBookNotifyFinish(String order_id) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderBookNotifyFinish", order_id);
	}

	@Override
	public void addEopAndPayNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("receiveNotify.addEopAndPayNotify", map);
	}

	@Override
	public List<Map<String, String>> queryEopAndPayNotify() {
		return this.getSqlMapClientTemplate().queryForList("receiveNotify.queryEopAndPayNotify");
	}

	@Override
	public void updateEopAndPayNotifyInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateEopAndPayNotifyInfo", param);
	}

	@Override
	public void updateEopAndPayNotifyFinish(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateEopAndPayNotifyFinish", param);
	}

	@Override
	public void updateEopAndPayNotifyNums(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateEopAndPayNotifyNums", param);
	}

	@Override
	public void insertPayReturnNotify(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("receiveNotify.insertPayReturnNotify", param);
	}

	@Override
	public void updateOrderPayNotifyFinish(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("receiveNotify.updatePayReturnNotifyStatus", param);
	}

	@Override
	public List<Map<String, String>> findOrderPayNotify() {
		return this.getSqlMapClientTemplate().queryForList("receiveNotify.findOrderPayNotify");
	}

	@Override
	public void updatePayReturnNotifyNums(String order_id) {
		this.getSqlMapClientTemplate().update("receiveNotify.updatePayReturnNotifyNums", order_id);
	}

	@Override
	public Integer queryOrderResultNotifyStartNum(String order_id) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("receiveNotify.queryOrderResultNotifyStartNum",order_id);
	}

	@Override
	public String queryMerchantIdByOrderId(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("receiveNotify.queryMerchantIdByOrderId",orderId);
	}

	@Override
	public Integer updateOrderGtBookNotifyStatus(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Integer) this.getSqlMapClientTemplate().update("receiveNotify.updateOrderGtBookNotifyStatus",paramMap);
	}

	@Override
	public int updateOrderResultStatus(Map<String, String> updatenew) {
		// TODO Auto-generated method stub
	    return (Integer) this.getSqlMapClientTemplate().update("receiveNotify.updateOrderResultStatus",updatenew);
	}

	@Override
	public String queryBookNotifyStatus(String order_id) {
		// TODO Auto-generated method stub
		return (String) this.getSqlMapClientTemplate().queryForObject("receiveNotify.queryBookNotifyStatus",order_id);
	}

	@Override
	public int queryOrderBookNotifyStartNum(String order_id) {
		return (Integer)getSqlMapClientTemplate().queryForObject("receiveNotify.queryOrderBookNotifyStartNum",order_id);
	}
}