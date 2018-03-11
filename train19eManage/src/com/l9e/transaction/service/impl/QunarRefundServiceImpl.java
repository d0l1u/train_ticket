package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import com.l9e.transaction.dao.QunarRefundDao;
import com.l9e.transaction.service.QunarRefundService;

@Service("qunarRefundService")
public class QunarRefundServiceImpl implements QunarRefundService{

	@Resource
	private QunarRefundDao qunarRefundDao;
	

	public HashMap<String, String> getRefundInfo(Map<String, String> map) {
		return qunarRefundDao.getRefundInfo(map);
	}
	
	// 获得订单信息
	public HashMap<String, String> getRefundTicketInfo(String orderId) {
		return qunarRefundDao.getRefundTicketInfo(orderId);
	}

	public List<Map<String, String>> getRefundTicketList(
			HashMap<String, Object> map) {
		return qunarRefundDao.getRefundTicketList(map);
	}

	public int getRefundTicketListCounts(HashMap<String, Object> map) {
		return qunarRefundDao.getRefundTicketListCounts(map);
	}
	
	// 获得该订单的车票信息
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId) {
		return qunarRefundDao.getRefundTicketcpInfo(orderId);
	}

	// 更新日志信息
	public void insertLog(HashMap<String, Object> map) {
		qunarRefundDao.insertLog(map);

	}

	public List<HashMap<String, String>> queryLog(String order_id) {
		return qunarRefundDao.queryLog(order_id);
	}

	public List<Map<String, String>> queryRefundTicketInfo(
			Map<String, String> map) {
		return qunarRefundDao.queryRefundTicketInfo(map);
	}

	public void updateOrder(HashMap<String, Object> map) {
		qunarRefundDao.updateOrder(map);
	}

	public void updateRefund(HashMap<String, Object> map) {
		updateOrder(map);
		qunarRefundDao.updateRefund(map);
	}

	// 修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map) {
		qunarRefundDao.updateRefundOpt(map);
	}

	public void updateRefuse(HashMap<String, Object> map) {
		updateOrder(map);
		qunarRefundDao.updateRefuse(map);
		
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		qunarRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public void updateAlertRefund(HashMap<String, Object> paramMap) {
		qunarRefundDao.updateAlertRefund(paramMap);//更新改签后的车次信息
	}

	@Override
	public List<String> queryLianchengOrder_id(String orderid) {
		return qunarRefundDao.queryLianchengOrder_id(orderid);
	}

	@Override
	public List<String> queryOrderCpId(String orderid) {
		return qunarRefundDao.queryOrderCpId(orderid);
	}
	//更新通知状态
	public void updateNotify_status(Map<String,String> paramMap){
		qunarRefundDao.updateNotify_status(paramMap);
	}
}
