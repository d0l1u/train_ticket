package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;

public interface LoginService {

	Map<String, String> queryLogin_UserInfo(Map<String, String> query_Map);

	List<Map<String, Object>> queryLoginUserList(Map<String, Object> query_Map);

	int queryLoginUserListCount(Map<String, Object> query_Map);

	List<Map<String, Object>> queryLoginUserListOnly(Map<String, Object> query_MapOnly);

	void deleteUserAccount(String user_id);

	void updateUserIsOpen(Map<String, Object> update_Map);

	Map<String, String> queryUpdateUserInfo(Map<String, Object> query_Map);

	void updateUserInfo(Map<String, String> update_Map);

	void addUser(Map<String, String> add_Map);

	String queryUsername(String user_name);

	void addLogin_Log(Map<String, String> add_Map);

	void updateLoginTimeAndIp(Map<String, String> add_Map);

	int queryLoginLogsListCount(Map<String, Object> query_Map);

	List<Map<String, String>> queryLoginLogsList(Map<String, Object> query_Map);

	/**
	 * 获取省份
	 * @return
	 */
	public List<AreaVo> getProvince();

	int queryUserLoginTotal(String user_id);

	int queryRealname(String real_name);
	
}
