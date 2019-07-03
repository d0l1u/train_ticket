package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface InnerRefundService {
	int queryRefundTicketCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketList(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map);

	List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id);
	
	List<Map<String, Object>> queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> map_add);
	
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
	
	void updateOfflineRefund(Map<String, String> log_Map,
			Map<String, Object> offline_Map);

	void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map);

	void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap);

	void addErrorLogInfo(Map<String, String> log_Map);
	
	String queryRefundTicketOrderId(String order_id);

	String queryRefundTicketOrderIdExists(String orderId);

	Map<String, String> queryBookOrderInfo(String orderId);

	List<Map<String, String>> queryBookOrderInfoBx(String orderId);

	List<Map<String, String>> queryBookOrderInfoCp(String orderId);

	int queryNoReplyCount();
	
	List<Map<String, String>> queryRefundTicket(Map<String, Object> paramMap);

	List<Map<String, Object>> queryRefundTicketAdd(Map<String, String> logMap,
			Map<String, Object> mapAdd);

	void refundTicketAgain(Map<String, String> logMap,
			Map<String, Object> refundMap);
}
