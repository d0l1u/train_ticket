package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface ReceiveNotifyService {

	int updateOrderWithCpNotify(Map<String, String> paraMap,
			List<Map<String, String>> cpMapList, Map<String, String> failRefundMap);

}
