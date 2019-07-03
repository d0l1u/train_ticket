package com.l9e.transaction.dao;

import java.util.List;

public interface SendAgainDao {

	List<String> queryNeedSendAgainOrder(int num);

	void updateToSendAgainByOrderId(String orderId);

	List<String> queryNeedToManualOrder(int num);

	void updateToManualByOrderId(String orderId);

	void updateCpQueueByOrderId(String orderId);

}
