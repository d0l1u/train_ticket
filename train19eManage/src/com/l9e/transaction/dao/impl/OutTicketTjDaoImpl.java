package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OutTicketTjDao;

@Repository("outTicketTjDao")
public class OutTicketTjDaoImpl extends BaseDao implements OutTicketTjDao{

	@Override
	public int queryOutTicketTjCounts(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("outTicketTj.queryOutTicketTjCounts",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOutTicketTjList(
			Map<String, Object> paramMap) {
		return  this.getSqlMapClientTemplate().queryForList("outTicketTj.queryOutTicketTjList",paramMap);
	}


}
