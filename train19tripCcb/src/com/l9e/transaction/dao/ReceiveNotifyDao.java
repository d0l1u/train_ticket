package com.l9e.transaction.dao;

import java.util.Map;

public interface ReceiveNotifyDao {

	void updateOrderWithCpNotify(Map<String, String> paraMap);

	void updateCpOrderWithCpNotify(Map<String, String> cpMap);

}
