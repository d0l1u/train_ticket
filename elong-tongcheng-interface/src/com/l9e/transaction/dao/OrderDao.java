package com.l9e.transaction.dao;

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
	/**更新订单状态*/
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
	void addStudentInfo(DBStudentInfo s);
	
	public String querySeatNo(Map<String, String> cpInfo);
	public List<String> queryChangeIdsByOrderId(String order_id);
	Map<String, String> queryUsedAccountInfo(Integer id);
	
	/**
	 * 查询订单中出发与到达车站的三字码 
	 * @param map
	 * @return 出发与到达车站三字码列表
	 */
	public List<DBOrderInfo> queryCity3CList(Map<String, String> map);
}
