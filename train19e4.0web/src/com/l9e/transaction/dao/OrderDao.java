package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;

public interface OrderDao {

	void addOrderInfo(OrderInfo orderInfo);

	void addOrderInfoCp(OrderInfoCp orderInfoCp);

	void addOrderInfoBx(OrderInfoBx orderInfoBx);

	void addOrderInfoPs(OrderInfoPs orderInfoPs);
	
	List<Map<String, String>> queryOrderDetailList(String order_id);

	OrderInfo queryOrderInfo(String order_id);
	
	OrderInfo queryOrderInfo2(Map<String, String> paramMap);

	OrderInfoPs queryOrderInfoPs(String order_id);
	
	List<OrderInfo> queryOrderList(Map<String, Object> paramMap);

	int queryOrderListCount(Map<String, Object> paramMap);
	
	void updateOrder(String orderid);
	
	void updateRefund(RefundVo refund);

	void updateOrderEopInfo(Map<String, String> eopInfo);

	List<Map<String, String>> queryTimedRefundList();

	List<Map<String, String>> queryCpInfoList(String order_id);
	
	List<Map<String, String>> queryScanedOrderList();

	int updateScanInfoById(Map<String, String> map);

	Map<String, String> queryNotifyCpOrderInfo(String order_id);

	void updateOrderTimeOut(Map<String, String> map);

	List<Map<String, String>> queryTimedSendList();
	
	List<Map<String, String>> queryTimedSendList2();

	Map<String, String> queryOrderContactInfo(String orderId);

	List<Map<String, String>> queryCpContactInfo(String orderId);

	void updateOrderRefund(Map<String, String> eopInfo);

	void updateBxStatusNotSend(String order_id);

	Map<String, String> queryOrderDiffer(String order_id);

	void addOrderDiffer(Map<String, String> differMap);

	List<Map<String, String>> queryTimedDifferRefundList();

	int updateDifferBegin(Map<String, String> paramMap);

	void updateOrderDiffer(Map<String, String> map);

	int queryDifferCountBySeq(String refund_seq);

	String queryUploadTipTime(String order_id);

	Map<String, String> queryDifferRefundInfo(String order_id);

	void deleteOldRefund(String order_id);

	List<OrderInfo> queryLastestOrderList(Map<String, Object> paramMap);

	Map<String, String> queryOrderForRefund(String order_id);

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

	List<Map<String, String>> queryOrderDifferForHis();

	List<Map<String, String>> queryOrderRefundForHis();

	void addRefundStreamForHis(Map<String, String> map1);

	void updateOrderCanRefundForHis(String order_id);

	void addOrderInfoBxfp(Map<String, String> bxfpMap);

	String queryBxPayMoneyAtPaySucc(String orderId);

	void addMsn(Map<String, String> msn);
	
	/**
	 * 	查询代理商出票成功、出票失败、出票中的订单数据
	 * @param map
	 * @return
	 */
	Map<String,Object> queryAgentOrderNum(Map<String,Object> map);
	
	/**
	 * 查询代理商退款的订单数据
	 * @param map
	 * @return
	 */
	Map<String,Object> queryAgentRefundNum(Map<String,Object> map);
	
	String selectRefundPassengers(String cp_id);

	List<OrderInfo> queryOrderRefundList(Map<String, Object> paramMap);

	List<Map<String, Object>> querySaleReportList(Map<String, Object> paramMap);

	int querySaleReportListCount(Map<String, Object> paramMap);

	Map<String, Object> queryOrderTravelTime(String orderId);

	void updateOrderStatus(Map<String, String> paramMap);

	List<Map<String, String>> queryCanRefundStreamList();

	Map<String, String> queryAccountOrderInfo(Map<String, String> param);

	Map<String, String> queryRefundCpOrderInfo(Map<String, String> param);

	void updateCPAlterInfo(Map<String, Object> map);

	//void updateCPRefundInfo(Map<String, String> map);

	void updateRefundInfo(Map<String, Object> map);

	void updateOrderRefundStatus(Map<String, String> map);
	//人工出票数
	int queryManualOrderCount();

	List<String> queryNeedCheckOptlogList(String orderId);
	
	int deleteOrder(String orderId);

	void addOrderSpsmInfo(Map<String, String> spsmMap);

	Map<String, String> queryOrderInfoPssm(String orderId);

	void insertIntoPsOutTicket(Map<String, String> orderMap);

	void insertIntoPsOutTicketCp(Map<String, String> cpMap);

	void updateHcOrderTo22(String orderId);
	/**
	 * 学生票 插入学生信息
	 * @param studentInfo
	 */
	void addStudentInfo(DBStudentInfo studentInfo);
}
