package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ElongExcelService {

	int getExcelListCount(HashMap<String, Object> paramMap);

	List<Map<String,Object>> getExcelList(HashMap<String, Object> paramMap);
	
	void insertIntoExcel(HashMap<String, Object> paramMap);

	void deleteExcelById(HttpServletRequest request, HttpServletResponse response, String excelId);

	List<Map<String, String>> queryTcBookList(HashMap<String, Object> map);

	List<Map<String, String>> queryTcRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryTcAlterList(HashMap<String, Object> map);

	List<Map<String, String>> getElongAlterCpList(String id);

	List<Map<String, String>> queryAllBook(HashMap<String, Object> map);

	void tcAddCheck(Map<String, Object> paramMap);

	int getTcCheckCount(HashMap<String, Object> paramMap);

	List<Map<String, Object>> getTcCheckList(HashMap<String, Object> paramMap);

	List<String> getFileNameById(Map<String, String> excel);

	void addFileNameLogs(Map<String, String> excel);
}
