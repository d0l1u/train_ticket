package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.AmountUtil;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = Logger
			.getLogger(OrderServiceImpl.class);

	@Resource
	private OrderDao orderDao;

	@Resource
	private CommonDao commonDao;

	public String addOrder(OrderInfo orderInfo, OrderInfoPs orderInfoPs,
			List<OrderInfoCp> orderInfoCpList,
			List<OrderInfoBx> orderInfoBxList, Map<String, String> bxfpMap) {

		orderDao.addOrderInfo(orderInfo);// 订单

		for (OrderInfoCp orderInfoCp : orderInfoCpList) {// 车票
			orderDao.addOrderInfoCp(orderInfoCp);
		}

		for (OrderInfoBx orderInfoBx : orderInfoBxList) {// 保险
			orderDao.addOrderInfoBx(orderInfoBx);
		}
		orderDao.addOrderInfoPs(orderInfoPs);// 配送

		if (bxfpMap != null) {
			orderDao.addOrderInfoBxfp(bxfpMap);
		}

		return orderInfo.getOrder_id();
	}

	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return orderDao.queryOrderDetailList(order_id);
	}

	public OrderInfo queryOrderInfo(String order_id) {
		return orderDao.queryOrderInfo(order_id);
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
		// orderDao.updateOrder(refund.getOrder_id());

		// 如果是拒绝退款，则先删除原来的退款信息
		if (!StringUtils.isEmpty(old_refund_status)
				&& TrainConsts.REFUND_REFUSE.equals(old_refund_status)) {
			orderDao.deleteOldRefund(refund.getOrder_id());
		}

		orderDao.updateRefund(refund);
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

	public void addRefundStream(List<Map<String, String>> refundList,
			String order_id) {
		Map<String, String> map = orderDao.queryOrderForRefund(order_id);
		double refund_total = Double.parseDouble(map.get("refund_total"));
		double refund_money = 0;
		int count = 0;
		for (Map<String, String> refundMap : refundList) {
			// 验证退款流水中是否包含该车票的退款信息（除拒绝退款数据）,已经有该车票退款数据，则拒绝
			count = orderDao.queryRefundStreamContainCp(refundMap);
			if (count > 0) {
				logger.info("[数据库校验]退款发生重复提交，请求被拒绝");
				return;
			}

			// 退款列表中含有拒绝退款的车票，则删除老的退款流水
			if (!StringUtils.isEmpty(refundMap.get("old_refund_status"))
					&& TrainConsts.REFUND_STREAM_REFUSE.equals(refundMap
							.get("old_refund_status"))) {
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
		// 查询未发生退款的票数
		int left_count = orderDao.queryRefundLeftCount(order_id);
		if (left_count == 0) {
			paramMap.put("can_refund", "0");// 不能退款
		} else {
			paramMap.put("can_refund", "1");// 能退款
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
		for (Map<String, String> map1 : refundList) {
			orderDao.addRefundStreamForHis(map1);
			orderDao.updateOrderCanRefundForHis(map1.get("order_id"));
		}
		for (Map<String, String> map2 : differList) {
			orderDao.addRefundStreamForHis(map2);
		}
	}

	public String queryBxPayMoneyAtPaySucc(String orderId) {
		return orderDao.queryBxPayMoneyAtPaySucc(orderId);
	}

	public void addMsn(Map<String, String> msn) {
		orderDao.addMsn(msn);
	}

	public void updateOrderPayNo(Map<String, String> map) {
		orderDao.updateOrderPayNo(map);
	}

	public List<String> queryPassengerList(String orderId) {
		return orderDao.queryPassengerList(orderId);
	}

	public void updateRefundStream(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		orderDao.updateRefundStream(paramMap);
	}

}
