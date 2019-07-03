package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface ReserveBuyTicketService {

	List<Map<String, String>> selectReserveMap(String beginTime);

	void insertIntoNotify(List<Map<String, String>> list);

	List<String> selectListFromNotify();

	Map<String, Object> queryOrderInfo(String orderId);

	void updateOrderStatusBegin(String orderId);

	void updateNotifyInfo(Map<String, String> param);

	List<Map<String, Object>> selectReserveNotifyList(String allMinutes);

	void updateNotifyStatus(String orderId);

	void updateNotifyOutTicketStaus(String string);

}
