package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface PictureDao {

	int queryPictureCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryPictureList(Map<String, Object> paramMap);

	int queryCodeHourCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryCodeHourList(Map<String, Object> paramMap);

	int queryUserHourCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryUserHourList(Map<String, Object> paramMap);

	List<Map<String, String>> queryCodeHourEveryDayList(
			String paramMap);

	List<Map<String, String>> queryUserHourEveryDayList(String string);

	List<Map<String, String>> queryFailCodeHourEveryDayList(String string);

	List<Map<String, String>> queryFailCodeHourList(Map<String, Object> paramMap);

	List<Map<String, String>> querySuccessCodeHourEveryDayList(String string);

	List<Map<String, String>> querySuccessCodeHourList(
			Map<String, Object> paramMap);
	
}
