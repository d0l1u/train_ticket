package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OrderInfoToCpStatDao;
import com.l9e.transaction.dao.OrderInfoToCpStatOneDao;
import com.l9e.transaction.service.OrderInfoToCpStatOneService;
@Service
public class OrderInfoToCpStatOneServiceImpl implements OrderInfoToCpStatOneService{
	@Resource
	private OrderInfoToCpStatOneDao orderInfoToCpStatOneDao;
	public int query_Hc_StatInfo() {
		return orderInfoToCpStatOneDao.query_Hc_StatInfo();
	}
	public List<Map<String,Object>> queryDate() {
		return orderInfoToCpStatOneDao.queryDate();
	}
	public List<Map<String, Object>> createDateList(Map<String, Object> map) {
		return orderInfoToCpStatOneDao.createDateList(map);
	}
	public void addOrderInfoToCpStat(Map<String, Object> map) {
		orderInfoToCpStatOneDao.addOrderInfoToCpStat(map);
		
	}
	public int queryPre_bx_count(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_bx_count(date_time);
	}
	public double queryPre_bx_countMoney(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_bx_countMoney(date_time);
	}
	public int queryPre_day_order_count(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_day_order_count(date_time);
	}
	public int queryPre_day_out_ticket_defeated(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_day_out_ticket_defeated(date_time);
	}
	public int queryPre_day_out_ticket_succeed(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_day_out_ticket_succeed(date_time);
	}
	public double queryPre_defeated_money(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_defeated_money(date_time);
	}
	public int queryPre_preparative_count(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_preparative_count(date_time);
	}
	public int queryPre_refund_count(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_refund_count(date_time);
	}
	public double queryPre_succeed_money(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_succeed_money(date_time);
	}
	public int queryPre_ticket_count(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_ticket_count(date_time);
	}
	public int queryPre_pay_defeated(String date_time) {
		return orderInfoToCpStatOneDao.queryPre_pay_defeated(date_time);
	}
	public int queryPre_activeAgent(String dateTime) {
		return orderInfoToCpStatOneDao.queryPre_activeAgent(dateTime);
	}

}
