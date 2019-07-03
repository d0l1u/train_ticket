package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface RegisterDao {

	List<Map<String, Object>> queryRegisterList(Map<String, Object> paramMap);

	List<Map<String, String>> queryRegisterExcel(Map<String, Object> paramMap);
	
	int queryRegisterListCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryRegisterInfo(Map<String, Object> paramMap);

	void updateRegisterFail(Map<String, Object> updateMap);

	void updateRegisterSuccess(Map<String, Object> updateMap);

	void updateRegisterCheck(Map<String, Object> updateMap);

	Map<String, String> queryRegister(String registId);

	void addAccountInfo(Map<String, String> paramMap);

	void addRegister(Map<String, String> addMap);

	String queryRegisterIdcard(String content);

	void update12306Register(Map<String, String> paramMap);

	void updateRegisterSuccessGrade(Map<String, Object> updateGrade);

}
