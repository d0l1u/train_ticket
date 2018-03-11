package com.l9e.transaction.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RefundTicketDao;
import com.l9e.transaction.service.RefundTicketService;


@Service("refundTicketService")
public class RefundTicketServiceImpl implements RefundTicketService{
	@Resource
	private RefundTicketDao refundTicketDao ;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return refundTicketDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return refundTicketDao.queryRefundTicketList(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return refundTicketDao.queryRefundTicketInfo(id_Map);
	}

	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return refundTicketDao.queryRefundTicketOrderInfoCp(cp_id);
	}
	
	
	public List<Map<String, String>> queryRefundTicketByStreamId(String stream_id){
		return refundTicketDao.queryRefundTicketByStreamId(stream_id);
	}
	public List<Map<String, Object>> queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		refundTicketDao.addOrderRefundTicket_log(log_Map);
		return refundTicketDao.queryrefundTicketAdd(mapAdd);
	}
	
	public void updateOrderInfo_can_refundTo0(Map<String, Object> mapAdd) {
		refundTicketDao.updateOrderInfo_can_refundTo0(mapAdd);
	}
	
	public List<Map<String, Object>> queryrefundTicketTelAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		refundTicketDao.addOrderRefundTicket_log(log_Map);
		return refundTicketDao.queryrefundTicketTelAdd(mapAdd);
	}
	
	public void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map) {
		refundTicketDao.addCpRefundTicket_log(log_Map);
		refundTicketDao.updateRefundTicketInfo(refund_Map); //执行退款
	}

	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return refundTicketDao.queryHistroyByCpId(cp_id);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return refundTicketDao.queryHistroyByOrderId(order_id);
	}
	
	public Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = refundTicketDao.querySumRefundMoney(order_id);
		String pay_money = refundTicketDao.queryBuymoneyAndTicketpaymoney(order_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	
	public Map<String, String> queryRefundMoney(String order_id,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = refundTicketDao.querySumRefundMoney(order_id);
		String pay_money = refundTicketDao.queryBuymoneyAndTicketpaymoney(order_id);
		String refund_money =refundTicketDao.queryRefundMoney(stream_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		money_Map.put("refund_money", refund_money);
		return money_Map;
	}
	
	
	public void updateRefuseRefund(Map<String, String> log_Map,
			Map<String, Object> refuse_Map,Map<String,String>order_Map) {
		refundTicketDao.addCpRefundTicket_log(log_Map);
		refundTicketDao.updateRefund_StatusTo55(refuse_Map);
		refundTicketDao.updateOrderInfo_can_refundTo1_And_refund_total(order_Map);
	}

	public void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map) {
		refundTicketDao.addOrderRefundTicket_log(log_Map);
		refundTicketDao.updateDifferRefund(differ_Map);
	}


	public void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map) {
		refundTicketDao.addOrderRefundTicket_log(log_Map);
		refundTicketDao.updateOut_Ticket_Refund(outTicket_Defeated_Map);
	}

	public void updateRefreshNotice( Map<String, Object> update_RefreshNoticeMap) {
		refundTicketDao.updateRefreshNotice(update_RefreshNoticeMap);
	}
	
	public void updateRefreshNoticeById(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> update_Map) {
		int count=	refundTicketDao.updateRefreshNoticeById(update_Map);
		if(count>0){
			write(response,"true");
		}else{
			write(response,"false");
		}
		
	}
	private void write(HttpServletResponse response, String wrStr) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(wrStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addErrorLogInfo(Map<String, String> log_Map) {
		refundTicketDao.addCpRefundTicket_log(log_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return refundTicketDao.queryRefundTicketOrderId(order_id);
	}

	public String queryRefundTicketOrderIdExists(String orderId) {
		return refundTicketDao.queryRefundTicketOrderIdExists(orderId);
	}

	@Override
	public int queryNoReplyCount() {
		return refundTicketDao.queryNoReplyCount();
	}

	@Override
	public void updateGezhiRefund(Map<String, String> logMap,
			Map<String, Object> refuseMap) {
		refundTicketDao.addCpRefundTicket_log(logMap);//添加操作日志
		refundTicketDao.updateGezhiRefund(refuseMap);//搁置订单
	}

	@Override
	public List<String> queryOrderCpId(String orderId) {
		return refundTicketDao.queryOrderCpId(orderId);
	}

	@Override
	public void updateAlertRefund(HashMap<String, Object> paramMap) {
		refundTicketDao.updateAlertRefund(paramMap);
	}

	@Override
	public List<Map<String, String>> queryRefundTicket(
			Map<String, Object> paramMap) {
		return refundTicketDao.queryRefundTicket(paramMap);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		refundTicketDao.updateOrder(map);
		
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		refundTicketDao.updateRefundOpt(map);
		
	}
  
	@Override
	public int queryCpidIsRefundNum(Map<String, Object> paramMap) {
		return refundTicketDao.queryCpidIsRefundNum(paramMap);
	}
	
	@Override
	public String queryStatusByOrderId(Map<String, Object> paramMap) {
		return refundTicketDao.queryStatusByOrderId(paramMap);
	}
	
	@Override
	public  Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap) {
		return refundTicketDao.queryMoneyByCpId(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd) {
		refundTicketDao.addOrderRefundTicket_log(logMap); 
		return refundTicketDao.queryRefundStationTicketAdd(mapAdd);
	}
}
