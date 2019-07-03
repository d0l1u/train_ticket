package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.UserInfoDao;

@Repository("userInfoDao")
public class UserInfoDaoImpl extends BaseDao implements UserInfoDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getUserIdsCardInfo(Map<String,String> map) {
		// TODO Auto-generated method stub
		return  this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryUserIdsCardInfo", map);
	}

	@Override
	public void addUserinfoCheck(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("userIdsCardInfo.addUserInfoCheck", map);
	}

	@Override
	public int updateUserInfoCheck(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("userIdsCardInfo.updateUserInfoCheck", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryUserInfoCheckList() {
		return this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryUserInfoCheckList");
	}

	@Override
	public int updateHcUserRegistStatus(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("userIdsCardInfo.updateHcUserRegistStatus", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryAgentPassNumAndId() {
		return this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryAgentPassNumAndId");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryAgentOrderInfo(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryAgentOrderInfo", map);
	}

	@Override
	public void addAgentwinning(Map<String, Object> agentWinningMap) {
		this.getSqlMapClientTemplate().insert("userIdsCardInfo.addAgentWinningInfo", agentWinningMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryAgentWinningDay(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryAgentWinningDay", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryAgentWinningInfo() {
		return this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryAgentWinningInfo");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAgentLogin(String agentId) {
		return this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryAgentLogin",agentId);
	}

	@Override
	public void addAgentLoginInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("userIdsCardInfo.addAgentLoginInfo", map);
		
	}

	@Override
	public void addAgentPassInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("userIdsCardInfo.addAgentPassInfo", param);
	}

	@Override
	public void deleteAgentPass(Map<String, Object> map) {
		this.getSqlMapClientTemplate().delete("userIdsCardInfo.deleteAgentPass", map);
	}

	@Override
	public void deleteAgentPassInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().delete("userIdsCardInfo.deleteAgentPassInfo", param);
	}

	@Override
	public Integer queryAgentPassTotalNum(String userId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("userIdsCardInfo.queryAgentPassTotalNum", userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryLinkInfoList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryLinkInfoList", paramMap);
	}

	@Override
	public Integer queryPassNumByCard(Map<String, String> param) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("userIdsCardInfo.queryPassNumByCard", param);
	}
}
