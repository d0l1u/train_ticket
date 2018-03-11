package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface Tj_UserInfoDao {

	int queryTable_count();

	List<Map<String, String>> queryAllArea_nameAndArea_no();

	List<Map<String,String>> queryAll_date();

	int queryUser_total(Map<String, String> query_Map);

	int queryActiveUserNow(Map<String, String> query_Map);

	int queryUser_total_Add(Map<String, String> query_Map);

	int queryActiveUserAll(Map<String, String> query_Map);

	int queryActiveUser_Add(Map<String, String> query_Map);

	void addTj_User(Map<String, Object> add_Map);

}
