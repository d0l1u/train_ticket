package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface ElongOrderGoBackService {

	List<Map<String,Object>> getOrderGoBackIdList();

	void deleteAllOrderInfo(String orderId);

	void updateGoBackNotify(Map<String,String> param);

	void doLogAndNotifyUpdate(String string, int notifyNum,String order_id,String message);

}
