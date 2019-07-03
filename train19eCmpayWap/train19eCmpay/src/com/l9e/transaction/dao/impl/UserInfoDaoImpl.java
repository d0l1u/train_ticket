package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.UserInfoDao;

@Repository("userInfoDao")
public class UserInfoDaoImpl extends BaseDao implements UserInfoDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUserIdsCardInfo(Map<String,String> map) {
		// TODO Auto-generated method stub
		return  this.getSqlMapClientTemplate().queryForList("userIdsCardInfo.queryUserIdsCardInfo", map);
	}

	public void addUserinfoCheck(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("userIdsCardInfo.addUserInfoCheck", map);
	}

	public int updateUserInfoCheck(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("userIdsCardInfo.updateUserInfoCheck", map);
	}
}
