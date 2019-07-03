package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.InnerRefundDao;
import com.l9e.transaction.service.InnerRefundService;

@Service("innerRefundService")
public class InnerRefundServiceImpl implements InnerRefundService {
	@Resource
	private InnerRefundDao innerRefundDao ;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return innerRefundDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return innerRefundDao.queryRefundTicketList(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return innerRefundDao.queryRefundTicketInfo(id_Map);
	}

	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return innerRefundDao.queryRefundTicketOrderInfoCp(cp_id);
	}
	
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		innerRefundDao.addOrderRefundTicket_log(log_Map);
		return innerRefundDao.queryrefundTicketAdd(mapAdd);
	}
	
	public void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map) {
		innerRefundDao.addCpRefundTicket_log(log_Map);
		innerRefundDao.updateRefundTicketInfo(refund_Map);
	}

	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return innerRefundDao.queryHistroyByCpId(cp_id);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return innerRefundDao.queryHistroyByOrderId(order_id);
	}
	
	public Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = innerRefundDao.querySumRefundMoney(order_id);
		String pay_money = innerRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	
	public Map<String, String> queryRefundMoney(String order_id,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = innerRefundDao.querySumRefundMoney(order_id);
		String pay_money = innerRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		String refund_money =innerRefundDao.queryRefundMoney(stream_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		money_Map.put("refund_money", refund_money);
		return money_Map;
	}
	
	public void updateRefuseRefund(Map<String, String> log_Map,
			Map<String, Object> refuse_Map,Map<String,String>order_Map) {
		innerRefundDao.addCpRefundTicket_log(log_Map);
		innerRefundDao.updateRefund_StatusTo55(refuse_Map);
		innerRefundDao.updateOrderInfo_can_refundTo1_And_refund_total(order_Map);
	}

	public void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map) {
		innerRefundDao.addOrderRefundTicket_log(log_Map);
		innerRefundDao.updateDifferRefund(differ_Map);
	}


	public void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map) {
		innerRefundDao.addOrderRefundTicket_log(log_Map);
		innerRefundDao.updateOut_Ticket_Refund(outTicket_Defeated_Map);
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		innerRefundDao.updateRefreshNotice(update_RefreshNoticeMap);
	}

	public void addErrorLogInfo(Map<String, String> log_Map) {
		innerRefundDao.addCpRefundTicket_log(log_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return innerRefundDao.queryRefundTicketOrderId(order_id);
	}

	public String queryRefundTicketOrderIdExists(String orderId) {
		return innerRefundDao.queryRefundTicketOrderIdExists(orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return innerRefundDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return innerRefundDao.queryBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return innerRefundDao.queryBookOrderInfoCp(orderId);
	}

	@Override
	public int queryNoReplyCount() {
		return innerRefundDao.queryNoReplyCount();
	}

	@Override
	public List<Map<String, String>> queryRefundTicket(
			Map<String, Object> paramMap) {
		return innerRefundDao.queryRefundTicket(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundTicketAdd(Map<String, String> logMap,
			Map<String, Object> mapAdd) {
		innerRefundDao.addCpRefundTicket_log(logMap);
		return innerRefundDao.queryRefundTicketAdd(mapAdd);
	}

	@Override
	public void updateOfflineRefund(Map<String, String> logMap,
			Map<String, Object> offlineMap) {
		innerRefundDao.addCpRefundTicket_log(logMap);
		innerRefundDao.updateOfflineRefund(offlineMap);
	}

	@Override
	public void refundTicketAgain(Map<String, String> logMap,
			Map<String, Object> refundMap) {
		innerRefundDao.addCpRefundTicket_log(logMap);
		innerRefundDao.refundTicketAgain(refundMap);
	}

}
