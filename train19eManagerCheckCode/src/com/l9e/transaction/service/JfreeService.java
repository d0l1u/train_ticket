package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface JfreeService {

	int query15dayUserCodeCount(String optRen);

	List<Map<String, Object>> query15dayUserCode(String optRen);

	List<Map<String, String>> query15dayCodePicture();

	List<Map<String, Object>> query3dayCodeSuccess(String date2);

	List<Map<String, Object>> queryTodayCodeSuccess(String today);

	int queryUserCodeSuccessCount(String optRen);

	List<Map<String, Object>> queryUserCodeSuccessList(Map<String,String> paramMap);

	List<Map<String, String>> query15dayDepartCodePicture(String department);

	List<Map<String, Object>> queryDepartCodeSuccessPicture(
			Map<String, String> paramMap);

}
