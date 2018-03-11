package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.LoginDao;
import com.l9e.transaction.vo.AreaVo;
@Repository("loginDao")
public class LoginDaoImpl extends BaseDao implements LoginDao{

	@SuppressWarnings("unchecked")
	public Map<String, String> queryLogin_UserInfo(Map<String, String> query_Map) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("login.queryLogin_UserInfo",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLoginUserList(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("login.queryLoginUserList",query_Map);
	}

	public int queryLoginUserListCount(Map<String, Object> query_Map) {
		return getTotalRows("login.queryLoginUserListCount",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLoginUserListOnly(Map<String, Object> query_Maponly) {
		return this.getSqlMapClientTemplate().queryForList("login.queryLoginUserListOnly",query_Maponly);
	}

	public void deleteUserAccount(String user_id) {
		this.getSqlMapClientTemplate().delete("login.deleteUserAccount",user_id);
	}

	public void updateUserIsOpen(Map<String, Object> update_Map) {
		this.getSqlMapClientTemplate().update("login.updateUserIsOpen",update_Map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryUpdateUserInfo(Map<String, Object> query_Map) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("login.queryUpdateUserInfo",query_Map);
	}

	public void updateUserInfo(Map<String, String> update_Map) {
		this.getSqlMapClientTemplate().update("login.updateUserInfo",update_Map);
	}

	public void addUser(Map<String, String> add_Map) {
		this.getSqlMapClientTemplate().insert("login.addUser",add_Map);
	}

	public String queryUsername(String user_name) {
		return (String)this.getSqlMapClientTemplate().queryForObject("login.queryUsername",user_name);
	}

	public void addLogin_Log(Map<String, String> add_Map) {
		this.getSqlMapClientTemplate().insert("login.addLogin_Log",add_Map);
	}

	public void updateLoginTimeAndIp(Map<String, String> add_Map) {
		this.getSqlMapClientTemplate().update("login.updateLoginTimeAndIp",add_Map);
	}

	public int queryLoginLogsListCount(Map<String, Object> query_Map) {
		return this.getTotalRows("login.queryLoginLogsListCount", query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryLoginLogsList(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("login.queryLoginLogsList",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("login.getProvince");
	}

	public int queryUserLoginTotal(String user_id) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryUserLoginTotal", user_id);
	}

	public int queryRealname(String real_name) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryRealname",real_name);
	}


}
