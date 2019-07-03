package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface Tj_hc_outTicketSblService {

	List<Map<String, String>> queryOutTicketSblBefore(String createTime);

	List<Map<String, String>> queryOutTicketSblAfter(String createTime);

	int queryCount(Map paramMap);

	void addToTj_hc_outTicketSbl(Map paramMap);

	void updateTj_hc_outTicketSbl(Map paramMap);

}
