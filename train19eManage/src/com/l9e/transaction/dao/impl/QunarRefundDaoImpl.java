package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QunarRefundDao;

@Repository("qunarRefundDao")
public class QunarRefundDaoImpl extends BaseDao implements QunarRefundDao{

	//获得订单信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRefundTicketInfo(String orderId) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("qunarRefund.queryRefundTicketorderInfo", orderId);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getRefundTicketList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarRefund.queryRefundTicketList", map);
	}

	public int getRefundTicketListCounts(HashMap<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarRefund.queryRefundTicketCounts", map);
	}

	//获得该订单的车票信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId) {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarRefund.queryRefundTicketcpInfo", orderId);
	}

	//更新日志信息
	public void insertLog(HashMap<String, Object> map){
		this.getSqlMapClientTemplate().insert("qunarRefund.insertLog", map);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> queryLog(String order_id) {
		List<HashMap<String, String>> history = this.getSqlMapClientTemplate().queryForList("qunarRefund.queryLog", order_id);
//		System.out.println("日志条数为："+ history.size());
//		for(HashMap<String, String> map:history) {
//			System.out.println("日志信息" + map);
//		}
		return history;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String, String> map){
		return this.getSqlMapClientTemplate().queryForList("qunarRefund.queryTicketInfo", map);
	}

	public void updateOrder(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("qunarRefund.updateOrder", map);
	}

	public void updateRefund(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("qunarRefund.updateRefund", map);
	}

	//修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("qunarRefund.updateRefundOpt", map);
	}

	public void updateRefuse(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("qunarRefund.updateRefuse", map);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRefundInfo(Map<String, String> map) {
		return  (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("qunarRefund.queryRefundTicketInfo1", map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("qunarRefund.updateOrderstatusToRobotGai", updateMap);
	}

	@Override
	public void updateAlertRefund(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("qunarRefund.updateAlertRefund", map);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryLianchengOrder_id(String orderid) {
		return this.getSqlMapClientTemplate().queryForList("qunarRefund.queryLianchengOrder_id", orderid);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryOrderCpId(String orderid) {
		return this.getSqlMapClientTemplate().queryForList("qunarRefund.queryOrderCpId", orderid);
	}
	
	@Override
	public void updateNotify_status(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("qunarRefund.updateNotify_status", updateMap);
	}
}
