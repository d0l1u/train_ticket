package com.l9e.transaction.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OrderInfoToCpStatDao;
import com.l9e.transaction.service.OrderInfoToCpStatService;
@Service
public class OrderInfoToCpStatServiceImpl implements OrderInfoToCpStatService{
	@Resource
	private OrderInfoToCpStatDao orderInfoToCpStatDao;
	public int queryPre_day_order_count() {
		return orderInfoToCpStatDao.queryPre_day_order_count();
	}

	public int queryPre_day_out_ticket_defeated() {
		return orderInfoToCpStatDao.queryPre_day_out_ticket_defeated();
	}

	public int queryPre_day_out_ticket_succeed() {
		return orderInfoToCpStatDao.queryPre_day_out_ticket_succeed();
	}

	public int queryPre_bx_count() {
		return orderInfoToCpStatDao.queryPre_bx_count();
	}
	
	public double queryPre_bx_countMoney() {
		return orderInfoToCpStatDao.queryPre_bx_countMoney();
	}
	
	public double queryPre_defeated_money() {
		return orderInfoToCpStatDao.queryPre_defeated_money();
	}

	public double queryPre_succeed_money() {
		return orderInfoToCpStatDao.queryPre_succeed_money();
	}

	public void addOrderInfoToCpStat(Map<String, Object> map) {
		orderInfoToCpStatDao.addOrderInfoToCpStat(map);
	}

	public int queryPre_preparative_count() {
		return orderInfoToCpStatDao.queryPre_preparative_count();
	}

	public int queryPre_refund_count() {
		return orderInfoToCpStatDao.queryPre_refund_count();
	}

	public int queryPre_ticket_count() {
		return orderInfoToCpStatDao.queryPre_ticket_count();
	}
	
	public int queryPre_pay_defeated() {
		return orderInfoToCpStatDao.queryPre_pay_defeated();
	}

	////////////////////////

	public int query_Hc_StatInfo() {
		return orderInfoToCpStatDao.query_Hc_StatInfo();
	}

	public int queryPre_activeAgent() {
		return orderInfoToCpStatDao.queryPre_activeAgent();
	}





}
