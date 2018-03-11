package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ElongRefundDao;
import com.l9e.transaction.service.ElongRefundService;

@Service("elongRefundService")
public class ElongRefundServiceImpl implements ElongRefundService {

	@Resource
	private ElongRefundDao elongRefundDao;
	

	public HashMap<String, String> getRefundInfo(Map<String, String> map) {
		return elongRefundDao.getRefundInfo(map);
	}
	
	// 获得订单信息
	public HashMap<String, String> getRefundTicketInfo(String orderId) {
		return elongRefundDao.getRefundTicketInfo(orderId);
	}

	public List<Map<String, String>> getRefundTicketList(
			HashMap<String, Object> map) {
		return elongRefundDao.getRefundTicketList(map);
	}

	public int getRefundTicketListCounts(HashMap<String, Object> map) {
		return elongRefundDao.getRefundTicketListCounts(map);
	}
	
	// 获得该订单的车票信息
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId) {
		return elongRefundDao.getRefundTicketcpInfo(orderId);
	}

	// 更新日志信息
	public void insertLog(HashMap<String, Object> map) {
		elongRefundDao.insertLog(map);

	}

	public List<HashMap<String, String>> queryLog(String order_id) {
		return elongRefundDao.queryLog(order_id);
	}

	public List<Map<String, String>> queryRefundTicketInfo(
			Map<String, String> map) {
		return elongRefundDao.queryRefundTicketInfo(map);
	}

	public void updateOrder(HashMap<String, Object> map) {
		elongRefundDao.updateOrder(map);
	}

	public void updateRefund(HashMap<String, Object> map) {
		updateOrder(map);
		elongRefundDao.updateRefund(map);
	}

	// 修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map) {
		elongRefundDao.updateRefundOpt(map);
	}

	public void updateRefuse(HashMap<String, Object> map) {
		updateOrder(map);
		elongRefundDao.updateRefuse(map);
		
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		elongRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public void updateAlertRefund(HashMap<String, Object> paramMap) {
		elongRefundDao.updateAlertRefund(paramMap);//更新改签后的车次信息
	}

	@Override
	public List<String> queryLianchengOrder_id(String orderid) {
		return elongRefundDao.queryLianchengOrder_id(orderid);
	}

	@Override
	public List<String> queryOrderCpId(String orderid) {
		return elongRefundDao.queryOrderCpId(orderid);
	}

	@Override
	public List<Map<String, Object>> queryRefundTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd) {
		elongRefundDao.addTicket_log(logMap);
		return elongRefundDao.queryRefundTicketAdd(mapAdd);
	}
	
	public String queryRefundTicketOrderId(String order_id) {
		return elongRefundDao.queryRefundTicketOrderId(order_id);
	}
	
	@Override
	public String queryRefundTicketCpId(Map<String, Object> paramMap) {
		return elongRefundDao.queryRefundTicketCpId(paramMap);
	}

	public List<Map<String, String>> queryRefundTicket(Map<String, Object> paramMap){
		return elongRefundDao.queryRefundTicket(paramMap);
	}
	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return elongRefundDao.queryCpidIsRefund(paramMap);
	}
	
	@Override
	public int queryCpidIsRefundNum(Map<String, Object> paramMap) {
		return elongRefundDao.queryCpidIsRefundNum(paramMap);
	}
	
	@Override
	public String queryStatusByOrderId(Map<String, Object> paramMap) {
		return elongRefundDao.queryStatusByOrderId(paramMap);
	}
	
	@Override
	public  Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap) {
		return elongRefundDao.queryMoneyByCpId(paramMap);
	}
	
	//重新通知
	public void updateNotify_Again(Map<String, String> paramMap) {
		elongRefundDao.updateNotify_Again(paramMap);
		
	}
	
	public Map<String, String> queryRefundMoney(Map<String, Object> paramMap,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = elongRefundDao.querySumRefundMoney(paramMap);
		String sumYhRefundMoney = elongRefundDao.querySumYhRefundMoney(paramMap);
		String sumXxRefundMoney = elongRefundDao.querySumXxRefundMoney(paramMap);
		if(!"55".equals(paramMap.get("refund_type"))){
			List<Map<String, String>> buy_moneyAndPaymoney = elongRefundDao.queryBuymoneyAndTicketpaymoney(paramMap);
			if(buy_moneyAndPaymoney.size() > 0) {
				money_Map.put("pay_money", buy_moneyAndPaymoney.get(0).get("ticket_pay_money"));
				money_Map.put("buy_money", buy_moneyAndPaymoney.get(0).get("ticket_buy_money"));
			}
		}else{
			List<Map<String, String>> changeMoney = elongRefundDao.queryChangeMoney(paramMap);
			if(changeMoney.size() > 0) {
				money_Map.put("pay_money", changeMoney.get(0).get("ticket_pay_money"));
				money_Map.put("buy_money", changeMoney.get(0).get("ticket_buy_money"));
			}
		}
		if(!stream_id.isEmpty()){
		String refund_money =elongRefundDao.queryRefundMoney(stream_id);
		money_Map.put("refund_money", refund_money);
		}
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("sumYhRefundMoney", sumYhRefundMoney);
		money_Map.put("sumXxRefundMoney", sumXxRefundMoney);
		return money_Map;
	}
	
	//批量车站退票
	public void addElongStation(Map<String, Object> paramMap,Map<String, String> logMap){
		elongRefundDao.addTicket_log(logMap);
		elongRefundDao.addElongStation(paramMap);
	}

	@Override
	public void deleteOrder(Map<String, String> map) {
		elongRefundDao.deleteOrder(map);
	}

	@Override
	public List<Map<String, String>> queryChangeRefundTicket(Map<String, Object> paramMap) {
		return elongRefundDao.queryChangeRefundTicket(paramMap);
	}

	@Override
	public String queryIsAlter(Map<String, Object> param) {
		return elongRefundDao.queryIsAlter(param);
	}

}
