package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuRefundDao;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.TuniuRefund;
import com.l9e.transaction.vo.TuniuUrgeRefund;

@Repository("tuniuRefundDao")
public class TuniuRefundDaoImpl extends BaseDao implements TuniuRefundDao {

	@Override
	public void insertRefund(TuniuRefund refund) {
		getSqlMapClientTemplate().insert("refund.insertRefund", refund);
	}

	@Override
	public TuniuRefund selectOneRefund(Map<String, Object> queryParam) {
		return (TuniuRefund) getSqlMapClientTemplate().queryForObject("refund.selectRefund", queryParam);
	}

	@Override
	public void updateRefund(TuniuRefund refund) {
		getSqlMapClientTemplate().update("refund.updateRefund", refund);
	}

	@Override
	public Account selectOneAccount(Map<String, Object> queryParam) {
		return (Account) getSqlMapClientTemplate().queryForObject("refund.selectAccount", queryParam);
	}

	@Override
	public void deleteRefund(TuniuRefund refund) {
		getSqlMapClientTemplate().delete("refund.deleteRefund", refund);
	}

	@Override
	public TuniuUrgeRefund getUrgeRefundInfo(Map<String, Object> param) {
		return (TuniuUrgeRefund) getSqlMapClientTemplate().queryForObject("refund.getUrgeRefundInfo", param);
	}

	@Override
	public void insertUrgeRefund(TuniuUrgeRefund urgeRefundInfo) {
		getSqlMapClientTemplate().insert("refund.insertUrgeRefund", urgeRefundInfo);
	}

	@Override
	public int updateUrgeRefund(TuniuUrgeRefund urgeRefundInfo) {
		return this.getSqlMapClientTemplate().update("refund.updateUrgeRefundInfo", urgeRefundInfo);
	}
	@SuppressWarnings("unchecked")
	public List<Integer> checkOfflineNotify(Map<String, Object> queryParam){
		return this.getSqlMapClientTemplate().queryForList("refund.checkOfflineNotify", queryParam);
	}

}
