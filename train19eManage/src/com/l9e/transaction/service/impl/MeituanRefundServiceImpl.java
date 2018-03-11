package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.MeituanRefundDao;
import com.l9e.transaction.service.MeituanRefundService;

@Service("meituanRefundService")
public class MeituanRefundServiceImpl implements MeituanRefundService {

	@Resource
	private MeituanRefundDao meituanRefundDao;
	

	public HashMap<String, String> getRefundInfo(Map<String, String> map) {
		return meituanRefundDao.getRefundInfo(map);
	}
	
	// 获得订单信息
	public HashMap<String, String> getRefundTicketInfo(String orderId) {
		return meituanRefundDao.getRefundTicketInfo(orderId);
	}

	public List<Map<String, String>> getRefundTicketList(
			HashMap<String, Object> map) {
		return meituanRefundDao.getRefundTicketList(map);
	}

	public int getRefundTicketListCounts(HashMap<String, Object> map) {
		return meituanRefundDao.getRefundTicketListCounts(map);
	}
	
	// 获得该订单的车票信息
	public List<HashMap<String, String>> getRefundTicketcpInfo(String orderId) {
		return meituanRefundDao.getRefundTicketcpInfo(orderId);
	}

	// 更新日志信息
	public void insertLog(HashMap<String, Object> map) {
		meituanRefundDao.insertLog(map);

	}

	public List<HashMap<String, String>> queryLog(String order_id) {
		return meituanRefundDao.queryLog(order_id);
	}

	public List<Map<String, String>> queryRefundTicketInfo(
			Map<String, String> map) {
		return meituanRefundDao.queryRefundTicketInfo(map);
	}

	public void updateOrder(HashMap<String, Object> map) {
		meituanRefundDao.updateOrder(map);
	}

	public void updateRefund(HashMap<String, Object> map) {
		updateOrder(map);
		meituanRefundDao.updateRefund(map);
	}

	// 修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map) {
		meituanRefundDao.updateRefundOpt(map);
	}

	public void updateRefuse(HashMap<String, Object> map) {
		updateOrder(map);
		meituanRefundDao.updateRefuse(map);
		
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		meituanRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public void updateAlertRefund(HashMap<String, Object> paramMap) {
		meituanRefundDao.updateAlertRefund(paramMap);//更新改签后的车次信息
	}

	@Override
	public List<String> queryOrderCpId(String orderid) {
		return meituanRefundDao.queryOrderCpId(orderid);
	}

	@Override
	public List<Map<String, Object>> queryRefundTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd) {
		meituanRefundDao.addTicket_log(logMap);
		return meituanRefundDao.queryRefundTicketAdd(mapAdd);
	}
	
	public String queryRefundTicketOrderId(String order_id) {
		return meituanRefundDao.queryRefundTicketOrderId(order_id);
	}
	
	@Override
	public String queryRefundTicketCpId(Map<String, Object> paramMap) {
		return meituanRefundDao.queryRefundTicketCpId(paramMap);
	}

	public List<Map<String, String>> queryRefundTicket(Map<String, Object> paramMap){
		return meituanRefundDao.queryRefundTicket(paramMap);
	}
	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return meituanRefundDao.queryCpidIsRefund(paramMap);
	}
	
	@Override
	public int queryCpidIsRefundNum(Map<String, Object> paramMap) {
		return meituanRefundDao.queryCpidIsRefundNum(paramMap);
	}
	
	@Override
	public String queryStatusByOrderId(Map<String, Object> paramMap) {
		return meituanRefundDao.queryStatusByOrderId(paramMap);
	}
	
	@Override
	public  Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap) {
		return meituanRefundDao.queryMoneyByCpId(paramMap);
	}
	
	//重新通知
	public void updateNotify_Again(Map<String, String> paramMap) {
		meituanRefundDao.updateNotify_Again(paramMap);
		
	}
	
	public Map<String, String> queryRefundMoney(Map<String, Object> paramMap,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = meituanRefundDao.querySumRefundMoney(paramMap);
		String sumYhRefundMoney = meituanRefundDao.querySumYhRefundMoney(paramMap);
		String sumXxRefundMoney = meituanRefundDao.querySumXxRefundMoney(paramMap);
		List<Map<String, String>> buy_moneyAndPaymoney = meituanRefundDao.queryBuymoneyAndTicketpaymoney(paramMap);
		if(!stream_id.isEmpty()){
		String refund_money =meituanRefundDao.queryRefundMoney(stream_id);
		money_Map.put("refund_money", refund_money);
		}
		money_Map.put("sumRefundMoney", sumRefundMoney);
		if(buy_moneyAndPaymoney.size() != 0) {
			
			money_Map.put("pay_money", buy_moneyAndPaymoney.get(0).get("ticket_pay_money"));
			money_Map.put("buy_money", buy_moneyAndPaymoney.get(0).get("ticket_buy_money"));
		}else{
			String  changeCpPayMoney = meituanRefundDao.queryChangeCpPayMoney(paramMap);
			money_Map.put("pay_money",changeCpPayMoney);
			money_Map.put("buy_money",changeCpPayMoney);
		}
	
		money_Map.put("sumYhRefundMoney", sumYhRefundMoney);
		money_Map.put("sumXxRefundMoney", sumXxRefundMoney);
		return money_Map;
	}
	
	//批量车站退票
	public void addMeituanStation(Map<String, Object> paramMap,Map<String, String> logMap){
		meituanRefundDao.addTicket_log(logMap);
		meituanRefundDao.addMeituanStation(paramMap);
	}

	@Override
	public void deleteOrder(Map<String, String> map) {
		meituanRefundDao.deleteOrder(map);
	}

	@Override
	public List<Map<String, String>> queryChangeRefundTicket(
			Map<String, Object> paramMap) {
		return meituanRefundDao.queryChangeRefundTicket(paramMap);
	}
}
