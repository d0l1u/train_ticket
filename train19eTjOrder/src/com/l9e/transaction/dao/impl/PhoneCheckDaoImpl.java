package com.l9e.transaction.dao.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.PhoneCheckDao;

@Repository("phoneCheckDao")
public class PhoneCheckDaoImpl extends BaseDao implements PhoneCheckDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAccountInfo(Map<String, Object> querymap) {
		return this.getSqlMapClientTemplate().queryForList("phonecheck.queryAccountInfo",querymap);
	}
	
	public int updateRefundStatus(Map<String, String> map) {
		return (Integer) this.getSqlMapClientTemplate().update("phonecheck.updateRefundStatus",map);
	}
	
	public void addAccountCheckInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("phonecheck.addAccountCheckInfo", map);
	}

	public void updateAccountCheckInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("phonecheck.updateAccountCheckInfo", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getPhoneMsgList() {
		return this.getSqlMapClientTemplate().queryForList("phonecheck.getPhoneMsgList");
	}

	public int queryAccountNum(Map<String, String> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("phonecheck.queryAccountNum",map);
	}

	public void addAccountCheckRefundInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("phonecheck.addAccountCheckRefundInfo", map);
	}


}
