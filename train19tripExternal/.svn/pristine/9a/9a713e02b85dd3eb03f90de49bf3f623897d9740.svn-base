package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TextDao;

@Repository("textDao")
public class TextDaoImpl  extends BaseDao implements TextDao {
	@Override
	public Map<String, Object> queryOrderInfo(String orderId) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("text.queryOrderInfo",orderId);
	}
	@Override
	public List<String> queryOutTicket() {
		System.out.println("-------------queryOutTicket----------------");
		return this.getSqlMapClientTemplate().queryForList("text.queryOutTicket");
	}
	@Override
	public int updateNoticeInfo(Map<String, Object> orderInfo) {
		return this.getSqlMapClientTemplate().update("text.updateNoticeInfo",orderInfo);
	}
	@Override
	public void updateOrderInfo(Map<String, Object> orderInfo) {
		this.getSqlMapClientTemplate().update("text.updateOrderInfo",orderInfo);
	}
	@Override
	public Map<String, Object> queryRefundOrderInfo(Map<String, Object> param) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("text.queryRefundOrderInfo",param);
	}
	@Override
	public List<Map<String, Object>> queryRefundTicket() {
		return this.getSqlMapClientTemplate().queryForList("text.queryRefundTicket");
	}
	@Override
	public void updateRefundOrderInfo(Map<String, Object> refundInfo) {
		this.getSqlMapClientTemplate().update("text.updateRefundOrderInfo",refundInfo);
	}
	@Override
	public Map<String, Object> queryCpInfo(String cp_id) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("text.queryCpInfo",cp_id);
	}
	@Override
	public List<String> queryOutTicketCpId(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("text.queryOutTicketCpId",orderId);
	}
	@Override
	public void updateCpInfo(Map<String, Object> cpInfo) {
		this.getSqlMapClientTemplate().update("text.updateCpInfo",cpInfo);
	}
	@Override
	public Map<String, Object> queryMerchantinfo(String channel) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("text.queryMerchantinfo",channel);
	}
	@Override
	public List<String> queryYdToOutTicket() {
		return this.getSqlMapClientTemplate().queryForList("text.queryYdToOutTicket");
	}
	@Override
	public void updateRefundNoticeInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("text.updateRefundNoticeInfo",map);
	}
	@Override
	public int selectCountNumFromNotice(Map<String, Object> orderInfo) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("text.selectCountNumFromNotice",orderInfo);
	}
	@Override
	public List<String> queryPaySuccess() {
		return this.getSqlMapClientTemplate().queryForList("text.queryPaySuccess");
	}
	@Override
	public String selectSeatType(String order_id) {
		// TODO Auto-generated method stub
		return  (String)this.getSqlMapClientTemplate().queryForObject("text.selectSeatType",order_id);
	}
}
