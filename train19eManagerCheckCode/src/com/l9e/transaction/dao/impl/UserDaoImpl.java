package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.UserDao;
@Repository("userDao")
public class UserDaoImpl extends BaseDao implements UserDao {

	@Override
	public Map<String, String> queryUserInfo(String optRen) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("user.queryUserInfo", optRen);
	}

	@Override
	public void updateUser(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("user.updateUser", map);
	}

	@Override
	public int queryDayUserCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("user.queryDayUserCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryDayUserList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("user.queryDayUserList", paramMap);
	}

	@Override
	public void updateLogined(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("user.updateLogined", map);
	}
	
}
