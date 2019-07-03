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
			List<OrderInfoCp> orderInfoCpList, List<OrderInfoBx> orderInfoBxList, Map<String, String> bxfpMap);

	OrderInfo queryOrderInfo(String order_id);

	OrderInfoPs queryOrderInfoPs(String order_id);
	
	List<Map<String, String>> queryOrderDetailList(String order_id);

	List<OrderInfo> queryOrderList(Map<String, Object> paramMap);

	int queryOrderListCount(Map<String, Object> paramMap);

	void refunding(RefundVo refund, String old_refund_status);

	void updateOrderEopInfo(Map<String, String> eopInfo);
	
	void updateOrderRefund(Map<String, String> map);

	List<Map<String, String>> queryTimedRefundList();

	Map<String, String> queryNotifyCpOrderInfo(String order_id);

	List<Map<String, String>> queryCpInfoList(String order_id);
	
	List<Map<String, String>> queryScanedOrderList();

	int updateScanInfoById(Map<String, String> map);

	void updateOrderTimeOut(Map<String, String> map);

	List<Map<String, String>> queryTimedSendList();

	void updateOrderStatus(Map<String, String> map);

	Map<String, String> queryOrderContactInfo(String orderId);

	List<Map<String, String>> queryCpContactInfo(String orderId);

	List<Map<String, String>> queryTimedDifferRefundList();

	int updateDifferBegin(Map<String, String> paramMap);

	void updateOrderDiffer(Map<String, String> map);

	int queryDifferCountBySeq(String refund_seq);

	String queryUploadTipTime(String order_id);

	Map<String, String> queryDifferRefundInfo(String order_id);

	List<OrderInfo> queryLastestOrderList(Map<String, Object> paramMap);

	void addRefundStream(List<Map<String, String>> refundList, String order_id);

	List<Map<String, String>> queryRefundStreamList(String order_id);

	List<Map<String, String>> queryTimedRefundStreamList();

	int updateRefundStreamBegin(Map<String, String> paramMap);

	void updateOrderStreamStatus(Map<String, String> map);

	Map<String, String> querySpecTimeBeforeFrom(String order_id);

	List<Map<String, String>> queryOrderRefundForHis();

	List<Map<String, String>> queryOrderDifferForHis();

	void addOrderRefundHis(List<Map<String, String>> refundList,
			List<Map<String, String>> differList);

	String queryBxPayMoneyAtPaySucc(String orderId);

	void addMsn(Map<String, String> msn);
	
	void updateOrderPayNo(Map<String, String> map);

	/**
	 * 根据19pay返回的支付信息，修改支付状态和19pay订单号等
	 */
	int updateOrderPayInfo(Map<String,String> map);
	
	/**
	 * 根据订单号，查询退款流水中的退款金额和退款流水号
	 */
	RefundVo queryRefundSInfo(String orderId);
	
	/**
	 * 修改退款流水表中的退款金额，19pay退款流水号，退款时间，退款状态 
	 */
	int updateRefundStreamInfo(Map<String,String> map);
	
	/**
	 * 19pay收单成功，更新 19pay订单号退款流水和退款状态
	 */
	void updateRefundSStatus(Map<String,String> map);
	
	/**
	 * 出票结果通知扫描订单信息
	 */
	 List<Map<String, String>> queryTimedOrderReList();
	 
	 /**
	 * 修改出票结果通知表中的通知状态为开始通知
	 */
	 void updateReNotifyStatus(Map<String,String> map);
	 
	 /**
	  *  修改通知状态为通知完成 
	  */
	 void updateReNoStatusTo22(Map<String,String> map);
	 
	 /**
	  * 修改通知次数为10次
	  */
	 void updateReNotifyNum(Map<String,String> map);
	 
	 /**
	  * 出票结果通知表中信息的添加
	  */
	 void addResultNotifyInfo(Map<String,String> map);
	 
	 /**
	  * 出票结果通知表中信息的修改
	  */
	 int updateResultNotifyInfo(Map<String,String> map);
	 
	 void addOrderinfoHistory(Map<String, String> map);
	 
	 void updateOrderStreamResultStatus(Map<String, String> map);

	List<Map<String, String>> queryCanRefundStreamList();

	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);

	Map<String, String> queryAccountOrderInfo(Map<String, String> param);

	void updateOrderRefundStatus(Map<String, String> map);

	void updateCPAlterInfo(Map<String, Object> map);

	void updateRefundInfo(Map<String, Object> map);
}
