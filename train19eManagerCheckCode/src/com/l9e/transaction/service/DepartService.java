package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface DepartService {

	int queryDepartCodeCountToday(String department);

	int queryDepartCodeToday(String department);

	int queryDepartCodeFailToday(String department);

	int queryDepartCodeOvertimeToday(String department);

	Map<String, Object> queryDepartCodeCountWeek(String department);

	Map<String, Object> queryDepartCodeCountMonth(String department);

	Map<String, Object> queryDepartCodeCount(String department);

	Map<String, Object> queryDepartCodeCountYesterday(String department);

	int queryDepartCurrentNameCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryAdminCurrentName(Map<String, Object> paramMap);

	int queryAdminCodeTodayCount(String optRen);

	int queryAdminCodeTodaySuccess(String optRen);

	int queryAdminCodeTodayFail(String optRen);

	int queryAdminCodeTodayOvertime(String optRen);

	int queryPictureCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryPictureList(Map<String, Object> paramMap);

	int queryDayPageCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryDayPageList(Map<String, Object> paramMap);

	int queryByAdminPageCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryAdminCodeList(Map<String, Object> paramMap);

	void addAdmin(Map<String, String> addMap);

	int queryDepartAdminUserListPageCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryDepartAdminUserList(
			Map<String, Object> paramMap);

	void deleteDepartAdmin(String optRen);

	String queryDepartment(String optRen);

}
