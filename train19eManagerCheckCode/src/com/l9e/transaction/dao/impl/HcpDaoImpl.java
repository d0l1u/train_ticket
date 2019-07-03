package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.HcpDao;
@Repository("hcpDao")
public class HcpDaoImpl extends BaseDao implements HcpDao {

	@Override
	public int queryAdminCurrentNameCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcp.queryAdminCurrentNameCount");
	}

	@Override
	public int queryCodeCountToday() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcp.queryCodeCountToday");
	}

	@Override
	public int queryCodeToday() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcp.queryCodeToday");
	}

	@Override
	public int queryMinutes() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcp.queryMinutes");
	}

	@Override
	public int querySuccess1() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcp.querySuccess1");
	}

	@Override
	public int querySuccess2() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcp.querySuccess2");
	}

	@Override
	public int queryUncode(String channel) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcp.queryUncode",channel);
	}
	
	@Override
	public String codeQunarType() {
		return (String)this.getSqlMapClientTemplate().queryForObject("hcp.codeQunarType");
	}

}
