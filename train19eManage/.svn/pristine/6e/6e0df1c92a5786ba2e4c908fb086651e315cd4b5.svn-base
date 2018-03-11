package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GtRefundService {
	int queryRefundTicketCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketList(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map);

	List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id);
	
	void queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> map_add);
	
	Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id);
	
	Map<String, String> queryRefundMoney(String order_id,String Stream_id);
	
	void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map);

	List<Map<String, Object>> queryHistroyByCpId(String cp_id);

	List<Map<String, Object>> queryHistroyByOrderId(String order_id);
	
	
	void updateGezhiRefund(Map<String, String> logMap,
			Map<String, Object> refuseMap);

	void updateRefuseRefund(Map<String, String> log_Map,
			Map<String, Object> refuse_Map, Map<String, String> order_Map);

	void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map);

	void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map);

	void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap);

	void addErrorLogInfo(Map<String, String> log_Map);
	
	String queryRefundTicketOrderId(String order_id);

	String queryRefundTicketOrderIdExists(String orderId);

	Map<String, String> queryBookOrderInfo(String orderId);

	List<Map<String, String>> queryBookOrderInfoBx(String orderId);

	List<Map<String, String>> queryBookOrderInfoCp(String orderId);

	void updateRefundNotify(Map<String,Object>refund_Map,Map<String,String>log_Map);

	List<Map<String, String>> queryGtMerchantinfo();

	void updateGtRefundNotifyNum(Map<String, String> paramMap);

	List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd);

	String queryRefundStationTicketCpId(Map<String, Object> paramMap);

	String queryCpidIsRefund(Map<String, Object> paramMap);

	String queryMerchantOrderId(String orderId);


	//更新日志信息
	public void insertLog(HashMap<String, Object> map);
	//修改orderinfo订单中操作人信息
	public void updateOrder(HashMap<String, Object> map);
	
	//修改refund操作人信息
	public void updateRefundOpt(HashMap<String, Object> map);
	
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap);

	int queryCpidIsRefundNum(Map<String, Object> pMap);

	String queryStatusByOrderId(Map<String, Object> pMap);

	Map<String, String> queryMoneyByCpId(Map<String, Object> pMap);

	String queryMerchantIdByOrderId(Map<String, Object> pMap);

	List<Map<String, String>> queryTicketCpId(Map<String, Object> paramMap);

	Map<String, String> checkRefundMoney(Map<String, Object> paramMap);
	
	public List<Map<String, String>> queryChangeRefundTicket(Map<String, Object> paramMap);
	String queryChangeCpPayMoney(Map<String, Object> paramMap);
}
