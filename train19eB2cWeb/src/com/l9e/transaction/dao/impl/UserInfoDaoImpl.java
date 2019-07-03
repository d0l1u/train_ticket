package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.common.LoginUserInfo;
import com.l9e.transaction.dao.UserInfoDao;

@Repository("userInfoDao")
public class UserInfoDaoImpl extends BaseDao implements UserInfoDao{

	@SuppressWarnings("unchecked")
	
	public List<Map<String, String>> getUserIdsCardInfo(Map<String,String> map) {
		// TODO Auto-generated method stub
		return  this.getSqlMapClientTemplate().queryForList("userInfo.queryUserIdsCardInfo", map);
	}

	
	public void addUserinfoCheck(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("userInfo.addUserInfoCheck", map);
	}

	
	public int updateUserInfoCheck(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("userInfo.updateUserInfoCheck", map);
	}

	@SuppressWarnings("unchecked")
	
	public List<Map<String, String>> queryUserInfoCheckList() {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryUserInfoCheckList");
	}

	
	public int updateHcUserRegistStatus(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("userInfo.updateHcUserRegistStatus", map);
	}

	@SuppressWarnings("unchecked")
	
	public List<Map<String, Object>> queryAgentPassNumAndId() {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryAgentPassNumAndId");
	}

	@SuppressWarnings("unchecked")
	
	public List<Map<String, Object>> queryAgentOrderInfo(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryAgentOrderInfo", map);
	}

	
	public void addAgentwinning(Map<String, Object> agentWinningMap) {
		this.getSqlMapClientTemplate().insert("userInfo.addAgentWinningInfo", agentWinningMap);
	}

	@SuppressWarnings("unchecked")
	
	public List<Map<String, Object>> queryAgentWinningDay(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryAgentWinningDay", map);
	}

	@SuppressWarnings("unchecked")
	
	public List<Map<String, Object>> queryAgentWinningInfo() {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryAgentWinningInfo");
	}

	@SuppressWarnings("unchecked")
	
	public List<Map<String, String>> queryAgentLogin(String agentId) {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryAgentLogin",agentId);
	}

	
	public void addAgentLoginInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("userInfo.addAgentLoginInfo", map);
		
	}

	
	public void addAgentPassInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("userInfo.addAgentPassInfo", param);
	}

	
	public void deleteAgentPass(Map<String,Object> map) {
		this.getSqlMapClientTemplate().delete("userInfo.deleteAgentPass", map);
	}

	
	public Integer queryAgentPassTotalNum(String user_id) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("userInfo.queryAgentPassTotalNum",user_id);
	}

	
	public List<Map<String, String>> queryLinkInfoList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryLinkInfoList",paramMap);
	}

	
	public Integer queryPassNumByCard(Map<String, String> param) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("userInfo.queryPassNumByCard",param);
	}

	
	public void updateAgentPassBuyNum(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("userInfo.updateAgentPassBuyNum", param);
	}

	
	public void deleteAgentPassInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().delete("userInfo.deleteAgentPassInfo", param);
	}

	
	public void updateAgentLoginInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("userInfo.updateAgentLoginInfo", map);
	}

	
	public List<Map<String, String>> queryAgentLevelAndId() {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryAgentLevelAndId");
	}

	
	public List<Map<String, Object>> queryMayBeWinOrderInfo(
			Map<String, Object> goldMap) {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryMayBeWinOrderInfo", goldMap);
	}


	public void addUserInfo(LoginUserInfo loginUser) {
		this.getSqlMapClientTemplate().insert("userInfo.addUserInfo", loginUser);
	}


	public LoginUserInfo queryUserInfo(String linkPhone) {
		return (LoginUserInfo) this.getSqlMapClientTemplate().queryForObject("userInfo.queryUserInfo", linkPhone);
	}


	public void updateUserPwd(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("userInfo.updateUserPwd", map);
	}


	public Map<String, String> queryUserAllInfo(String userId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("userInfo.queryUserAllInfo", userId);
	}


	public void updateUserInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("userInfo.updateUserInfo", map);
	}


	public List<Map<String, String>> queryPassengerList(
			Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryPassengerList", paramMap);
	}


	public void addPassenger(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("userInfo.addPassenger", paramMap);
	}


	public void deletePassenger(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().delete("userInfo.deletePassenger", paramMap);
	}


	public Map<String, String> queryPassenger(Map<String, String> paramMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("userInfo.queryPassenger", paramMap);
	}


	public void updatePassenger(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("userInfo.updatePassenger", paramMap);
	}


	
	
	public void addAddress(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("userInfo.addAddress", paramMap);
	}


	public void deleteAddress(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().delete("userInfo.deleteAddress", paramMap);
	}


	public Map<String, String> queryAddress(Map<String, String> paramMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("userInfo.queryAddress", paramMap);
	}


	public List<Map<String, String>> queryAddressList(
			Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("userInfo.queryAddressList", paramMap);
	}


	public int queryAddressListCount(Map<String, String> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("userInfo.queryAddressListCount", paramMap);
	}


	public void updateAddress(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("userInfo.updateAddress", paramMap);
	}


}
