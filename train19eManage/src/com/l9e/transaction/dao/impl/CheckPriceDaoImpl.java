package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CheckPriceDao;

@Repository("checkPriceDao")
public class CheckPriceDaoImpl extends BaseDao implements CheckPriceDao {

	@Override
	public void addAlipayInfo(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("checkprice.addAlipayInfo",
				paramMap);
	}


	@Override
	public int queryAlipayCounts(Map<String, Object> pMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryAlipayCounts", pMap);
	}

	// 查询List
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOutTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryOutTicketList", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryRefundTicketList", paramMap);
	}

	// 查询总数
	@Override
	public int queryOutTicketCounts(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryOutTicketCounts", paramMap);
	}

	// 查询支付宝余额总数
	@Override
	public int queryAlipayBalanceCounts(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryAlipayBalanceCounts", paramMap);
	}

	// 查询支付宝账户余额List
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAlipayBalanceList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryAlipayBalanceList", paramMap);
	}

	@Override
	public int queryRefundTicketCounts(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryRefundTicketCounts", paramMap);
	}

	// 导表
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOutTicketExcel(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryOutTicketExcel", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryRefundTicketExcel(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryRefundTicketExcel", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOutAlipayBalanceExcel(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryOutAlipayBalanceExcel", paramMap);
	}

	@Override
	public int updateSeqById(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update(
				"checkprice.updateSeqById", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAlipayExcel(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryAlipayExcel", map);
	}

	@Override
	public int updateOrderInfo(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().update(
				"checkprice.updateOrderInfo", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryNeedRefund(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"checkprice.queryNeedRefund", map);
	}

	@Override
	public int updateRefundPrice(Map<String, String> m) {
		return this.getSqlMapClientTemplate().update(
				"checkprice.updateRefundPrice", m);
	}

	@Override
	public String queryElongRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryElongRefund", m);
	}

	@Override
	public String queryExtRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryExtRefund", m);
	}

	@Override
	public String queryGtRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryGtRefund", m);
	}

	@Override
	public String queryInnerRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryInnerRefund", m);
	}

	@Override
	public String queryMtRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryMtRefund", m);
	}

	@Override
	public String queryQunarRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryQunarRefund", m);
	}

	@Override
	public String queryTuniuRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryTuniuRefund", m);
	}

	@Override
	public String queryXcRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryXcRefund", m);
	}

	@Override
	public String queryl9eRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryl9eRefund", m);
	}

	@Override
	public String queryAppRefund(Map<String, String> m) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"checkprice.queryAppRefund", m);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> selectAlipay(Map<String, String> m) {
		return this.getSqlMapClientTemplate().queryForList("checkprice.selectAlipay", m);
	}


	@Override
	public int deleteTicketById(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().update(
				"checkprice.updateTicketById", map);
	}
}
