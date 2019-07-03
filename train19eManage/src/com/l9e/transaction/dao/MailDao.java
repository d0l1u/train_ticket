package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface MailDao {

	List<Map<String, String>> queryMailList(Map<String, Object> paramMap);

	List<Map<String, String>> queryMailExcel(Map<String, Object> paramMap);
	
	int queryMailListCount(Map<String, Object> paramMap);

	void addMail(Map<String, String> addMap);

	Map<String, String> queryMailModify(String mailId);

	void updateMail(Map<String, String> updateMap);

	String queryMailAddress(String content);

}
