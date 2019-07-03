package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_Account_Dao;

@Repository("tj_Account_Dao")
public class Tj_Account_DaoImpl extends BaseDao implements Tj_Account_Dao{

	public void addToTj_Account(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_Account.addToTj_Account",map);
	}
	public void updateToTj_Account(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("tj_Account.updateToTj_Account", map);
	}
	public int query19eTodayCount(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Account.query19eTodayCount", map);
	}
	public int account_num(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Account.account_num", paramMap);
	}
	public int regist_num(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Account.regist_num", paramMap);
	}
	
	public int account_can_use(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Account.account_can_use", paramMap);
	}
	
	public String setting_value(String setting_name) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tj_Account.setting_value", setting_name);
	}
	
}
