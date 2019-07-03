package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoTrip;


public interface OrderDao {

	int queryOrderCountByNo(String order_id);

	int queryOrderCPCountByNo(String order_id);
	
	void addQunarOrder(OrderInfo orderInfo);

	void addQunarOrderCp(OrderInfoCp cpInfo);

	void addQunarOrderNotify(String order_id, String order_type, String cp_notify_status);

	List<Map<String, String>> queryTimedOutTicketList();

	List<Map<String, String>> queryOrderCpList(String order_id);

	void updateQunarOutNotifyEnd(String order_id);

	void updateQunarOutNotifyBegin(String order_id);

	List<Map<String, String>> queryTimedWaitingRefundList();

	void updateQunarRefundNotifyBegin(Map<String, String> map);

	void updateQunarRefundNotifyEnd(Map<String, String> map);

	int queryRefundCountByNo(String order_id);

	void addQunarRefund(Map<String, String> map);

	OrderInfo queryOrderInfoById(String order_id);

	List<Map<String, String>> queryTimedCpSysList();

	List<Map<String, String>> queryCpInfoList(String order_id);

	void updateCpSysNotifyBegin(String order_id);

	void updateCpSysOutNotifyEnd(String order_id);

	void updateOrderWithCpNotify(Map<String, String> paramMap);

	void updateCpOrderWithCpNotify(Map<String, String> cpMap);

	void addOrderInfoLog(Map<String, String> logMap);

	void updateOutNotifyPrepare(String order_id);

	String queryQunarSysSetting(String key);

	void addQunarOrderTrip(OrderInfoTrip trip);

	OrderInfoTrip queryTripOrderInfoById(String trip_id);

	void updateTripOrderWithCpNotify(Map<String, String> paramMap);

	List<Map<String, String>> queryTripListByOrderId(String order_id);

	int queryOutTicketNotifyCount(String order_id);

	void updateOrderNotifyFail(String order_id);

	List<Map<String, String>> queryPayMoneyByOrderId(String order_id);
	
	int queryRefundCountByRefuse(String order_id);

	Map<String, String> queryRefundStatusByNo(String order_id);

	void updateQunarRefund(String order_id);

	void updateQunarRefundStatus(String order_id);
	
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

	int queryBookTicketNotifyCount(String orderId);

	void updateOrderBookNotifyFail(String orderId);

	void updateQunarBookNotifyEnd(String orderId);

	void updateQunarBookNotifyBegin(String orderId);

	int queryPayNotifyCount(String orderNo);

	List<Map<String, String>> queryWaitPayOrderList();

	void updateOrderPayNotifyFail(String orderNo);

	void updateQunarPayNotifyBegin(String orderNo);

	void updateQunarPayNotifyEnd(String orderNo);

	
	void addOrderInfoByBackup(String order_id);
	
	void addOrderCpInfoByBackup(String order_id);
	
	Map<String, String> queryAccountOrderBackupInfo(Map<String, String> param);

	Map<String, String> queryAccountInfo(String orderNo);

	void updateBookNotifyPrepare(String string);

	List<Map<String, String>> queryOrderList(Map<String, Object> param);

	List<Map<String, String>> queryPassengerList(Map<String, String> map);

	Integer queryOrderCount(Map<String, Object> param);

	void addStudentInfo(DBStudentInfo s);
}
