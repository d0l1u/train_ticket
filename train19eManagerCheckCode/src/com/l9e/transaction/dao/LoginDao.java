package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;


public interface LoginDao {

	Map<String, Object> queryLogin_UserInfo(Map<String, String> query_Map);

	void updateLoginTimeAndIp(Map<String, String> add_Map);

	int queryCodeCountToday(Map<String,Object>paramMap);

	int queryCodeToday(Map<String,Object>paramMap);

	int queryCode(int i);

	Map<String,Object> queryCodeCount(Map<String,Object>paramMap);

	Map<String,Object> queryCodeCountMonth(Map<String,Object>paramMap);

	Map<String,Object> queryCodeCountWeek(Map<String,Object>paramMap);

	int queryCodeMonth(int i);

	int queryCodeWeek(int i);

	void addAdmin(Map<String, String> addMap);

	int queryRealname(String realName);

	String queryUsername(String userName);

	List<String> queryDate_List();

	int queryDayPageCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryDayPageList(Map<String, Object> paramMap);

	int queryByAdminPageCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryAdminCodeList(Map<String, Object> paramMap);

	int queryAdminCodeTodayCount(String optRen);

	int queryAdminCodeTodayFail(String optRen);

	int queryAdminCodeTodayOvertime(String optRen);

	int queryAdminCodeTodaySuccess(String optRen);

	Map<String, Object> queryAdminCodeInfoSum(String optRen);

	Map<String, Object> queryAdminCodeMonth(String optRen);

	Map<String, Object> queryAdminCodeWeek(String optRen);

	List<Map<String, Object>> queryAdminCurrentName(Map<String, Object> paramMap);

	int queryAdminCodeCurrentCount(Map<String, Object> paramMap);

	int queryAdminCurrentNameCount(Map<String,Object>paramMap);

	String queryAdminDepartment(String username);

	int queryCodeFailToday(Map<String,Object>paramMap);

	int queryCodeOvertimeToday(Map<String,Object>paramMap);

	String queryOptname(String optRen);

	Map<String, Object> queryCodeCountYesterday(Map<String,Object>paramMap);

	Map<String, Object> queryAdminCodeYesterday(String optRen);

	void deleteAdmin(String optRen);

	List<Map<String, Object>> queryAdminUserList(Map<String, Object> paramMap);

	int queryByAdminUserListPageCount(Map<String, Object> paramMap);

	Integer queryAdminCodeMonthMoney(String username);

	int querySuccessPreCount(String optRen);

	int queryWaitCodeCount();

	int queryCodeRateCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryCodeRateList(Map<String, Object> paramMap);

	Map<String, String> querySystemInfo();

	int queryWaitCodeQueenCount(String string);

	void updateSystemInfo(Map<String, Object> paramMap);

	int queryAdminCurrentOtherCount(Map<String, Object> paramMap1);

	
}
