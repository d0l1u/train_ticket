package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Worker;

public interface CommonService {

	String querySysSettingByKey(String string);

	Worker queryRandomWorker(Map<String,Object> map);
	
	Integer queryHeyanWorkNum(Integer wokr_type);
	
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
	
	void updateElongRefundOrderStatus(Map<String,String> map);

	int updateElongSysNoticeStaus(Map<String, String> paramElong);
	//更新支付宝账户余额
	void updateBalance(Map<String,String> map);
} 
