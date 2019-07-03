package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface PhoneCheckService {

	List<Map<String, String>> queryAccountInfo(Map<String, Object> querymap);

	boolean addAccountCheckInfo(Map<String, String> map);

	boolean updateAccountCheckInfo(Map<String, String> map);

	List<Map<String, String>> getPhoneMsgList();

	boolean updateRefundStatus(Map<String, String> map);

}
