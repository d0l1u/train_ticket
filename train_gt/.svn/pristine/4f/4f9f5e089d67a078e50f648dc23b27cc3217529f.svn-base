package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface RefundDao {

	int queryRefundStreamContainCp(Map<String, String> refundMap);

	Map<String, String> queryPassengerInfoByCpId(String string);

	void deleteRefundStreamOnRefuse(Map<String, String> refundMap);

	void addRefundStream(Map<String, String> refundMap);

	Map<String, String> querySpecTimeBeforeFrom(String order_id);

	void addRefundNotify(Map<String, String> notifyMap);

	Map<String, String> queryRefundTotalInOrder(String order_id);

	int queryRefundLeftCount(String order_id);

	List<Map<String, String>> queryRefundResultWaitList();

	List<Map<String, String>> queryRefundStreamListBySeq(
			Map<String, String> paramMap);

	List<Map<String, String>> queryCpListByOrderId(String order_id);

	void updateRefundNotfiyFinish(Map<String, String> map);

	int updateRefundNotfiyBegin(Map<String, String> beginMap);

	int queryRefundCountByMerchantSeq(Map<String, String> orderMap);

	String queryOrderStatusById(Map<String, String> orderMap);
	
	void updateOrderRefundStatus(Map<String,String> param);
	
	List<Map<String, String>> queryEopRefundResultWaitList();
	
	void updateEopRefundNotfiyFinish(Map<String, String> map);

	int updateEopRefundNotfiyBegin(Map<String, String> beginMap);

	void updateRefundStreamEopRefundSeq(Map<String, String> param);
	
	void updateEopRefundStreamInfo(Map<String, String> map);
	
	void updateRefundNotifyRestart(Map<String,String> map);
	
	int queryRefundNotifyNum(Map<String,String> map);
	
	String queryRefundStatusByCpId(Map<String,String> map);
	
	void updateRefundStreamTo33(Map<String, String> updateMap);
	/**
	 * 查询改签退票的车票信息
	 * @param param
	 * @return
	 */
	Map<String, String> queryChangeRefundCpOrderInfo(Map<String, String> param);
}
