package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface Tj_hc_outTicketSblDao {

	void addToTj_hc_outTicketSbl(Map paramMap);

	int queryCount(Map paramMap);

	List<Map<String, String>> queryOutTicketSblAfter(String createTime);

	List<Map<String, String>> queryOutTicketSblBefore(String createTime);

	void updateTj_hc_outTicketSbl(Map paramMap);

}
