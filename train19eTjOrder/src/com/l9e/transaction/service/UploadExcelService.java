package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UploadExcelService {

	int getExcelListCount(HashMap<String, Object> paramMap);

	List<Map<String,Object>> getExcelList(HashMap<String, Object> paramMap);
	
	void insertIntoExcel(HashMap<String, Object> paramMap);

	void deleteExcelById(HttpServletRequest request, HttpServletResponse response, String excelId);

	List<Map<String, String>> queryTcBookList(HashMap<String, Object> map);

	List<Map<String, String>> queryTcRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryTcAlterList(HashMap<String, Object> map);

	List<Map<String, String>> queryAlterList(HashMap<String, Object> map);

	List<Map<String, String>> getElongAlterCpList(String id);

	List<Map<String, String>> queryAllBook(HashMap<String, Object> map);

	List<Map<String, String>> queryElongBookCp(HashMap<String, Object> map);

	List<Map<String, String>> queryElongRefundTicket(HashMap<String, Object> map);

	String queryElongRefundTicketName(String string);

	List<Map<String, String>> queryElongRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryQunarRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryAppRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryExtRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryInnerRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryl9eRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryTestList(HashMap<String, Object> map);

	List<Map<String, String>> queryTestMap(String string);

	List<Map<String, String>> queryGtRefundList(HashMap<String, Object> map);

	void tcAddTcCheck(Map<String, String> addMap);

	List<Map<String, String>> queryMtRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryXcRefundList(HashMap<String, Object> map);

	List<Map<String, String>> queryMtBookList(HashMap<String, Object> map);

	List<Map<String, String>> queryGtBookList(HashMap<String, Object> map);

	List<Map<String, String>> queryGtRefundNotifyList(HashMap<String, Object> map);
	List<Map<String, String>> queryGtgjAlterList(HashMap<String, Object> map);
	
	List<Map<String, String>> queryXcBookList(HashMap<String, Object> map);

	List<Map<String, String>> queryXcRefundNotifyList(HashMap<String, Object> map);

	List<Map<String, String>> queryTuniuRefundList(HashMap<String, Object> map);

	void addCheckOrderInfo(Map<String, String> m);

	List<Map<String, String>> queryTuniuBookList(HashMap<String, Object> map);

	List<Map<String, String>> queryTuniuRefundTicket(HashMap<String, Object> map);

	List<Map<String, String>> queryCheckTcAlter(HashMap<String, Object> map);
}
