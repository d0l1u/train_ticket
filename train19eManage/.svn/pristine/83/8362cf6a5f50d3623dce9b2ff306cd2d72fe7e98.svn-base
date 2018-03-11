package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;



public interface BalanceAccountDao {

	String queryOrderId(String sh_order_id);

	void insertBalanceAccount(List<Map<String, String>> list);

	int queryBalanceAccountListCount(Map<String, Object> paramMap);

	int updateOrderId(Map<String, String> paramMap);

	void insertBalanceAccountOne(Map<String, String> values);

	String queryOrderIdByPaySeq(String stringCellValue);

	String queryOrderIdByOrderInfo(Map<String, String> param);
	
	int queryFileCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryFileList(Map<String, Object> paramMap);
	
	void insertFile(Map<String, String> paramMap);
	
	String queryFilepath(String id);

	void deleteFile(String id);

	List<Map<String, String>> queryOrderList(Map<String, String> param);

	int updateBalancAccount(Map<String, String> param);

	int selectUpdateCount(Map<String, String> param);

	List<Map<String, Object>> queryAppRefund(Map<String,String>param);

	List<Map<String, Object>> queryInnerRefund(Map<String,String>param);

	List<Map<String, Object>> queryElongRefund(Map<String,String>param);

	List<Map<String, Object>> queryExtRefund(Map<String,String>param);

	List<Map<String,Object>> queryHcRefund(Map<String,String>param);

	List<Map<String, Object>> queryQunarRefund(Map<String,String>param);

	String queryRefundMoney(String orderId);

	List<Map<String, String>> queryBalancAccountList(
			Map<String, Object> paramMap);

	void updateRefundResult(Map<String, String> updateParam);

}
