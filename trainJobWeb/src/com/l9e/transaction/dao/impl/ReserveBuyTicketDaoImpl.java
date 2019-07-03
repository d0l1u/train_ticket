package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ReserveBuyTicketDao;

@Repository("reserveBuyTicketDao")
public class ReserveBuyTicketDaoImpl extends BaseDao implements ReserveBuyTicketDao{

	@Override
	public void insertIntoNotify(List<Map<String, String>> list) {
		this.getSqlMapClientTemplate().insert("reserve.insertIntoNotify",list);
	}

	@Override
	public Map<String, Object> queryOrderInfo(String orderId) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("reserve.queryOrderInfo", orderId);
	}

	@Override
	public List<String> selectListFromNotify() {
		return this.getSqlMapClientTemplate().queryForList("reserve.selectListFromNotify");
	}

	@Override
	public List<Map<String, String>> selectReserveMap(String beginTime) {
		return this.getSqlMapClientTemplate().queryForList("reserve.selectReserveMap",beginTime);
	}

	@Override
	public List<Map<String, Object>> selectReserveNotifyList(String allMinutes) {
		return this.getSqlMapClientTemplate().queryForList("reserve.selectReserveNotifyList",allMinutes);
	}

	@Override
	public void updateNotifyInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("reserve.updateNotifyInfo",param);
	}

	@Override
	public void updateOrderStatusBegin(String order_id) {
		this.getSqlMapClientTemplate().update("reserve.updateOrderStatusBegin",order_id);
	}

	@Override
	public void updateNotifyStatus(String order_id) {
		this.getSqlMapClientTemplate().update("reserve.updateNotifyStatus",order_id);
	}

	@Override
	public void updateNotifyOutTicketStaus(String order_id) {
		this.getSqlMapClientTemplate().update("reserve.updateNotifyOutTicketStaus",order_id);
	}
	
}
