package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ExtRefundDao {
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

	void updateRefundNotify(Map<String,Object>refund_Map);

	List<Map<String, String>> queryExtMerchantinfo();

	void updateExtRefundNotifyNum(Map<String, String> paramMap);

	List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, Object> mapAdd);

	String queryRefundStationTicketCpId(Map<String, Object> paramMap);

	String queryCpidIsRefund(Map<String, Object> paramMap);

	String queryMerchantOrderId(String orderId);

	void updateExtRefundNotifyStatus(Map<String, Object> refundMap);

	void deleteRefundNotify(Map<String, Object> refuseMap);

	void updateCpRefundStatusSuccess(String string);

	int queryRefundCount(String string);

	void updateCpRefundStatusFail(Map<String, Object> refuse_Map);

	void updateOutTicketRefundEOP(Map<String, Object> outTicketDefeatedMap);

	void addOrderRefundNotify(Map<String, String> mapAdd);

	List<Map<String, String>> queryOrderInfo(Object object);

	void updateGezhiRefund(Map<String, Object> refuseMap);
	

	//更新日志信息
	public void insertLog(HashMap<String, Object> map);
	//修改orderinfo订单中操作人信息
	public void updateOrder(HashMap<String, Object> map);
	
	//修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map);
	
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap);

	List<Map<Object, Object>> queryExtOrderInfo(String order_id);

	int queryRefundNotifyNum(Map<String, String> param_eop);

	void addRefundNotify(Map<String, String> param_eop);

	String findExtRefundNotifyUrl(Map<String, String> queryMap);
}
