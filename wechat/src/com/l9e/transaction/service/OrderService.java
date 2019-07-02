package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;

public interface OrderService {

	String addOrder(OrderInfo orderInfo, OrderInfoPs orderInfoPs,
			List<OrderInfoCp> orderInfoCpList,
			List<OrderInfoBx> orderInfoBxList, Map<String, String> bxfpMap);

	Map<String, String> queryOrderInfo(String order_id);

	List<Map<String, String>> queryOrderDetailList(String order_id);

	List<Map<String, String>> queryOrderList(Map<String, Object> paramMap);

	int queryOrderListCount(Map<String, Object> paramMap);

	void refunding(RefundVo refund, String old_refund_status);

	void updateOrderEopInfo(Map<String, String> eopInfo);

	void updateOrderInfo(Map<String, String> map);

	void updateOrderRefund(Map<String, String> map);

	Map<String, String> queryNotifyCpOrderInfo(String order_id);

	List<Map<String, String>> queryCpInfoList(String order_id);

	List<Map<String, String>> queryScanedOrderList();

	int updateScanInfoById(Map<String, String> map);

	List<Map<String, String>> queryTimedSendList();

	void updateOrderStatus(Map<String, String> map);

	List<Map<String, String>> queryCpContactInfo(String orderId);

	String queryUploadTipTime(String order_id);

	List<OrderInfo> queryLastestOrderList(Map<String, Object> paramMap);

	void addRefundStream(List<Map<String, String>> refundList, String order_id);

	List<Map<String, String>> queryRefundStreamList(String order_id);

	List<Map<String, String>> queryTimedRefundStreamList();

	int updateRefundStreamBegin(Map<String, String> paramMap);

	void updateOrderStreamStatus(Map<String, String> map);

	Map<String, String> querySpecTimeBeforeFrom(String order_id);

	void addOrderRefundHis(List<Map<String, String>> refundList,
			List<Map<String, String>> differList);

	String queryBxPayMoneyAtPaySucc(String orderId);

	void updateOrderPayNo(Map<String, String> map);

	List<String> queryPassengerList(String orderId);

	void updateRefundStream(Map<String, String> paramMap);

}
