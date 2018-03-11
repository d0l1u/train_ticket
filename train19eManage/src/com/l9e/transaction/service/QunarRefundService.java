package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface QunarRefundService {

	public int getRefundTicketListCounts(HashMap<String, Object> map);
	
	public List<Map<String, String>> getRefundTicketList(HashMap<String, Object> map);
	
	//获得订单信息
	public HashMap<String, String> getRefundTicketInfo(String orderId);
	
	//获得该订单的车票信息
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId);
	
	public List<Map<String, String>> queryRefundTicketInfo(Map<String, String> map);
	//获得逄1�7款信恄1�7
	public HashMap<String, String> getRefundInfo(Map<String, String> map);
	
	//逄1�7票时修改逄1�7票信恄1�7
	public void updateRefund(HashMap<String, Object> map);
	
	//拒绝逄1�7款时修改状�1�7�1�7
	public void updateRefuse(HashMap<String, Object> map);
	
	//修改orderinfo订单朄1�7后操作信恄1�7
	public void updateOrder(HashMap<String, Object> map);
	
	//查询日志信息
	public List<HashMap<String, String>> queryLog(String order_id);
	
	//更新日志信息
	public void insertLog(HashMap<String, Object> map);
	
	//修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map);

	public void updateOrderstatusToRobotGai(Map<String, String> updateMap);

	public void updateAlertRefund(HashMap<String, Object> paramMap);

	public List<String> queryLianchengOrder_id(String orderid);

	public List<String> queryOrderCpId(String orderid);
	
	//更新通知状态
	public void updateNotify_status(Map<String,String> paramMap);

}
