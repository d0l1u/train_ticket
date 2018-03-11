package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RemindService {
	
	Map<String, String> queryCounts();
	
	int queryAdminCurrentNameCount();

	int queryCodeCountToday();

	int queryCodeToday();

	int queryUncode(String channel);

	String codeQunarType();
	
	Map<String, String> queryRobotCounts();

	int queryWaitCodeQueenCount(String department);

	int queryAdminCurrentNameCount2(Map<String, Object> paramMap1);

	List<Map<String, String>> queryAccountList();

	List<Map<String, String>> queryAccountMarginList(
			HashMap<String, Integer> map);

	List<Map<String, String>> queryAccountContactList(
			HashMap<String, Integer> map);

	int queryOrderNumber();

	int queryRobotNumber(String string);

	List<Map<String, String>> queryZhifubaoMoney(String name);
}
