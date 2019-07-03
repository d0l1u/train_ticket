package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;

public interface OrderService {

	String addOrder(OrderInfo orderInfo, OrderInfoPs orderInfoPs,
	List<OrderInfoCp> orderInfoCpList, List<OrderInfoBx> orderInfoBxList, Map<String, String> bxfpMap, Map<String, String> spsmMap);

	OrderInfo queryOrderInfo(String order_id);
	
	OrderInfo queryOrderInfo2(Map<String, String> paramMap);

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
	
	List<Map<String, String>> queryTimedSendList2();

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
	
	/**
	 * 	查询代理商出票成功、出票失败、出票中、退款的订单数据 
	 * @param map
	 * @return
	 */
	Map<String,Object> queryAgentOrderNum(Map<String,Object> map);
	/**
	 * 	根据车票ID查询退票乘客 
	 * @param map
	 * @return
	 */
	String selectRefundPassengers(String cp_id);

	List<OrderInfo> queryOrderRefundList(Map<String, Object> paramMap);

	int querySaleReportListCount(Map<String, Object> paramMap);

	List<Map<String, Object>> querySaleReportList(Map<String, Object> paramMap);

	List<Map<String, String>> queryCanRefundStreamList();

	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);

	Map<String, String> queryAccountOrderInfo(Map<String, String> param);

	void updateCPAlterInfo(Map<String, Object> map);

	void updateRefundInfo(Map<String, Object> map);

//	void updateCPRefundInfo(Map<String, String> map);

	void updateOrderRefundStatus(Map<String, String> map);

	void addOrderOptLog(Map<String, String> map);
	
	//人工出票数
	int queryManualOrderCount();
	
	List<String> queryNeedCheckOptlogList(String orderId);
	//删除订单
	int deleteOrder(String orderId);

	Map<String, String> queryOrderInfoPssm(String orderId);

	boolean insertIntoPsOutTicket(String orderId, Map<String, String> orderMap,
			List<Map<String, String>> cpInfoList);

	void insertStuCpInfo(DBStudentInfo stu);

 }
