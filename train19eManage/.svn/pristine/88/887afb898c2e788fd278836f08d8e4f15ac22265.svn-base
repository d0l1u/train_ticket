package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface TicketPriceDao {

	int queryRefundTicketCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryTicketPriceList(Map<String, Object> paramMap);

	Map<String, String> queryTicketPriceModify(Map<String, String> updateMap);

	void updateTicketPrice(Map<String, String> updateMap);

	int queryTicketPriceCheci(Map<String, Object> paramMap);

	void addTicketPrice(Map<String, String> addMap);

	void addTicletPriceLogs(Map<String, String> logMap);
	
	int queryTicketPriceLogCount();

	void deleteTicketPrice(Map<String, String> deleteMap);

	List<Map<String, Object>> queryTicketPriceLogList(Map<String, Object> paramMap);
}
