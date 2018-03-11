package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;


public interface BalanceAccountService {

	void uploadExcel(MultipartHttpServletRequest request,
			HttpServletResponse response);

	int queryBalanceAccountListCount(Map<String, Object> paramMap);


	void updateOrderId(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> paramMap);

	int queryFileCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryFileList(Map<String, Object> paramMap);
	
	String queryFilepath(String id);

	void deleteFile(String id);

	void queryOrderList(HttpServletRequest request,
			HttpServletResponse response);

	void updateBalancAccount(HttpServletRequest request,
			HttpServletResponse response);

	void matchRefund(HttpServletRequest request, HttpServletResponse response);
	
	List<Map<String, String>> queryBalancAccountList(Map<String, Object> paramMap);
	
	String addTxt(MultipartHttpServletRequest request,
			HttpServletResponse response)throws Exception ;
}
