package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AppUserDao;
@Repository("appUserDao")
public class AppUserDaoImpl extends BaseDao implements AppUserDao {

	@Override
	public List<Map<String, Object>> queryAppUserList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("appUser.queryAppUserList", paramMap);
	}

	@Override
	public int queryAppUserListCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("appUser.queryAppUserListCount", paramMap);
	}

	@Override
	public Integer queryLinkerNum(String userId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("appUser.queryLinkerNum", userId);
	}

	@Override
	public Integer queryRefereeAccountNum(String userId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("appUser.queryRefereeAccountNum", userId);
	}

	@Override
	public Map<String, String> queryAppUserInfo(String userId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("appUser.queryAppUserInfo", userId);
	}

	@Override
	public List<Map<String, Object>> queryAppUserLinkerList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("appUser.queryAppUserLinkerList", paramMap);
	}

	@Override
	public int queryAppUserLinkerListCount(String userId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("appUser.queryAppUserLinkerListCount", userId);
	}

	@Override
	public void updateAppUser(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("appUser.updateAppUser", paramMap);
	}

	@Override
	public void deleteAppUser(String userId) {
		this.getSqlMapClientTemplate().delete("appUser.deleteAppUser", userId);
	}

	@Override
	public void updateAppUserStop(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("appUser.updateAppUserStop", paramMap);
	}

}
