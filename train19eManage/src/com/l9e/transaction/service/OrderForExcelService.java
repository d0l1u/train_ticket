package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderForExcelService {
	
  	public List<Map<String, String>> getOrderForExcelList(HashMap<String, Object> map);

  	public List<Map<String, String>> getOrderForExcelRefundList(HashMap<String, Object> paramMap);

  	public List<Map<String, Object>> queryBxInfo(String order_id);

  	public List<Map<String, String>> getOrderForExcelOpterList(HashMap<String, Object> paramMap);

  	public List<Map<String, String>> getOrderForExcelProvinceStatList(String create_time);

  	public Map<String, String> queryProvinceTicketAndMoney(HashMap<String, Object> paramMap);
	
  	public List<Map<String, String>> queryInnerBookList(HashMap<String, Object> paramMap);

  	public List<Map<String, String>> getOrderForExcelInnerRefundList(HashMap<String, Object> paramMap);
	
  	public List<Map<String, String>> getOrderForExcelExtRefundList(HashMap<String, Object> paramMap);

  	public List<Map<String, String>> queryExtBookList(HashMap<String, Object> paramMap);

  	public List<Map<String, String>> queryAppBookList(HashMap<String, Object> paramMap);

  	public List<Map<String, String>> getOrderForExcelAppRefundList(HashMap<String, Object> paramMap);

  	public List<Map<String, String>> getOrderForExcelAllInsuranceList(HashMap<String, Object> paramMap);

  	public List<Map<String, String>> getBalancAccountExcelList(HashMap<String, Object> paramMap);

	public List<Map<String, String>> getElongAlterExcelList(HashMap<String, Object> paramMap);

	public List<Map<String, String>> getElongAlterCpList(String change_id);

	public List<Map<String, String>> getCtripExcelList(Map<String, Object> paramMap);

	public List<Map<String, String>> queryGtBookList(HashMap<String, Object> paramMap);

	public List<Map<String, String>> queryGtRefundList(HashMap<String, Object> paramMap);

	public List<Map<String, String>> queryXcBookList(HashMap<String, Object> paramMap);

	public List<Map<String, String>> queryXcRefundList(HashMap<String, Object> paramMap);

	public List<Map<String, String>> queryTuniuBookExcelList(
			Map<String, Object> paramMap);

}
