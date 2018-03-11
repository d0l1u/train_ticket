package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface ReceiveNotifyDao {

	void updateOrderWithCpNotify(Map<String, String> paraMap);

	void updateCpOrderWithCpNotify(Map<String, String> cpMap);

	void addOrderResultNotify(Map<String, String> paramMap);
	
	List<Map<String,String>> findOrderResultNotify();
	
	void updateOrderResultNotify(Map<String, String> map);
	
	void updateOrderResultNotifyStartNum(String order_id);
	
	void addPayResultNotify(Map<String,String> map);
	
	void updatePayResultNotifyNum(Map<String, String> param);
	
	void updatePayResultNotify(String order_id);
	
	List<Map<String,String>> findPayResultNotify();
	
	int queryEopRefundNotifyAlterNum(String refund_seq);
	
	void updateEopRefundResult(Map<String,String> paramMap);
	
	void addEopRefundNotify(Map<String,String> paramMap);
	
	void updateOrderReturnStatus(Map<String,String> map);
	
	List<Map<String,String>> findOrderBookNotify();
	
	void updateOrderBookStatus(Map<String,String> map);
	
	void updateOrderBookNotifyStartNum(String order_id);
	
	void updatePayResultNotifyStatus(Map<String,String> map);
	
	void updateOrderBookNotifyFinish(String order_id);
	
	void addEopAndPayNotify(Map<String,String> map);
	
	List<Map<String, String>> queryEopAndPayNotify();
	
	void updateEopAndPayNotifyInfo(Map<String,String> param);
	
	void updateEopAndPayNotifyFinish(Map<String,String> param);
	
	void updateEopAndPayNotifyNums(Map<String,String> param);
	
	void insertPayReturnNotify(Map<String,String> paramMap);
	
	void updateOrderPayNotifyFinish(Map<String,String> paramMap);
	
	List<Map<String, String>> findOrderPayNotify();
	
	void updatePayReturnNotifyNums(String order_id);
	
	Integer queryOrderResultNotifyStartNum(String order_id);
	
	String queryMerchantIdByOrderId(String orderId);
}
