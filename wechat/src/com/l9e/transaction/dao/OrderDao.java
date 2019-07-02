package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;

public interface OrderDao {

	void addOrderInfo(OrderInfo orderInfo);

	void addOrderInfoCp(OrderInfoCp orderInfoCp);

	void addOrderInfoBx(OrderInfoBx orderInfoBx);

	List<Map<String, String>> queryOrderDetailList(String order_id);

	Map<String, String> queryOrderInfo(String order_id);

	List<Map<String, String>> queryOrderList(Map<String, Object> paramMap);

	int queryOrderListCount(Map<String, Object> paramMap);

	void updateOrderEopInfo(Map<String, String> eopInfo);

	void updateOrderInfo(Map<String, String> map);
	List<Map<String, String>> queryCpInfoList(String order_id);

	List<Map<String, String>> queryScanedOrderList();

	int updateScanInfoById(Map<String, String> map);

	Map<String, String> queryNotifyCpOrderInfo(String order_id);

	List<Map<String, String>> queryTimedSendList();


	List<Map<String, String>> queryCpContactInfo(String orderId);

	void updateOrderRefund(Map<String, String> eopInfo);

	void updateBxStatusNotSend(String order_id);

	Map<String, String> queryOrderDiffer(String order_id);

	String queryUploadTipTime(String order_id);


	void deleteOldRefund(String order_id);

	List<OrderInfo> queryLastestOrderList(Map<String, Object> paramMap);

	void updateOrderRefundTotal(Map<String, String> paramMap);

	void addRefundStream(Map<String, String> refundMap);

	int queryRefundStreamContainCp(Map<String, String> refundMap);

	List<Map<String, String>> queryRefundStreamList(String order_id);

	int queryRefundLeftCount(String order_id);

	void deleteRefundStreamOnRefuse(Map<String, String> refundMap);

	List<Map<String, String>> queryTimedRefundStreamList();

	void addOrderOptLog(Map<String, String> logMap);

	int updateRefundStreamBegin(Map<String, String> paramMap);

	void updateOrderStreamStatus(Map<String, String> map);

	Map<String, String> querySpecTimeBeforeFrom(String order_id);

	void addOrderInfoBxfp(Map<String, String> bxfpMap);

	String queryBxPayMoneyAtPaySucc(String orderId);

	void updateOrderPayNo(Map<String, String> map);

	List<String> queryPassengerList(String orderId);

	void updateRefundStream(Map<String, String> paramMap);
}
