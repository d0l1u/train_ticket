package com.l9e.transaction.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.AmountUtil;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

	@Resource
	private OrderDao orderDao;
	
	@Resource
	private CommonDao commonDao;

	public String addOrder(OrderInfo orderInfo, OrderInfoPs orderInfoPs,
			List<OrderInfoCp> orderInfoCpList, List<OrderInfoBx> orderInfoBxList, Map<String, String> bxfpMap,Map<String, String> spsmMap) {
		
		orderDao.addOrderInfo(orderInfo);//订单
		
		for (OrderInfoCp orderInfoCp : orderInfoCpList) {//车票
			orderDao.addOrderInfoCp(orderInfoCp);
		}
		
		for (OrderInfoBx orderInfoBx : orderInfoBxList) {//保险
			orderDao.addOrderInfoBx(orderInfoBx);
		}
		orderDao.addOrderInfoPs(orderInfoPs);//配送
		
		if(bxfpMap != null){
			orderDao.addOrderInfoBxfp(bxfpMap);
		}

		if(spsmMap != null){
			orderDao.addOrderSpsmInfo(spsmMap);
		}
		
		return orderInfo.getOrder_id();
	}
	
	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return orderDao.queryOrderDetailList(order_id);
	}

	public OrderInfo queryOrderInfo(String order_id) {
		return orderDao.queryOrderInfo(order_id);
	}
	
	public OrderInfo queryOrderInfo2(Map<String, String> paramMap) {
		return orderDao.queryOrderInfo2(paramMap);
	}

	public OrderInfoPs queryOrderInfoPs(String order_id) {
		return orderDao.queryOrderInfoPs(order_id);
	}

	public List<OrderInfo> queryOrderList(Map<String, Object> paramMap) {
		return orderDao.queryOrderList(paramMap);
	}

	public int queryOrderListCount(Map<String, Object> paramMap) {
		return orderDao.queryOrderListCount(paramMap);
	}
	
	public void refunding(RefundVo refund, String old_refund_status) {
//		orderDao.updateOrder(refund.getOrder_id());
		
		//如果是拒绝退款，则先删除原来的退款信息
		if(!StringUtils.isEmpty(old_refund_status) && TrainConsts.REFUND_REFUSE.equals(old_refund_status)){
			orderDao.deleteOldRefund(refund.getOrder_id());
		}
		
		orderDao.updateRefund(refund);
	}

	public void updateOrderEopInfo(Map<String, String> eopInfo) {
		commonDao.updateOrderEop(eopInfo);
		
		String orderId = eopInfo.get("asp_order_id");
		if(eopInfo.get("order_status").equals(TrainConsts.PAY_SUCCESS)){//支付成功
			//判断出发日期是否超过20天，若超过则将订单更改为“预约购票状态77”
			Map<String, Object> map = orderDao.queryOrderTravelTime(orderId);
			long differ_day = (Long)map.get("differ_day");// 12306可以购票的日期-发车日期
			if(differ_day < 0){//出发日期超过20天
				//将订单更改为“预约购票状态77”
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("order_status", "77");
				paramMap.put("order_id", orderId);
				orderDao.updateOrderStatus(paramMap);
				logger.info("订单号【"+orderId+"】出发日期超过60天,将订单更改为'预约购票状态77'");
			}else{
				orderDao.updateOrderEopInfo(eopInfo);
				logger.info("订单号【"+orderId+"】出发日期在60天之内，将订单状态更改为'支付成功11'");
			}
		}else{//支付失败
			orderDao.updateOrderEopInfo(eopInfo);
			logger.info("订单号【"+orderId+"】支付失败，将订单状态更改为'支付失败99'");
		}
	}

	public void updateOrderRefund(Map<String, String> map) {
		commonDao.updateOrderEop(map);
		
		orderDao.updateOrderRefund(map);
	}

	public List<Map<String, String>> queryTimedRefundList() {
		return orderDao.queryTimedRefundList();
	}

	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return orderDao.queryCpInfoList(order_id);
	}
	
	public List<Map<String, String>> queryScanedOrderList() {
		return orderDao.queryScanedOrderList();
	}

	public int updateScanInfoById(Map<String, String> map) {
		return orderDao.updateScanInfoById(map);
	}

	public Map<String, String> queryNotifyCpOrderInfo(String order_id) {
		return orderDao.queryNotifyCpOrderInfo(order_id);
	}

	public void updateOrderTimeOut(Map<String, String> map) {
		orderDao.updateOrderTimeOut(map);
	}

	public List<Map<String, String>> queryTimedSendList() {
		return orderDao.queryTimedSendList();
	}
	
	public List<Map<String, String>> queryTimedSendList2() {
		return orderDao.queryTimedSendList2();
	}

	public void updateOrderStatus(Map<String, String> map) {
		orderDao.updateOrderEopInfo(map);
	}

	public Map<String, String> queryOrderContactInfo(String orderId) {
		return orderDao.queryOrderContactInfo(orderId);
	}

	public List<Map<String, String>> queryCpContactInfo(String orderId) {
		return orderDao.queryCpContactInfo(orderId);
	}

	public List<Map<String, String>> queryTimedDifferRefundList() {
		return orderDao.queryTimedDifferRefundList();
	}

	public int updateDifferBegin(Map<String, String> paramMap) {
		return orderDao.updateDifferBegin(paramMap);
	}

	public void updateOrderDiffer(Map<String, String> map) {
		orderDao.updateOrderDiffer(map);
	}

	public int queryDifferCountBySeq(String refund_seq) {
		return orderDao.queryDifferCountBySeq(refund_seq);
	}

	public String queryUploadTipTime(String order_id) {
		return orderDao.queryUploadTipTime(order_id);
	}

	public Map<String, String> queryDifferRefundInfo(String order_id) {
		return orderDao.queryDifferRefundInfo(order_id);
	}

	public List<OrderInfo> queryLastestOrderList(Map<String, Object> paramMap) {
		return orderDao.queryLastestOrderList(paramMap);
	}

	public void addRefundStream(List<Map<String, String>> refundList, String order_id) {
		Map<String, String> map = orderDao.queryOrderForRefund(order_id);
		double refund_total = Double.parseDouble(map.get("refund_total")); 
		double refund_money = 0;
		int count = 0;
		for(Map<String, String> refundMap : refundList){
			//验证退款流水中是否包含该车票的退款信息（除拒绝退款数据）,已经有该车票退款数据，则拒绝
			count = orderDao.queryRefundStreamContainCp(refundMap);
			if(count > 0){
				logger.info("[数据库校验]退款发生重复提交，请求被拒绝");
				return;
			}
			
			//退款列表中含有拒绝退款的车票，则删除老的退款流水
			if(!StringUtils.isEmpty(refundMap.get("old_refund_status"))
					&& TrainConsts.REFUND_STREAM_REFUSE.equals(refundMap.get("old_refund_status"))){
				orderDao.deleteRefundStreamOnRefuse(refundMap);
			}
			refund_money = Double.parseDouble(refundMap.get("refund_money"));
			refund_total = AmountUtil.add(refund_total, refund_money);
			refundMap.put("eop_order_id", map.get("eop_order_id"));
			orderDao.addRefundStream(refundMap);
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("refund_total", String.valueOf(refund_total));
		paramMap.put("order_id", order_id);
		//查询未发生退款的票数
		int left_count = orderDao.queryRefundLeftCount(order_id);
		if(left_count==0){
			paramMap.put("can_refund", "0");//不能退款
		}else{
			paramMap.put("can_refund", "1");//能退款
		}
		orderDao.updateOrderRefundTotal(paramMap);
	}

	public List<Map<String, String>> queryRefundStreamList(String order_id) {
		return orderDao.queryRefundStreamList(order_id);
	}

	public List<Map<String, String>> queryTimedRefundStreamList() {
		return orderDao.queryTimedRefundStreamList();
	}

	public int updateRefundStreamBegin(Map<String, String> paramMap) {
		return orderDao.updateRefundStreamBegin(paramMap);
	}

	public void updateOrderStreamStatus(Map<String, String> map) {
		orderDao.updateOrderStreamStatus(map);
	}

	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return orderDao.querySpecTimeBeforeFrom(order_id);
	}

	public List<Map<String, String>> queryOrderRefundForHis() {
		return orderDao.queryOrderRefundForHis();
	}

	public List<Map<String, String>> queryOrderDifferForHis() {
		return orderDao.queryOrderDifferForHis();
	}

	public void addOrderRefundHis(List<Map<String, String>> refundList,
			List<Map<String, String>> differList) {
		for(Map<String, String> map1 : refundList){
			orderDao.addRefundStreamForHis(map1);
			orderDao.updateOrderCanRefundForHis(map1.get("order_id"));
		}
		for(Map<String, String> map2 : differList){
			orderDao.addRefundStreamForHis(map2);
		}
	}

	public String queryBxPayMoneyAtPaySucc(String orderId) {
		return orderDao.queryBxPayMoneyAtPaySucc(orderId);
	}

	public void addMsn(Map<String, String> msn) {
		orderDao.addMsn(msn);
	}

	@Override
	public Map<String, Object> queryAgentOrderNum(Map<String, Object> map) {
		//拼接出票中，出票失败，出票成功和退款的数据
		Map<String,Object> trainMap=orderDao.queryAgentOrderNum(map);
		Map<String,Object> refundMap=orderDao.queryAgentRefundNum(map);
		if(refundMap.get("refundNum")==null){
			//避免空指针异常
		}else{
			trainMap.put("refundNum", refundMap.get("refundNum"));
		}
		
		return trainMap;
	}

	@Override
	public String selectRefundPassengers(String cp_id) {
		return orderDao.selectRefundPassengers(cp_id);
	}

	@Override
	public List<OrderInfo> queryOrderRefundList(Map<String, Object> paramMap) {
		return orderDao.queryOrderRefundList(paramMap);
	}

	@Override
	public List<Map<String, Object>> querySaleReportList(
			Map<String, Object> paramMap) {
		return orderDao.querySaleReportList(paramMap);
	}

	@Override
	public int querySaleReportListCount(Map<String, Object> paramMap) {
		return orderDao.querySaleReportListCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		return orderDao.queryCanRefundStreamList();
	}

	@Override
	public Map<String, String> queryAccountOrderInfo(Map<String, String> param) {
		return orderDao.queryAccountOrderInfo(param);
	}

	@Override
	public Map<String, String> queryRefundCpOrderInfo(Map<String, String> param) {
		return orderDao.queryRefundCpOrderInfo(param);
	}

	@Override
	public void updateCPAlterInfo(Map<String, Object> map) {
		orderDao.updateCPAlterInfo(map);
	}

	@Override
	public void updateRefundInfo(Map<String, Object> map) {
		orderDao.updateRefundInfo(map);
	}

	@Override
	public void updateOrderRefundStatus(Map<String, String> map) {
		orderDao.updateOrderRefundStatus(map);
	}

	@Override
	public void addOrderOptLog(Map<String, String> map) {
		orderDao.addOrderOptLog(map);
	}

	@Override
	//人工出票数
	public int queryManualOrderCount() {
		return orderDao.queryManualOrderCount();
	}

	@Override
	public List<String> queryNeedCheckOptlogList(String orderId) {
		return orderDao.queryNeedCheckOptlogList(orderId);
	}

	@Override
	public int deleteOrder(String orderId) {
		return orderDao.deleteOrder(orderId);
	}

	@Override
	public Map<String, String> queryOrderInfoPssm(String orderId) {
		return orderDao.queryOrderInfoPssm(orderId);
	}

	@Override
	public boolean insertIntoPsOutTicket(String order_id,Map<String, String> orderMap,
			List<Map<String, String>> cpInfoList) {
		boolean result = false;
		try {
			orderDao.insertIntoPsOutTicket(orderMap);
			for(Map<String, String> cpMap:cpInfoList){
				orderDao.insertIntoPsOutTicketCp(cpMap);
			}
			logger.info("insert【送票上门】表格成功order_id:"+order_id);
			orderDao.updateHcOrderTo22(order_id);
			logger.info("update【送票上门】hc表格到正在预定 order_id:"+order_id);
			result = true;
		} catch (Exception e) {
			logger.info("insertIntoPsOutTicket异常"+e);
		}
		return result;
	}

	@Override
	public void insertStuCpInfo(DBStudentInfo stu) {
		orderDao.addStudentInfo(stu);
	}


}
