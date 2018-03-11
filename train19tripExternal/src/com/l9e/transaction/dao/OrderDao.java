package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookStuInfo;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;

public interface OrderDao {

	void addOrderInfo(OrderInfo orderInfo);

	void addOrderInfoCp(OrderInfoCp orderInfoCp);

	void addOrderInfoBx(OrderInfoBx orderInfoBx);

	void addOrderInfoPs(OrderInfoPs orderInfoPs);
	
	void addBookStuInfo(BookStuInfo bookStuInfo);
	
	List<Map<String, String>> queryOrderDetailList(String order_id);

	OrderInfo queryOrderInfo(String order_id);

	OrderInfoPs queryOrderInfoPs(String order_id);
	
	List<OrderInfo> queryOrderList(Map<String, Object> paramMap);

	List<BookDetailInfo> queryOrderCpList(String order_id);
	
	Map<String, String> queryOrderContactInfo(String orderId);

	List<Map<String, String>> queryCpContactInfo(String orderId);
	
	int queryOrderListCount(Map<String, Object> paramMap);
	
	void updateOrder(String orderid);
	
	void updateOrderStatus(Map<String, String> eopInfo);

	List<Map<String, String>> queryTimedRefundList();

	List<Map<String, String>> queryCpInfoList(String order_id);

	Map<String, String> queryNotifyCpOrderInfo(String order_id);

	List<Map<String, String>> queryTimedSendList();

	void updateOrderRefund(Map<String, String> eopInfo);

	void updateBxStatusNotSend(String order_id);

	Map<String, String> queryOrderDiffer(String order_id);

	Map<String, String> queryOrderForRefund(String order_id);

	void updateOrderRefundTotal(Map<String, String> paramMap);

	List<Map<String, String>> queryRefundStreamList(String order_id);

	void addOrderOptLog(Map<String, String> logMap);

	Map<String, String> querySpecTimeBeforeFrom(String order_id);

	void addOrderInfoBxfp(Map<String, String> bxfpMap);

	List<String> queryCp_idList(String orderId);
	
	List<String> queryCp_idByIds(Map<String, String> paramMap);
	
	String queryOrderStatusById(Map<String, String> paramMap);
	
	String queryCpOrderinfoStatusById(Map<String, String> paramMap);
	
	void updateOrderStatusById(Map<String, String> paramMap);
	
	int queryOrderBxCount(Map<String,String> paramMap);
	
	Map<String,String> queryOrderBxInfo(Map<String,String> paramMap);
	
	Map<String, String> queryOrderInfoByMap(Map<String,String> paramMap);
	
	Map<String,String> queryMerchantInfoByOrderId(String order_id);
	
	void updateOrderEopInfo(Map<String,String> map);
	
	void updateOrderPayFailReason(Map<String,String> map);
	
	void deleteOrderInfo(String order_id);
	
	void deleteOrderCpInfo(String order_id);
	
	void deleteOrderBxInfo(String order_id);
	
	String queryMsgChannel(String order_id);

	List<String> queryRefund_cp_list(String orderId);
	
	List<OrderInfoBx> queryBxInfosById(Map<String, String> paramMap);

	int updateRefuseOrderStatus(Map<String, String> param);
	
	void insertOrderLogs(ExternalLogsVo logs);
	
	void addAlterOrderinfo(Map<String,String> param_list);
	
	int queryAlterOrderifoNum(Map<String,String> param);
	
	void addAlterOrderNotify(Map<String,String> param);
	
	Map<String,String> queryFeeModel(String merchant_id);
	
	void updateMerchantTicketNum(String order_id); 
	
	String queryOrderIdByEop(String eop_order_id);
	
	void updateRefundOrderRepeatNotify(Map<String,String> param);
	
	Map<String,String> queryCpSizeAndPrice(String order_id);
	
	void addOrderBookNotifyInfo(Map<String,Object> map);
	
	void updateOrderWaitPayMoney(Map<String,Object> map);
	
	void updateBookSuccessTime(String order_id);
	
	String queryRefundStreamSeq(Map<String,String> param);
	
	Map<String, Object> queryAccountOrderInfo(Map<String, String> param);

	List<Map<String, String>> queryCanRefundStreamList();

	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);

	void updateCPAlterInfo(Map<String, Object> map);

	void updateOrderRefundStatus(Map<String, String> map);

	void updateRefundInfo(Map<String, Object> map);

	void updateCPOrderInfo(Map<String, Object> map);

	int queryRefundCount(String refundSeq);

	void updateExtRefundNotifyStatus(Map<String, Object> map);

	int queryHistoryByOrderId(String orderId);
	
	OrderInfoCp queryCpInfoByCpId(String cpId);
	/**
	 * 查询订单操作日志
	 * @param paramMap
	 * @return
	 */
	String selectOrderLog(Map<String,Object> paramMap );

}
