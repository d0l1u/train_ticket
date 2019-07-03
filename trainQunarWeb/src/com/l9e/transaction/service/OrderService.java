package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoTrip;


public interface OrderService {

	int queryOrderCountByNo(String order_id);
	
	int queryOrderCPCountByNo(String order_id);
	
	void addQunarOrder(OrderInfo orderInfo, List<OrderInfoCp> cpList, List<OrderInfoTrip> tripList, List<DBStudentInfo> students);

	List<Map<String, String>> queryTimedOutTicketList();

	List<Map<String, String>> queryOrderCpList(String order_id);

	void updateQunarOutNotifyBegin(String order_id);

	void updateQunarOutNotifyEnd(String order_id);

	List<Map<String, String>> queryTimedWaitingRefundList();

	void updateQunarRefundNotifyBegin(Map<String, String> map);

	void updateQunarRefundNotifyEnd(Map<String, String> map);

	int queryRefundCountByNo(String order_id);

	void addQunarRefund(Map<String, String> map);

	OrderInfo queryOrderInfoById(String order_id);

	List<Map<String, String>> queryTimedCpSysList();

	List<Map<String, String>> queryCpInfoList(String order_id);

	void updateCpSysNotifyBegin(String order_id);

	void updateCpSysOutNotifyEnd(String corder_id, String order_id);

	int updateOrderWithCpNotify(Map<String, String> paramMap,
			List<Map<String, String>> cpMapList, Map<String, String> logMap, 
			String notifyQuanrAtStatus, OrderInfoTrip trip);

	void addOrderInfoLog(Map<String, String> logMap);

	String queryQunarSysSetting(String string);

	OrderInfoTrip queryTripOrderInfoById(String trip_id);

	List<Map<String, String>> queryTripListByOrderId(String order_id);

	int queryOutTicketNotifyCount(String order_id);

	void updateOrderNotifyFail(String order_id);

	List<Map<String, String>> queryPayMoneyByOrderId(String order_id);
	
	int queryRefundCountByRefuse(String order_id);

	Map<String, String> queryRefundStatusByNo(String order_id);

	void updateQunarRefund(String order_id);

	void updateQunarRefundStatus(String orderId);
	
	void updateCPAlterInfo(Map<String,String> map);
	
	void updateCPRefundInfo(Map<String,String> map);
	
	void updateRefundInfoSingle(Map<String,String> map);

	void updateQunarCPRefundMoney(Map<String,Object> map);
	
	List<Map<String, String>> queryCanRefundStreamList();
	
	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);
	
	Map<String, String> queryAccountOrderInfo(Map<String, String> param);
	
	void updateOrderRefundStatus(Map<String, String> map);
	
	int queryOrderCpNoRefundNum(String order_id);
	
	List<Map<String, String>> queryOrderCpRefundInfoList(String order_id);

	List<Map<String, String>> queryBookResultList();

	void updateQunarBookNotifyEnd(String orderId);

	int queryBookTicketNotifyCount(String orderId);

	void updateOrderBookNotifyFail(String orderId);

	void updateQunarBookNotifyBegin(String string);

	List<Map<String, String>> queryWaitPayOrderList();

	void updateQunarPayNotifyBegin(String string);

	void updateQunarPayNotifyEnd(String orderNo);

	int queryPayNotifyCount(String orderNo);

	void updateOrderPayNotifyFail(String orderNo);
	
	void addOrderInfoByBackup(String order_id);
	
	void addOrderCpInfoByBackup(String order_id);
	
	Map<String, String> queryAccountOrderBackupInfo(Map<String, String> param);

	Map<String, String> queryAccountInfo(String orderNo);

	void updateOrderStatus(Map<String, String> map);
	
	void sendCpSysOutTicket(String order_type,String order_id)throws Exception ;
	
	void sendNotifyRequest(String order_id, String order_type) throws Exception ;

	void updateOrderWithCpNotify(Map<String, String> map);

	void updateBookNotifyPrepare(String string);

	List<Map<String, String>> queryOrderList(Map<String, Object> param);

	List<Map<String, String>> queryPassengerList(Map<String, String> map);

	Integer queryOrderCount(Map<String, Object> param);

}
