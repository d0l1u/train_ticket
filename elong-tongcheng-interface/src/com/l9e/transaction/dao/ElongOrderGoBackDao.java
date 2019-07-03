package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface ElongOrderGoBackDao {

	List<Map<String,Object>> getOrderGoBackIdList();

	void insertAllElongCpInfo(String orderId);

	void insertElongOrderInfo(String orderId);

	void deleteAllElongNotifyInfo(String orderId);

	void deleteAllElongOrderInfo(String orderId);

	void deleteAllElongCpInfo(String orderId);

	void deleteAllSysCpInfo(String orderId);

	void deleteAllSysNotifyInfo(String orderId);

	void deleteAllSysOrderInfo(String orderId);
	
	void updateGoBackNotify(Map<String,String> param);

	void updateGoBackOrderInfoStatusFail(String orderId);

}
