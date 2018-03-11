package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MeituanRefundDao {

	//获得订单信息
	public HashMap<String, String> getRefundTicketInfo(String orderId);

	public List<Map<String, String>> getRefundTicketList(HashMap<String, Object> map);

	public int getRefundTicketListCounts(HashMap<String, Object> map);
	
	//获得该订单的车票信息
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId);

	//更新日志信息
	public void insertLog(HashMap<String, Object> map);

	void addTicket_log(Map<String, String> log_Map);
	//查询日志信息
	public List<HashMap<String, String>> queryLog(String order_id);

	public List<Map<String, String>> queryRefundTicketInfo(Map<String, String> map);
	
	public HashMap<String, String> getRefundInfo(Map<String, String> map);

	//修改orderinfo订单信息
	public void updateOrder(HashMap<String, Object> map);

	public void updateRefund(HashMap<String, Object> map);

	//修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map);

	//拒绝逄1�7款时修改状�1�7�1�7
	public void updateRefuse(HashMap<String, Object> map);

	public void updateOrderstatusToRobotGai(Map<String, String> updateMap);

	public void updateAlertRefund(HashMap<String, Object> map);

	public List<String> queryLianchengOrder_id(String orderid);

	public List<String> queryOrderCpId(String orderid);

	List<Map<String, Object>> queryRefundTicketAdd(
			Map<String, Object> mapAdd);
	
	String queryRefundTicketOrderId(String order_id);
	
	String queryRefundTicketCpId(Map<String, Object> paramMap);

	public List<Map<String, String>> queryRefundTicket(Map<String, Object> paramMap);
	
	String queryCpidIsRefund(Map<String, Object> paramMap);
	
	int queryCpidIsRefundNum(Map<String, Object> paramMap);
	
	public String queryStatusByOrderId(Map<String, Object> paramMap);
	
	public Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap);
	//重新通知
	public void updateNotify_Again(Map<String,String> paramMap);
	
	public List<Map<String, String>> queryBuymoneyAndTicketpaymoney(Map<String, Object> paramMap);
	
	String querySumRefundMoney(Map<String, Object> paramMap);
	
	String querySumYhRefundMoney(Map<String, Object> paramMap);
	String querySumXxRefundMoney(Map<String, Object> paramMap);
	
	String queryRefundMoney(String Stream_id);
	
	//批量车站退票
	public void addMeituanStation(Map<String, Object> paramMap);
	
	public void deleteOrder(Map<String, String> map);
	//美团改签退票
	public List<Map<String, String>> queryChangeRefundTicket(Map<String, Object> paramMap);
	String queryChangeCpPayMoney(Map<String, Object> paramMap);
}
