package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.LoginDao;
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.vo.AreaVo;
@Service("loginService")
public class LoginServiceImpl implements LoginService{
	@Resource
	private LoginDao loginDao;

	public Map<String, String> queryLogin_UserInfo(Map<String, String> query_Map) {
		return loginDao.queryLogin_UserInfo(query_Map);
	}

	public List<Map<String, Object>> queryLoginUserList(
			Map<String, Object> query_Map) {
		return loginDao.queryLoginUserList(query_Map);
	}

	public int queryLoginUserListCount(Map<String, Object> query_Map) {
		return loginDao.queryLoginUserListCount(query_Map);
	}

	public List<Map<String, Object>> queryLoginUserListOnly(Map<String, Object> query_MapOnly) {
		return loginDao.queryLoginUserListOnly(query_MapOnly);
	}

	public void deleteUserAccount(String user_id) {
		loginDao.deleteUserAccount(user_id);
	}

	public void updateUserIsOpen(Map<String, Object> update_Map) {
		loginDao.updateUserIsOpen(update_Map);
	}

	public Map<String, String> queryUpdateUserInfo(Map<String, Object> query_Map) {
		return loginDao.queryUpdateUserInfo(query_Map);
	}

	public void updateUserInfo(Map<String, String> update_Map) {
		loginDao.updateUserInfo(update_Map);
	}

	public void addUser(Map<String, String> add_Map) {
		loginDao.addUser(add_Map);
	}

	public String queryUsername(String user_name) {
		return loginDao.queryUsername(user_name);
	}

	public void addLogin_Log(Map<String, String> add_Map) {
		loginDao.addLogin_Log(add_Map);
	}

	public void updateLoginTimeAndIp(Map<String, String> add_Map) {
		loginDao.updateLoginTimeAndIp(add_Map);
	}

	public int queryLoginLogsListCount(Map<String, Object> query_Map) {
		return loginDao.queryLoginLogsListCount(query_Map);
	}

	public List<Map<String, String>> queryLoginLogsList(
			Map<String, Object> query_Map) {
		return loginDao.queryLoginLogsList(query_Map);
	}

	public List<AreaVo> getProvince() {
		return loginDao.getProvince();
	}

	public int queryUserLoginTotal(String user_id) {
		return loginDao.queryUserLoginTotal(user_id);
	}

	public int queryRealname(String real_name) {
		return loginDao.queryRealname(real_name);
	}



}
