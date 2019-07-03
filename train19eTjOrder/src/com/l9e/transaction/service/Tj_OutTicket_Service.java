package com.l9e.transaction.service;

import java.util.Map;

public interface Tj_OutTicket_Service {
	
	int queryBook_xl(Map<String, Object> map);
	int queryShoudan(Map<String, Object> map);
	int queryFenfa(Map<String, Object> map);
	int queryBook(Map<String, Object> map);
	int queryNotify(Map<String, Object> map);
	int queryPay_xl(Map<String, Object> map);
	int queryNotifyPay(Map<String, Object> map);
	int queryOutticket_xl(Map<String, Object> map);
	
	void addToTj_OutTicket(Map<String, Object> map);
	
	void updateToTj_OutTicket(Map<String, Object> map);
	
	int query19eTodayCount(Map<String, Object> map);
	
	int queryShoudanTuniu(Map<String, Object> paramMap);
	int queryBook_xlTuniu(Map<String, Object> paramMap);
	int queryNotifyTuniu(Map<String, Object> paramMap);
	int queryNotifyPayTuniu(Map<String, Object> paramMap);
	int queryOutticket_xlTuniu(Map<String, Object> paramMap);
}
