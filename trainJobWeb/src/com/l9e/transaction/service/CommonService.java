package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Worker;

public interface CommonService {

	String querySysSettingByKey(String string);

	Worker queryRandomWorker();
	
	List<Map<String,String>> queryWaitNotify();

	void updateRobotStartNotify(Map<String,String> map);
	
	List<Map<String,String>> queryLockOrderList();
	
	void updateCpOrderStatus(Map<String,String> map);
	
	void addCpOrderHistory(Map<String,String> map);
	
	void updateCpOrderNotify(Map<String,String> map);
	
	List<Map<String,String>> queryLockAlterOrderList();
	
	void updateQunarRefundOrderStatus(Map<String,String> map);
	
	void update19eRefundOrderStatus(Map<String,String> map);
	
	List<String> queryPinganMessageList();
	
	List<Map<String,String>> queryAccountSeatList(Map<String, String> param);
	
	void updateAccountStatusFree(String acc_id);

	int updateQunarSysNoticeStaus(Map<String, String> param);
	
	List<Map<String,String>> queryExtMerchantList();
	
	List<String> queryLockBookOrder(Map<String, String> param);
	
	void updateCpOrderinfoNotify(Map<String, String> param);
	
	void insertCpHistory(Map<String, String> param);
	
	int updateElongSysNoticeStaus(Map<String, String> paramElong);
	
	void addCpOrderRefundHistory(Map<String, String> param);
	
	List<Map<String,String>> queryInnerLockOrderList();
	
	Map<String,String> queryCpOrderList(Map<String,String> map);
	
	void updateLockCpOrderinfoNotify(Map<String,String> map);
	
	List<Map<String,String>> query19eEopLockOrderList();
	
	List<Map<String,String>> query19eLockOrderList();
	
	List<Map<String,String>> queryElongLockOrderList();
	
	List<Map<String,String>> queryQunarLockOrderList();
	
	List<Map<String,String>> queryExtLockOrderList();
	
	void updateEopLockOrder(Map<String,String> map);
	
	List<Map<String,String>> queryChinaCitysInfo(Map<String, Integer> paramMap);

	String queryAccountIdByOrderId(String order_id);
} 
