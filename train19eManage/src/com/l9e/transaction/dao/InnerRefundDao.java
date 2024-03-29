package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface InnerRefundDao {
	int queryRefundTicketCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketList(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map);

	List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id);

	void addCpRefundTicket_log(Map<String, String> log_Map);

	void updateRefundTicketInfo(Map<String, Object> refund_Map);

	List<Map<String, Object>> queryHistroyByCpId(String cp_id);
	
	List<Map<String, Object>> queryrefundTicketAdd(Map<String, Object> map_add);
	
	String queryBuymoneyAndTicketpaymoney(String order_id);
	
	String queryRefundMoney(String Stream_id);
	
	void updateRefund_StatusTo11(Map<String,Object> id_Map); 

	void updateRefund_StatusTo55(Map<String, Object> refuse_Map);

	void updateOrderInfo_can_refundTo1_And_refund_total(Map<String, String> order_Map);

	void updateDifferRefund(Map<String, Object> differ_Map);

	void addOrderRefundTicket_log(Map<String, String> log_Map);

	List<Map<String, Object>> queryHistroyByOrderId(String order_id);

	void updateOut_Ticket_Refund(Map<String, Object> outTicket_Defeated_Map);

	void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap);

	String queryRefundTicketOrderId(String order_id);

	String querySumRefundMoney(String orderId);

	String queryRefundTicketOrderIdExists(String orderId);

	Map<String, String> queryBookOrderInfo(String orderId);

	List<Map<String, String>> queryBookOrderInfoBx(String orderId);

	List<Map<String, String>> queryBookOrderInfoCp(String orderId);

	int queryNoReplyCount();
	
	List<Map<String, String>> queryRefundTicket(Map<String, Object> paramMap);

	List<Map<String, Object>> queryRefundTicketAdd(Map<String, Object> mapAdd);

	void updateOfflineRefund(Map<String, Object> offlineMap);

	void refundTicketAgain(Map<String, Object> refundMap);
}
