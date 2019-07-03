package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface RefundService {

	List<Map<String, String>> queryCanRefundStreamList();

	void updateCPAlterInfo(Map<String, String> map);

	void updateRefundInfo(Map<String, String> map);

	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);

	Map<String, String> queryAccountOrderInfo(Map<String, String> param);

	int updateOrderRefundStatus(Map<String, String> map);

	void updateCPRefundInfo(Map<String, String> map);

	Map<String, String> queryChangeRefundCpOrderInfo(Map<String, String> param);

}
