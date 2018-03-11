package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AcquireVo;

public interface PsOrderDao {
	int queryPsOrderCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryPsOrderList(Map<String, Object> paramMap);

	Map<String, String> queryPsOrderInfo(String orderId);

	List<Map<String, Object>> queryPsOrderInfoCp(String orderId);

	List<Map<String, Object>> queryHistroyByOrderId(String orderId);

	String queryDbOrderStatus(String orderId);

	void updatePsOrderStatus(AcquireVo acquire);

	void updatePsOrderCpInfo(Map<String, String> cpInfo);

	void updateOrderWithCpNotify(Map<String, String> hc);

	void updateCpOrderWithCpNotify(Map<String, String> cpMap);

	void updateBxStatusNotSend(String string);

	Map<String, String> queryOrderDiffer(String string);

	void addRefundStream(Map<String, String> refundMap);

	void updateOrderRefundTotal(Map<String, String> tkMoneyMap);

	Map<String, String> queryPsOrderInfoPssm(String orderId);

}
