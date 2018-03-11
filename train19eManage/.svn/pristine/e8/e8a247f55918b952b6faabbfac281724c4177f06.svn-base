package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CheckPriceService {

	int queryCheckPriceCounts(Map<String, Object> paramMap);

	int queryAlipayBalanceCounts(Map<String, Object> paramMap);

	List<Map<String, String>> queryCheckPriceList(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryAlipayBalanceList(Map<String, Object> paramMap);

	int queryAlipayCounts(Map<String, Object> pMap);

	void addAlipayInfo(Map<String, Object> paramMap);

	List<Map<String, String>> queryCheckPriceExcel(HashMap<String, Object> map);

	List<Map<String, String>> queryAlipayBalanceExcel(HashMap<String, Object> map);

	int updateSeqById(Map<String, String> map);

	List<Map<String, String>> queryAlipayExcel(HashMap<String, Object> map);

	int updateOrderInfo(Map<String, Object> paramMap);

	List<Map<String, String>> queryNeedRefund(HashMap<String, Object> map);

	int updateRefundPrice(Map<String, String> m);

	String queryPriceByOrderId(Map<String, String> m);
	
	public List<String> selectAlipay(Map<String, String> m);
	
	int deleteTicketById(Map<String, Object> map);
}
