package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.DBStudentInfo;

public interface OrderDao {
	void addOrderInfo(DBOrderInfo orderInfo);

	void addPassengerInfo(DBPassengerInfo p);

	public DBOrderInfo queryOrderInfo(String orderId);

	public List<DBPassengerInfo> queryOrderCpsInfo(String orderId);

	public String queryOutTicketOrderStatusByOrderId(String orderId);

	/** 更新订单状态 */
	int updateOrderStatus(Map<String, String> map);

	void updateOrderNoticeStatus(Map<String, String> orderNotice);

	public Map<String, Object> queryOutTicketOrderInfo(String orderId);

	void updateOrderBookNoticeStatus(Map<String, String> orderNotice);

	public String queryOrderStatusByOrderId(String order_id);

	void updateCpOrderInfo(Map<String, String> cpInfo);

	void updateOrderInfo(DBOrderInfo orderInfo);

	String queryRefundStatus(Map<String, String> paramMap);

	void insertRefundOrder(Map<String, String> paramMap);

	Map<String, Object> queryOutTicketCpInfo(String cpId);

	Map<String, Object> queryAllChannelNotify(String order_id);

	void updateOrderPayMoney(Map<String, String> paramMap);

	public String querySeatNo(Map<String, String> cpInfo);

	void addStudentInfo(DBStudentInfo s);

	List<Map<String, String>> queryPassengerList(Map<String, String> param);

	List<Map<String, String>> queryPassersList(HashMap<String, Object> map);

	int queryPassersCount();

	void addVerifyTime(Map<String, String> timeMap);

	String queryVerfiyTimeRatio();

	void addPhone(Map<String, String> paramMap);
	public DBPassengerInfo queryCpById(String cpId);
}
