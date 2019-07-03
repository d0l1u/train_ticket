package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface AppUserDao {

	List<Map<String, Object>> queryAppUserList(Map<String, Object> paramMap);

	int queryAppUserListCount(Map<String, Object> paramMap);

	Integer queryLinkerNum(String userId);

	Integer queryRefereeAccountNum(String userId);

	Map<String, String> queryAppUserInfo(String userId);

	List<Map<String, Object>> queryAppUserLinkerList(
			Map<String, Object> paramMap);

	int queryAppUserLinkerListCount(String userId);

	void updateAppUser(Map<String, String> paramMap);

	void deleteAppUser(String userId);

	void updateAppUserStop(Map<String, String> paramMap);

}
