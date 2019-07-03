package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ReserveBuyTicketDao;

@Repository("reserveBuyTicketDao")
public class ReserveBuyTicketDaoImpl extends BaseDao implements ReserveBuyTicketDao{

	
	public void insertIntoNotify(List<Map<String, String>> list) {
		this.getSqlMapClientTemplate().insert("reserve.insertIntoNotify",list);
	}

	
	public Map<String, Object> queryOrderInfo(String orderId) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("reserve.queryOrderInfo", orderId);
	}

	
	public List<String> selectListFromNotify() {
		return this.getSqlMapClientTemplate().queryForList("reserve.selectListFromNotify");
	}

	
	public List<Map<String, String>> selectReserveMap(String beginTime) {
		return this.getSqlMapClientTemplate().queryForList("reserve.selectReserveMap",beginTime);
	}

	
	public List<Map<String, Object>> selectReserveNotifyList(String allMinutes) {
		return this.getSqlMapClientTemplate().queryForList("reserve.selectReserveNotifyList",allMinutes);
	}

	
	public void updateNotifyInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("reserve.updateNotifyInfo",param);
	}

	
	public void updateOrderStatusBegin(String order_id) {
		this.getSqlMapClientTemplate().update("reserve.updateOrderStatusBegin",order_id);
	}

	
	public void updateNotifyStatus(String order_id) {
		this.getSqlMapClientTemplate().update("reserve.updateNotifyStatus",order_id);
	}

	
	public void updateNotifyOutTicketStaus(String order_id) {
		this.getSqlMapClientTemplate().update("reserve.updateNotifyOutTicketStaus",order_id);
	}
	
}
