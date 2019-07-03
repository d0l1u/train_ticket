package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuPushTimeoutOrderDao;

@Repository("tuniuPushTimeoutOrderDao")
public class TuniuPushTimeoutOrderDaoImpl extends BaseDao implements TuniuPushTimeoutOrderDao{

	@Override
	public int queryTuniuTimeOutListCount(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return  (Integer) this.getSqlMapClientTemplate().queryForObject("tuniuTimeOutOrder.queryTuniuTimeOutListCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTuniuTimeOutList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return  this.getSqlMapClientTemplate().queryForList("tuniuTimeOutOrder.queryTuniuTimeOutList", paramMap);
	}

	@Override
	public void changeDealStatus(Map<String, String> map) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("tuniuTimeOutOrder.changeDealStatus", map);
	}
	
	
	
	

}
