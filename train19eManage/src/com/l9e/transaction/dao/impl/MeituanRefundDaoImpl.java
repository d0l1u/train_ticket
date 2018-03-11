package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.MeituanRefundDao;

@Repository("meituanRefundDao")
public class MeituanRefundDaoImpl extends BaseDao implements MeituanRefundDao {

	//获得订单信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRefundTicketInfo(String orderId) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryRefundTicketorderInfo", orderId);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getRefundTicketList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"meituanRefund.queryRefundTicketList", map);
	}

	public int getRefundTicketListCounts(HashMap<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"meituanRefund.queryRefundTicketCounts", map);
	}

	//获得该订单的车票信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId) {
		return this.getSqlMapClientTemplate().queryForList(
				"meituanRefund.queryRefundTicketcpInfo", orderId);
	}

	//更新日志信息
	public void insertLog(HashMap<String, Object> map){
		this.getSqlMapClientTemplate().insert("meituanRefund.insertLog", map);
	}
	public void addTicket_log(Map<String, String> log_Map) {
		this.getSqlMapClientTemplate().update("meituanRefund.addTicket_log", log_Map);
	}
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> queryLog(String order_id) {
		List<HashMap<String, String>> history = this.getSqlMapClientTemplate().queryForList("meituanRefund.queryLog", order_id);
//		System.out.println("日志条数为："+ history.size());
//		for(HashMap<String, String> map:history) {
//			System.out.println("日志信息" + map);
//		}
		return history;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicketInfo(Map<String, String> map){
		return this.getSqlMapClientTemplate().queryForList("meituanRefund.queryTicketInfo", map);
	}

	public void updateOrder(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("meituanRefund.updateOrder", map);
	}

	public void updateRefund(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("meituanRefund.updateRefund", map);
	}

	//修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("meituanRefund.updateRefundOpt", map);
	}

	public void updateRefuse(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("meituanRefund.updateRefuse", map);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRefundInfo(Map<String, String> map) {
		return  (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryRefundTicketInfo1", map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("meituanRefund.updateOrderstatusToRobotGai", updateMap);
	}

	@Override
	public void updateAlertRefund(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().update("meituanRefund.updateAlertRefund", map);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryLianchengOrder_id(String orderid) {
		return this.getSqlMapClientTemplate().queryForList("meituanRefund.queryLianchengOrder_id", orderid);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryOrderCpId(String orderid) {
		return this.getSqlMapClientTemplate().queryForList("meituanRefund.queryOrderCpId", orderid);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRefundTicketAdd(
			Map<String, Object> mapAdd) {
		return (List<Map<String, Object>>) this.getSqlMapClientTemplate().insert("meituanRefund.queryRefundTicketAdd",mapAdd);
	}
	
	public String queryRefundTicketOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryRefundTicketOrderId",order_id);
	}
	
	@Override
	public String queryRefundTicketCpId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryRefundTicketCpId", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicket(Map<String, Object> paramMap){
		return this.getSqlMapClientTemplate().queryForList("meituanRefund.queryRefundTicket", paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryCpidIsRefund", paramMap);
	}

	@Override
	public int queryCpidIsRefundNum(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryCpidIsRefundNum", paramMap);
	}
	
	@Override
	public String queryStatusByOrderId(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryStatusByOrderId", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap) {
		return (Map<String,String>) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryMoneyByCpId", paramMap);
	}
	//重新通知
	public void updateNotify_Again(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("meituanRefund.updateNotify_Again", paramMap);
	}
	
	@Override
	public String querySumRefundMoney(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("meituanRefund.querySumRefundMoney",paramMap);
	}
	
	@Override
	public String querySumYhRefundMoney(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("meituanRefund.querySumYhRefundMoney",paramMap);
	}
	
	@Override
	public String querySumXxRefundMoney(Map<String, Object> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("meituanRefund.querySumXxRefundMoney",paramMap);
	}
	@SuppressWarnings("unchecked")
	public  List<Map<String, String>> queryBuymoneyAndTicketpaymoney(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("meituanRefund.queryBuyMoney_TicketPayMoney",paramMap);
	}
	
	@Override
	public String queryRefundMoney(String stream_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryRefundMoney",stream_id);
	}
	
	//批量车站退票
	public void addMeituanStation(Map<String, Object> paramMap){
		this.getSqlMapClientTemplate().insert("meituanRefund.addMeituanStation", paramMap);
	}

	@Override
	public void deleteOrder(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("meituanRefund.deleteOrder", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryChangeRefundTicket(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("meituanRefund.queryChangeRefundTicket", paramMap);
	}

	@Override
	public String queryChangeCpPayMoney(Map<String, Object> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("meituanRefund.queryChangeCpPayMoney", paramMap);
	}

}
