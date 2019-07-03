package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface PhoneCheckDao {
	
	List<Map<String, String>> queryAccountInfo(Map<String, Object> querymap);

	void addAccountCheckInfo(Map<String, String> map);

	void updateAccountCheckInfo(Map<String, String> map);

	List<Map<String, String>> getPhoneMsgList();

	int updateRefundStatus(Map<String, String> map);

	int queryAccountNum(Map<String, String> map);

	void addAccountCheckRefundInfo(Map<String, String> map);

}
