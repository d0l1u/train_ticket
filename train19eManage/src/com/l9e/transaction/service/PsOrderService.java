package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AcquireVo;

public interface PsOrderService {

	int queryPsOrderCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryPsOrderList(Map<String, Object> paramMap);

	Map<String, String> queryPsOrderInfo(String orderId);

	List<Map<String, Object>> queryPsOrderInfoCp(String orderId);

	List<Map<String, Object>> queryHistroyByOrderId(String orderId);

	String queryDbOrderStatus(String orderId);

	void updatePsOrderStatus(AcquireVo acquire);

	void updatePsOrderCpList(List<Map<String, String>> cpList);

	int updateOrderWithCpNotify(Map<String, String> hc,
			List<Map<String, String>> cpList, Map<String, String> failRefundMap);

	Map<String, String> queryPsOrderInfoPssm(String orderId);

}
