package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDao implements OrderDao {

	public void addOrderInfo(OrderInfo orderInfo) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfo", orderInfo);
	}

	public void addOrderInfoBx(OrderInfoBx orderInfoBx) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoBx",
				orderInfoBx);
	}

	public void addOrderInfoCp(OrderInfoCp orderInfoCp) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoCp",
				orderInfoCp);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryOrderDetailList", order_id);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderInfo(String order_id) {
		Map<String, String> map = (Map<String, String>) this.getSqlMapClientTemplate().queryForList(
				"order.queryOrderInfo", order_id).get(0);
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrderList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryOrderList", paramMap);
	}

	public int queryOrderListCount(Map<String, Object> paramMap) {
		return this.getTotalRows("order.queryOrderListCount", paramMap);
	}



	public void updateOrderEopInfo(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("order.updateOrderEopInfo",
				eopInfo);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryCpInfoList", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryScanedOrderList() {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryScanedOrderList");
	}

	public int updateScanInfoById(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update(
				"order.updateScanInfoById", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryNotifyCpOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("order.queryNotifyCpOrderInfo", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedSendList() {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryTimedSendList");
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCpContactInfo(String orderId) {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryCpContactInfo", orderId);
	}

	public void updateOrderRefund(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefund",
				eopInfo);
	}

	public void updateBxStatusNotSend(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateBxStatusNotSend",
				order_id);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderDiffer(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("order.queryOrderDiffer", order_id);
	}

	public String queryUploadTipTime(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"order.queryUploadTipTime", order_id);
	}


	public void deleteOldRefund(String order_id) {
		this.getSqlMapClientTemplate()
				.delete("order.deleteOldRefund", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<OrderInfo> queryLastestOrderList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryLastestOrderList", paramMap);
	}


	public void updateOrderRefundTotal(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefundTotal",
				paramMap);
	}

	public void addRefundStream(Map<String, String> refundMap) {
		this.getSqlMapClientTemplate().insert("order.addRefundStream",
				refundMap);
	}

	public int queryRefundStreamContainCp(Map<String, String> refundMap) {
		return this.getTotalRows("order.queryRefundStreamContainCp", refundMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundStreamList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryRefundStreamList", order_id);
	}

	public int queryRefundLeftCount(String order_id) {
		return this.getTotalRows("order.queryRefundLeftCount", order_id);
	}

	public void deleteRefundStreamOnRefuse(Map<String, String> refundMap) {
		this.getSqlMapClientTemplate().delete(
				"order.deleteRefundStreamOnRefuse", refundMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedRefundStreamList() {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryTimedRefundStreamList");
	}

	public void addOrderOptLog(Map<String, String> logMap) {
		this.getSqlMapClientTemplate().insert("order.addOrderOptLog", logMap);
	}

	public int updateRefundStreamBegin(Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().update(
				"order.updateRefundStreamBegin", paramMap);
	}

	public void updateOrderStreamStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderStreamStatus",
				map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("order.querySpecTimeBeforeFrom", order_id);
	}


	public void addOrderInfoBxfp(Map<String, String> bxfpMap) {
		this.getSqlMapClientTemplate()
				.insert("order.addOrderInfoBxfp", bxfpMap);
	}

	public String queryBxPayMoneyAtPaySucc(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"order.queryBxPayMoneyAtPaySucc", orderId);
	}

	public void updateOrderPayNo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderPayNo", map);
	}

	public List<String> queryPassengerList(String orderId) {
		return this.getSqlMapClientTemplate().queryForList(
				"order.queryPassengerList", orderId);
	}

	public void updateRefundStream(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateRefundStream",
				paramMap);

	}

	@Override
	public void updateOrderInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderInfo", map);
		
	}

}
