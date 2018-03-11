package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MeituanRefundService {

	public int getRefundTicketListCounts(HashMap<String, Object> map);
	
	public List<Map<String, String>> getRefundTicketList(HashMap<String, Object> map);
	
	//获得订单信息
	public HashMap<String, String> getRefundTicketInfo(String orderId);
	
	//获得该订单的车票信息
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId);
	
	public List<Map<String, String>> queryRefundTicketInfo(Map<String, String> map);

	public HashMap<String, String> getRefundInfo(Map<String, String> map);
	

	public void updateRefund(HashMap<String, Object> map);
	

	public void updateRefuse(HashMap<String, Object> map);
	
	public void updateOrder(HashMap<String, Object> map);
	
	//查询日志信息
	public List<HashMap<String, String>> queryLog(String order_id);
	
	//更新日志信息
	public void insertLog(HashMap<String, Object> map);
	
	//修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map);

	public void updateOrderstatusToRobotGai(Map<String, String> updateMap);

	public void updateAlertRefund(HashMap<String, Object> paramMap);

	public List<String> queryOrderCpId(String orderid);

	//生成线下退款
	List<Map<String, Object>> queryRefundTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd);
	
	String queryRefundTicketOrderId(String order_id);
	
	String queryRefundTicketCpId(Map<String, Object> paramMap);
	
	public List<Map<String, String>> queryRefundTicket(Map<String, Object> paramMap);
	
	public String queryCpidIsRefund(Map<String, Object> paramMap);
	
	public int queryCpidIsRefundNum(Map<String, Object> paramMap);
	
	public String queryStatusByOrderId(Map<String, Object> paramMap);
	
	public Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap);
	
	//重新通知
	public void updateNotify_Again(Map<String,String> paramMap);
	
	Map<String, String> queryRefundMoney(Map<String, Object> paramMap,String Stream_id);
	
	//批量车站退票
	public void addMeituanStation(Map<String, Object> paramMap,Map<String, String> logMap);
	

	public void deleteOrder(Map<String, String> map);
	//美团改签退票
	public List<Map<String, String>> queryChangeRefundTicket(
			Map<String, Object> paramMap);
}
