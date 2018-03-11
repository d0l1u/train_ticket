package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookStuInfo;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;

public interface OrderService {
	 
	String addOrder(OrderInfo orderInfo,List<OrderInfoCp> orderInfoCpList, List<OrderInfoBx> orderInfoBxList, 
			List<BookStuInfo> studentsList,Map<String, String> bxfpMap ,String book_flow);
	 
	OrderInfo queryOrderInfo(String order_id);
	 
	OrderInfoPs queryOrderInfoPs(String order_id);
	 
	List<Map<String, String>> queryOrderDetailList(String order_id);
	 
	List<OrderInfo> queryOrderList(Map<String, Object> paramMap);
	
	List<BookDetailInfo> queryOrderCpList(String order_id);
	
	int queryOrderListCount(Map<String, Object> paramMap);
	 
	void updateOrderRefund(Map<String, String> map);
	
	Map<String, String> queryOrderContactInfo(String orderId);
	
	List<Map<String, String>> queryCpContactInfo(String orderId);
	 
	List<Map<String, String>> queryTimedRefundList();
	 
	Map<String, String> queryNotifyCpOrderInfo(String order_id);
	 
	List<Map<String, String>> queryCpInfoList(String order_id);
	 
	List<Map<String, String>> queryTimedSendList();
	 
	void updateOrderStatus(Map<String, String> map);
	 
	List<Map<String, String>> queryRefundStreamList(String order_id);
	 
	Map<String, String> querySpecTimeBeforeFrom(String order_id);
	 
	List<String> queryCp_idList(String order_id);
	 
	List<String> queryCp_idByIds(Map<String, String> paramMap);
	 
	String queryOrderStatusById(Map<String, String> paramMap);
	
	String queryCpOrderinfoStatusById(Map<String, String> paramMap);
	 
	void updateOrderStatusById(Map<String, String> paramMap);
	
	int queryOrderBxCount(Map<String, String> paramMap);
	
	Map<String,String> queryOrderBxInfo(Map<String, String> paramMap);
	
	Map<String,String> queryOrderInfoByMap(Map<String,String> paramMap);
	
	Map<String,String> queryMerchantInfoByOrderId(String order_id);
	
	String nopwdPayOrder(String order_id);
	
	void updateOrderEopInfo(Map<String,String> map);
	
	String queryMsgChannel(String order_id);

	List<String> queryRefund_cp_list(String order_id);
	
	List<OrderInfoBx> queryBxInfosById(Map<String, String> paramMap);

	int updateRefuseOrderStatus(Map<String,String> param);

	void addAlterOrderinfo(List<Map<String,String>> param_list ,Map<String,String> param);
	
	int queryAlterOrderifoNum(Map<String,String> param);
	
	void insertOrderLogs(ExternalLogsVo logs);
	
	Map<String,String> queryFeeModel(String merchant_id);
	
	String queryOrderIdByEop(String eop_order_id);
	
	boolean sendNotifyEop(Map<String, String> map);
	
	void addOrderBookNotifyInfo(Map<String,Object> map);
	
	void updateOrderWaitPayMoney(Map<String,Object> map);
	
	JSONArray updateBookSuccessInfo(String type,String order_id,String merchant_id);
	
	String queryRefundStreamSeq(Map<String,String> param);
	
	Map<String,String> queryCpSizeAndPrice(String order_id);

	String eopSendOrderAndPay(OrderInfo orderInfo, Map<String, String> map);
	
	void startNotifyPayOrder(String order_id,String pay_money,String pay_type);

	List<Map<String, String>> queryCanRefundStreamList();

	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);

	Map<String, Object> queryAccountOrderInfo(Map<String, String> param);

	void updateOrderRefundStatus(Map<String, String> map);

	void updateCPAlterInfo(Map<String, Object> map);

	void updateRefundInfo(Map<String, Object> map);

	void updateRefundAgree(Map<String, Object> map, Map<String,String> paramMap);

	void updateRefundRefuse(Map<String, Object> map, Map<String,String> paramMap);
	
	int queryHistoryByOrderId(String orderId);
	OrderInfoCp queryCpInfoByCpId(String cpId);
	/**
	 * 查询订单操作日志
	 * @param paramMap
	 * @return
	 */
	String selectOrderLog(Map<String,Object> paramMap );

}
