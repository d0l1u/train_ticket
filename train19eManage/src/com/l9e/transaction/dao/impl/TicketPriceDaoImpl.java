package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TicketPriceDao;
@Repository("ticketPriceDao")
public class TicketPriceDaoImpl extends BaseDao implements TicketPriceDao {

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("ticketPrice.queryTicketPriceCount", paramMap);
	}

	public List<Map<String, String>> queryTicketPriceList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("ticketPrice.queryTicketPriceList", paramMap);
	}

	public Map<String, String> queryTicketPriceModify(Map<String, String> updateMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("ticketPrice.queryTicketPriceModify", updateMap);
	}

	public void updateTicketPrice(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("ticketPrice.updateTicketPrice", updateMap);
	}

	public int queryTicketPriceCheci(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("ticketPrice.queryTicketPriceCheci", paramMap);
	}

	public void addTicketPrice(Map<String, String> addMap) {
		this.getSqlMapClientTemplate().insert("ticketPrice.addTicketPrice", addMap);
	}

	public void addTicletPriceLogs(Map<String, String> logMap) {
		this.getSqlMapClientTemplate().insert("ticketPrice.addTicletPriceLogs",logMap);
	}

	@Override
	public void deleteTicketPrice(Map<String, String> deleteMap) {
		this.getSqlMapClientTemplate().delete("ticketPrice.deleteTicketPrice",deleteMap);
	}

	@Override
	public int queryTicketPriceLogCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("ticketPrice.queryTicketPriceLogListCount");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryTicketPriceLogList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("ticketPrice.queryTicketPriceLogList", paramMap);
	}

}
