package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface UserDao {

	Map<String, String> queryUserInfo(String optRen);

	void updateUser(Map<String, String> map);

	int queryDayUserCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryDayUserList(Map<String, Object> paramMap);

	void updateLogined(Map<String, String> map);

}
