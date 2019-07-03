package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface AppUserService {

	int queryAppUserListCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryAppUserList(Map<String, Object> paramMap);

	Integer queryRefereeAccountNum(String userId);

	Integer queryLinkerNum(String userId);

	Map<String, String> queryAppUserInfo(String userId);

	int queryAppUserLinkerListCount(String userId);

	List<Map<String, Object>> queryAppUserLinkerList(
			Map<String, Object> paramMap);

	void updateAppUser(Map<String, String> paramMap);

	void deleteAppUser(String userId);

	void updateAppUserStop(Map<String, String> paramMap);

}
