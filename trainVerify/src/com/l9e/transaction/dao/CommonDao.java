package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Worker;

public interface CommonDao {

	String querySysSettingByKey(String key);

	Integer queryHeyanWorkNum(Integer worker_type);
	
	Worker queryRandomWorker(Map<String,Object> map);
	
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
	
	Map<String, String> queryQunarSystemSetting(String setting_name);
	
	Map<String, String> queryElongSystemSetting(String setting_name);
	//更新线上支付宝账号余额
	int updateBalance(Map<String, String> param);
}
