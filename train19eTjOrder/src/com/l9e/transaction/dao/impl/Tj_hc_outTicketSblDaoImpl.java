package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_hc_outTicketSblDao;
@Repository("tj_hc_outTicketSblDao")
public class Tj_hc_outTicketSblDaoImpl extends BaseDao implements Tj_hc_outTicketSblDao {

	public void addToTj_hc_outTicketSbl(Map paramMap) {
		this.getSqlMapClientTemplate().insert("tj_hc_outTicketSbl.addToTj_hc_outTicketSbl", paramMap);
	}

	public int queryCount(Map paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_hc_outTicketSbl.queryCount", paramMap);
	}

	public List<Map<String, String>> queryOutTicketSblAfter(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("tj_hc_outTicketSbl.queryOutTicketSblAfter", createTime);
	}

	public List<Map<String, String>> queryOutTicketSblBefore(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("tj_hc_outTicketSbl.queryOutTicketSblBefore", createTime);
	}

	public void updateTj_hc_outTicketSbl(Map paramMap) {
		this.getSqlMapClientTemplate().update("tj_hc_outTicketSbl.updateTj_hc_outTicketSbl", paramMap);
	}
	
}
