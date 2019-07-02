package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface RefundDao {

	List<Map<String, String>> queryCanRefundStreamList();

	void updateCPAlterInfo(Map<String, String> map);

	void updateRefundInfo(Map<String, String> map);

	Map<String, Object> queryAccountOrderInfo(Map<String, String> param);

	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);

	void updateCPRefundInfo(Map<String, String> map);

	int updateOrderRefundStatus(Map<String, String> map);
	/**
	 * 查询改签退票的车票信息
	 * @param param
	 * @return
	 */
	Map<String, String> queryChangeRefundCpOrderInfo(Map<String, String> param);
	


}
