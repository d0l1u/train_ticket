package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public interface RefundService {

	boolean addUserRefundStream(List<Map<String, String>> refundList, JSONObject jsonRes);

	Map<String, String> querySpecTimeBeforeFrom(String order_id);

	List<Map<String, String>> queryRefundResultWaitList();

	List<Map<String, String>> queryRefundStreamListBySeq(
			Map<String, String> paramMap);

	List<Map<String, String>> queryCpListByOrderId(String order_id);

	void updateRefundNotfiyFinish(Map<String, String> map);

	int updateRefundNotfiyBegin(Map<String, String> beginMap);

	int queryRefundCountByMerchantSeq(Map<String, String> merchantMap);

	String queryOrderStatusById(Map<String, String> orderMap);

	void updateOrderRefundStatus(Map<String,String> param);
	
	List<Map<String, String>> queryEopRefundResultWaitList();
	
	void updateEopRefundNotfiyFinish(Map<String, String> map);

	int updateEopRefundNotfiyBegin(Map<String, String> beginMap);
	
	void updateEopRefundStreamInfo(Map<String, String> map);
	
	void updateRefundNotifyRestart(Map<String,String> map);
	
	String queryRefundStatusByCpId(Map<String,String> map);
	
	void updateRefundStreamTo33(Map<String, String> updateMap);
}
