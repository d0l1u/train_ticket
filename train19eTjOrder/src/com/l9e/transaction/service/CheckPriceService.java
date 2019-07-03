package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;
@Deprecated
public interface CheckPriceService {

	void addAlipayInfoList(List<Map<String, Object>> paramList);

	void addAlipayBalance(Map<String, Object> paMap);

}
