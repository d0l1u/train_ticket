package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.MailDao;
@Repository("mailDao")
public class MailDaoImpl extends BaseDao implements MailDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryMailList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("mail.queryMailList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryMailExcel(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("mail.queryMailExcel", paramMap);
	}
	
	@Override
	public int queryMailListCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("mail.queryMailListCount", paramMap);
	}

	@Override
	public void addMail(Map<String, String> addMap) {
		this.getSqlMapClientTemplate().insert("mail.addMail", addMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryMailModify(String mailId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("mail.queryMailModify", mailId);
	}

	@Override
	public void updateMail(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("mail.updateMail", updateMap);
	}

	@Override
	public String queryMailAddress(String content) {
		return (String)this.getSqlMapClientTemplate().queryForObject("mail.queryMailAddress", content);
	}

}
