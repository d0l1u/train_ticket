package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ReserveBuyTicketDao;
import com.l9e.transaction.service.ReserveBuyTicketService;
@Service("reserveBuyTicketService")
public class ReserveBuyTicketServiceImpl implements ReserveBuyTicketService{

	@Resource
	private ReserveBuyTicketDao reserveBuyTicketDao;
	
	@Override
	public List<Map<String, String>> selectReserveMap(String beginTime) {
		return reserveBuyTicketDao.selectReserveMap(beginTime);
	}

	@Override
	public void insertIntoNotify(List<Map<String, String>> list) {
		reserveBuyTicketDao.insertIntoNotify(list);
	}

	@Override
	public List<String> selectListFromNotify() {
		return reserveBuyTicketDao.selectListFromNotify();
	}

	@Override
	public Map<String, Object> queryOrderInfo(String orderId) {
		return reserveBuyTicketDao.queryOrderInfo(orderId);
	}

	@Override
	public void updateNotifyInfo(Map<String, String> param) {
		reserveBuyTicketDao.updateNotifyInfo(param);
	}

	@Override
	public void updateOrderStatusBegin(String orderId) {
		 reserveBuyTicketDao.updateOrderStatusBegin(orderId);
	}

	@Override
	public List<Map<String, Object>> selectReserveNotifyList(String allMinutes) {
		// TODO Auto-generated method stub
		return reserveBuyTicketDao.selectReserveNotifyList(allMinutes);
	}

	@Override
	public void updateNotifyStatus(String order_id) {
		reserveBuyTicketDao.updateNotifyStatus(order_id);
	}

	@Override
	public void updateNotifyOutTicketStaus(String order_id) {
		reserveBuyTicketDao.updateNotifyOutTicketStaus(order_id);
	}

}
