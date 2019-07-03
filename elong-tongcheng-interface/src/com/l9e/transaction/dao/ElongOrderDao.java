package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ElongOrderInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.ElongPassengerInfo;

public interface ElongOrderDao {
	void insertElongOrder(Map<String, Object> orderInfo);
	List<ElongOrderInfoCp> queryOrderCpInfo(String orderId);
	void insertElongOrderLogs(ElongOrderLogsVo log);
	int queryOrderCount(String order_id);
	void updateOrderStatus(Map<String, Object> map);
	List<Map<String, Object>> querySendOrderCpsInfo(String order_id);
	void updateOrderInfo(Map<String, Object> map);
	void updateCpOrderInfo(Map<String, String> map);
	void updateRefundStream(Map<String, Object> map);
	Map<String, Object> queryOrderInfo(String order_id);
	String queryRefundStatus(Map<String, String> paramMap);
	void insertRefundOrder(Map<String, String> paramMap);
	Map<String, Object> queryrefundInfo(Map<String, String> params);
	void addOrderInfo(ElongOrderInfo orderInfo);
	void addPassengerInfo(ElongPassengerInfo p);
	void updateRefundStatus(Map<String, String> paramMap);
	void updateOrderNoticeStatus(Map<String, String> param);
	String queryCpPayMoney(Map<String, String> paramMap);
	List<Map<String, Object>> getOfflineRefundRefundList();
	void updateOfflineNoticeStatus(Map<String, String> map);
	void addOfflineRefund(Map<String, String> params);
	List<Map<String, Object>> getTcOfflineRefundRefundList();
	
	String queryChangeCpPayMoney(Map<String, String> paramMap);
	String queryChangeFromTime(Map<String, String> paramMap);
	String getFakerLogContentByOrderId(String orderId);
	/**
	 * 根据订单号查询未通过核验的操作日志
	 * @param orderId
	 * @return
	 */
	String queryUnVerLogContentByOrderId(String orderId);

	List<String> findUsedAccountLogContentByOrderId(String orderId);
	ElongOrderInfoCp queryElongCpInfoByCpId(String cp_id);
	Map<String, Object> queryAccountOrderInfo(Map<String, String> param);
}
