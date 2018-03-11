package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuUrgeRefundDao;
import com.l9e.transaction.vo.TuniuUrgeRefund;
@Repository("tuniuUrgeRefundDao")
public class TuniuUrgeRefundDaoImpl extends BaseDao implements TuniuUrgeRefundDao{

	@Override
	public int queryUrgeRefundCount(Map<String, Object> param) {
		return  (Integer) this.getSqlMapClientTemplate().queryForObject("tuniuUrgeRefund.queryUrgeRefundCount", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TuniuUrgeRefund> queryUrgeRefundList(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("tuniuUrgeRefund.queryUrgeRefundList", param);
	}

	@Override
	public TuniuUrgeRefund queryUrgeRefundInfo(Map<String, Object> param) {
		return (TuniuUrgeRefund) this.getSqlMapClientTemplate().queryForObject("tuniuUrgeRefund.queryUrgeRefundTicketInfo", param);
	}

	@Override
	public int updateUrgeRefundInfo(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().update("tuniuUrgeRefund.updateUrgeRefundInfo", param);
	}

	@Override
	public int updateUrgeRefundByOrderId(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().update("tuniuUrgeRefund.updateUrgeRefundByOrderId", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TuniuUrgeRefund> queryUrgeRefundListForExcel(
			Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("tuniuUrgeRefund.queryUrgeRefundListForExcel", param);
	}

	/* (non-Javadoc)
	 * @see com.l9e.transaction.dao.TuniuUrgeRefundDao#updateUrgeRefund(com.l9e.transaction.vo.TuniuUrgeRefund)
	 * 通过实体对象去更新
	 */
	@Override
	public int updateUrgeRefund(TuniuUrgeRefund tuniuUrgeRefund) {
		return this.getSqlMapClientTemplate().update("tuniuUrgeRefund.updateUrgeRefund", tuniuUrgeRefund);
	}

}
