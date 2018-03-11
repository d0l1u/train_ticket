package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.UserStatToUserinfoDao;

@Repository("userStatToUserinfoDao")
public class UserStatToUserinfoDaoImpl extends BaseDao implements UserStatToUserinfoDao{
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryStatList() {
		return this.getSqlMapClientTemplate().queryForList("statToUserinfo.queryStatList");
	}
	@SuppressWarnings("unchecked")
	public String queryUser(String queryUser_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("statToUserinfo.queryUser",queryUser_id);
	}
	@SuppressWarnings("unchecked")
	public void addUserStat(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("statToUserinfo.addUserStat",map);
	}
	@SuppressWarnings("unchecked")
	public void updateUserStat(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("statToUserinfo.updateUserStat",map);
	}

}
