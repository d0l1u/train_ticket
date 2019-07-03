package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RefundDao;

@Repository("refundDao")
public class RefundDaoImpl extends BaseDao implements RefundDao {
	@Override
	public int queryRefundStreamContainCp(Map<String, String> refundMap) {
		return this.getTotalRows("refund.queryRefundStreamContainCp", refundMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryPassengerInfoByCpId(String cp_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("refund.queryPassengerInfoByCpId", cp_id);
	}
	
	@Override
	public void deleteRefundStreamOnRefuse(Map<String, String> refundMap) {
		this.getSqlMapClientTemplate().delete("refund.deleteRefundStreamOnRefuse", refundMap);
	}

	@Override
	public void addRefundStream(Map<String, String> refundMap) {
		this.getSqlMapClientTemplate().insert("refund.addRefundStream", refundMap);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("refund.querySpecTimeBeforeFrom", order_id);
	}

	@Override
	public void addRefundNotify(Map<String, String> notifyMap) {
		this.getSqlMapClientTemplate().insert("refund.addRefundNotify", notifyMap);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> queryRefundTotalInOrder(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("refund.queryRefundTotalInOrder", order_id);
	}

	@Override
	public int queryRefundLeftCount(String order_id) {
		return this.getTotalRows("refund.queryRefundLeftCount", order_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryRefundResultWaitList() {
		return this.getSqlMapClientTemplate().queryForList("refund.queryRefundResultWaitList");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryRefundStreamListBySeq(
			Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("refund.queryRefundStreamListBySeq", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCpListByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("refund.queryCpListByOrderId", order_id);
	}

	@Override
	public void updateRefundNotfiyFinish(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("refund.updateRefundNotfiyFinish", map);
	}

	@Override
	public int updateRefundNotfiyBegin(Map<String, String> beginMap) {
		return this.getSqlMapClientTemplate().update("refund.updateRefundNotfiyBegin", beginMap);
	}

	@Override
	public int queryRefundCountByMerchantSeq(Map<String, String> merchantMap) {
		return this.getTotalRows("refund.queryRefundCountByMerchantSeq", merchantMap);
	}

	@Override
	public String queryOrderStatusById(Map<String, String> orderMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("refund.queryOrderStatusById", orderMap);
	}
	
	@Override
	public void updateOrderRefundStatus(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("refund.updateOrderRefundStatus", param);
	}

	@Override
	public List<Map<String, String>> queryEopRefundResultWaitList() {
		return (List<Map<String,String>>)this.getSqlMapClientTemplate().queryForList("refund.queryEopRefundResultWaitList");
	}

	@Override
	public int updateEopRefundNotfiyBegin(Map<String, String> beginMap) {
		return this.getSqlMapClientTemplate().update("refund.updateEopRefundNotfiyBegin", beginMap);
	}

	@Override
	public void updateEopRefundNotfiyFinish(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("refund.updateEopRefundNotfiyFinish", map);
	}

	@Override
	public void updateEopRefundStreamInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("refund.updateEopRefundStreamInfo", map);
	}

	@Override
	public void updateRefundStreamEopRefundSeq(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("refund.updateRefundStreamEopRefundSeq", param);
	}

	@Override
	public void updateRefundNotifyRestart(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("refund.updateRefundNotifyRestart", map);
		
	}

	@Override
	public int queryRefundNotifyNum(Map<String, String> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("refund.queryRefundNotifyNum", map);
	}

	@Override
	public String queryRefundStatusByCpId(Map<String, String> map) {
		return  (String) this.getSqlMapClientTemplate().queryForObject("refund.queryRefundStatusByCpId", map);
	}

	@Override
	public void updateRefundStreamTo33(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("refund.updateRefundStreamTo33", updateMap);
	}
}
