package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CheckPriceDao {

	void addAlipayInfo(Map<String, Object> paramMap);

	int queryAlipayCounts(Map<String, Object> pMap);

	int queryOutTicketCounts(Map<String, Object> paramMap);

	int queryAlipayBalanceCounts(Map<String, Object> paramMap);

	int queryRefundTicketCounts(Map<String, Object> paramMap);

	List<Map<String, String>> queryOutTicketList(Map<String, Object> paramMap);

	List<Map<String, String>> queryAlipayBalanceList(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryRefundTicketList(Map<String, Object> paramMap);

	List<Map<String, String>> queryOutTicketExcel(HashMap<String, Object> paramMap);
	
	List<Map<String, String>> queryOutAlipayBalanceExcel(HashMap<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketExcel(HashMap<String, Object> paramMap);

	int updateSeqById(Map<String, String> map);

	List<Map<String, String>> queryAlipayExcel(HashMap<String, Object> map);

	int updateOrderInfo(Map<String, Object> paramMap);

	List<Map<String, String>> queryNeedRefund(HashMap<String, Object> map);

	int updateRefundPrice(Map<String, String> m);

	String queryl9eRefund(Map<String, String> m);

	String queryQunarRefund(Map<String, String> m);

	String queryElongRefund(Map<String, String> m);

	String queryMtRefund(Map<String, String> m);

	String queryTuniuRefund(Map<String, String> m);

	String queryGtRefund(Map<String, String> m);

	String queryXcRefund(Map<String, String> m);

	String queryInnerRefund(Map<String, String> m);

	String queryExtRefund(Map<String, String> m);

	String queryAppRefund(Map<String, String> m);

	List<String> selectAlipay(Map<String, String> m);
	
	int deleteTicketById(Map<String, Object> map);

}
