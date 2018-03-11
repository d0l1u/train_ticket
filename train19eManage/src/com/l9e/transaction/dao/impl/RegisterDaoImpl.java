package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RegisterDao;

@Repository("registerDao")
public class RegisterDaoImpl extends BaseDao implements RegisterDao{
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRegisterList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("register.queryRegisterList",paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRegisterExcel(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("register.queryRegisterExcel",paramMap);
	}

	
	public int queryRegisterListCount(Map<String, Object> paramMap) {
		return getTotalRows("register.queryRegisterListCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRegisterInfo(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("register.queryRegisterInfo",paramMap);
	}

	public void updateRegisterInfo(Map<String, Object> updateMap) {
		this.getSqlMapClientTemplate().update("register.updateRegisterInfo", updateMap);
	}

	public void updateRegisterFail(Map<String, Object> updateMap) {
		this.getSqlMapClientTemplate().update("register.updateRegisterFail", updateMap);	
	}

	public void updateRegisterSuccess(Map<String, Object> updateMap) {
		this.getSqlMapClientTemplate().update("register.updateRegisterSuccess", updateMap);	
	}

	public void updateRegisterCheck(Map<String, Object> updateMap) {
		this.getSqlMapClientTemplate().update("register.updateRegisterCheck", updateMap);	
	}

	@Override
	public void addAccountInfo(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("register.addAccountInfo", paramMap);
	}

	@Override
	public Map<String, String> queryRegister(String registId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("register.queryRegister", registId);
	}

	@Override
	public void addRegister(Map<String, String> addMap) {
		this.getSqlMapClientTemplate().insert("register.addRegister",addMap);
	}

	@Override
	public String queryRegisterIdcard(String content) {
		return (String)this.getSqlMapClientTemplate().queryForObject("register.queryRegisterIdcard", content);
	}

	@Override
	public void update12306Register(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("register.update12306Register", paramMap);
	}

	@Override
	public void updateRegisterSuccessGrade(Map<String, Object> updateGrade) {
		this.getSqlMapClientTemplate().update("register.updateRegisterSuccessGrade", updateGrade);	
	}
}
