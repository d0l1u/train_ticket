package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface CheckPriceDao {


	void addAlipayInfoList(List<Map<String, Object>> paramList);

	void addAlipayBalance(Map<String, Object> paMap);

}
