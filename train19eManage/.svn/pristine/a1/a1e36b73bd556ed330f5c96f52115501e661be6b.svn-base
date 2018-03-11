package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RefundTicketService {

	int queryRefundTicketCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketList(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map);

	List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id);
	
	List<Map<String, String>> queryRefundTicketByStreamId(String stream_id);
	
	List<Map<String, Object>> queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> map_add);
	
	void updateOrderInfo_can_refundTo0(Map<String, Object> map_add);
	
	List<Map<String, Object>> queryrefundTicketTelAdd(Map<String, String> log_Map,Map<String, Object> map_add);
	
	Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id);
	
	Map<String, String> queryRefundMoney(String order_id,String Stream_id);
	
	void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map);

	List<Map<String, Object>> queryHistroyByCpId(String cp_id);

	List<Map<String, Object>> queryHistroyByOrderId(String order_id);

	void updateRefuseRefund(Map<String, String> log_Map,
			Map<String, Object> refuse_Map, Map<String, String> order_Map);

	void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map);

	void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map);

	void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap);
	
	void updateRefreshNoticeById(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> update_Map);

	void addErrorLogInfo(Map<String, String> log_Map);
	
	String queryRefundTicketOrderId(String order_id);

	String queryRefundTicketOrderIdExists(String orderId);

	int queryNoReplyCount();

	void updateGezhiRefund(Map<String, String> logMap,
			Map<String, Object> refuseMap);
	
	List<String> queryOrderCpId(String orderId);

	void updateAlertRefund(HashMap<String, Object> paramMap);
	
	List<Map<String, String>>  queryRefundTicket(Map<String, Object> paramMap);

	//修改操作人信息
	public void updateOrder(HashMap<String, Object> map);
	
	public void updateRefundOpt(HashMap<String, Object> map);
	//批量车站退票
	public int queryCpidIsRefundNum(Map<String, Object> paramMap);
	
	public String queryStatusByOrderId(Map<String, Object> paramMap);
	
	public Map<String,String> queryMoneyByCpId(Map<String, Object> paramMap);
	
	List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd);
}
