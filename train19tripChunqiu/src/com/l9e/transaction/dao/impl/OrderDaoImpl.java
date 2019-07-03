package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDao implements OrderDao {

	public void addOrderInfo(OrderInfo orderInfo) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfo", orderInfo);
	}

	public void addOrderInfoBx(OrderInfoBx orderInfoBx) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoBx", orderInfoBx);
	}

	public void addOrderInfoCp(OrderInfoCp orderInfoCp) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoCp", orderInfoCp);
	}
	
	public void addOrderInfoPs(OrderInfoPs orderInfoPs) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoPs", orderInfoPs);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderDetailList", order_id);
	}

	public OrderInfo queryOrderInfo(String order_id) {
		return (OrderInfo) this.getSqlMapClientTemplate().queryForObject("order.queryOrderInfo", order_id);
	}

	public OrderInfoPs queryOrderInfoPs(String order_id) {
		return (OrderInfoPs) this.getSqlMapClientTemplate().queryForObject("order.queryOrderInfoPs", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<OrderInfo> queryOrderList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderList", paramMap);
	}

	public int queryOrderListCount(Map<String, Object> paramMap) {
		return this.getTotalRows("order.queryOrderListCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public void updateOrder(String orderid) {
		this.getSqlMapClientTemplate().update("order.updateOrderForRefunding", orderid);
	}

	@SuppressWarnings("unchecked")
	public void updateRefund(RefundVo refund) {
		this.getSqlMapClientTemplate().update("order.insertRefundForRefund", refund);
	}

	public void updateOrderEopInfo(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("order.updateOrderEopInfo", eopInfo);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedRefundList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedRefundList");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryCpInfoList", order_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryScanedOrderList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryScanedOrderList");
	}

	public int updateScanInfoById(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("order.updateScanInfoById", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryNotifyCpOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryNotifyCpOrderInfo", order_id);
	}

	public void updateOrderTimeOut(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderTimeOut", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedSendList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedSendList");
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderContactInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderContactInfo", orderId);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCpContactInfo(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("order.queryCpContactInfo", orderId);
	}

	public void updateOrderRefund(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefund", eopInfo);
	}

	public void updateBxStatusNotSend(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateBxStatusNotSend", order_id);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderDiffer(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderDiffer", order_id);
	}

	public void addOrderDiffer(Map<String, String> differMap) {
		this.getSqlMapClientTemplate().insert("order.addOrderDiffer", differMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedDifferRefundList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedDifferRefundList");
	}

	public int updateDifferBegin(Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().update("order.updateDifferBegin", paramMap);
	}

	public void updateOrderDiffer(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderDiffer", map);
	}

	public int queryDifferCountBySeq(String refund_seq) {
		return this.getTotalRows("order.queryDifferCountBySeq", refund_seq);
	}

	public String queryUploadTipTime(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("order.queryUploadTipTime", order_id);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryDifferRefundInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryDifferRefundInfo", order_id);
	}

	public void deleteOldRefund(String order_id) {
		this.getSqlMapClientTemplate().delete("order.deleteOldRefund", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<OrderInfo> queryLastestOrderList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("order.queryLastestOrderList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderForRefund(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderForRefund", order_id);
	}

	public void updateOrderRefundTotal(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefundTotal", paramMap);
	}

	public void addRefundStream(Map<String, String> refundMap) {
		this.getSqlMapClientTemplate().insert("order.addRefundStream", refundMap);
	}

	public int queryRefundStreamContainCp(Map<String, String> refundMap) {
		return this.getTotalRows("order.queryRefundStreamContainCp", refundMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundStreamList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryRefundStreamList", order_id);
	}

	public int queryRefundLeftCount(String order_id) {
		return this.getTotalRows("order.queryRefundLeftCount",order_id);
	}

	public void deleteRefundStreamOnRefuse(Map<String, String> refundMap) {
		this.getSqlMapClientTemplate().delete("order.deleteRefundStreamOnRefuse", refundMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedRefundStreamList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedRefundStreamList");
	}

	public void addOrderOptLog(Map<String, String> logMap) {
		this.getSqlMapClientTemplate().insert("order.addOrderOptLog", logMap);
	}

	public int updateRefundStreamBegin(Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().update("order.updateRefundStreamBegin", paramMap);
	}

	public void updateOrderStreamStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderStreamStatus", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.querySpecTimeBeforeFrom", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrderRefundForHis() {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderRefundForHis");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrderDifferForHis() {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderDifferForHis");
	}

	public void addRefundStreamForHis(Map<String, String> map1) {
		this.getSqlMapClientTemplate().insert("order.addRefundStreamForHis", map1);
	}

	public void updateOrderCanRefundForHis(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateOrderCanRefundForHis", order_id);
	}

	public void addOrderInfoBxfp(Map<String, String> bxfpMap) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoBxfp", bxfpMap);
	}

	public String queryBxPayMoneyAtPaySucc(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("order.queryBxPayMoneyAtPaySucc", orderId);
	}

	public void addMsn(Map<String, String> msn) {
		this.getSqlMapClientTemplate().insert("order.addMsn", msn);
	}

	@Override
	public void updateOrderPayNo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderPayNo", map);
	}

	@Override
	public int updateOrderPayInfo(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("order.updateOrderPayInfo", map);
		
	}

	@Override
	public RefundVo queryRefundSInfo(String orderId) {
		return (RefundVo) this.getSqlMapClientTemplate().queryForObject("order.queryRefundSInfo", orderId);
	}

	@Override
	public int updateRefundStreamInfo(Map<String,String> map) {
		return this.getSqlMapClientTemplate().update("order.updateRefundSInfo", map);
	}

	@Override
	public void updateRefundSStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateRefundSStatus", map);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTimedOrderReList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedOrderReList");
	}

	@Override
	public void updateReNotifyStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateReNotifyStatus", map);
		
	}

	@Override
	public void updateReNoStatusTo22(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateReNoStatusTo22", map);
		
	}

	@Override
	public void updateReNotifyNum(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateReNotifyNum", map);
		
	}

	@Override
	public void addResultNotifyInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("order.addResultNotifyInfo", map);
		
	}

	@Override
	public int updateResultNotifyInfo(Map<String, String> map) {
		return this.getSqlMapClientTemplate().update("order.updateResultNotifyInfo", map);
		
	}

	@Override
	public void addOrderinfoHistory(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("order.addOrderinfoHistory", map);
	}

	@Override
	public void updateOrderStreamResultStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderStreamResultStatus", map);
	}

	@Override
	public Map<String, String> queryAccountOrderInfo(Map<String, String> param) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryAccountOrderInfo", param);
	}

	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("order.queryCanRefundStreamList");
	}

	@Override
	public Map<String, String> queryRefundCpOrderInfo(Map<String, String> param) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryRefundCpOrderInfo", param);
	}

	@Override
	public void updateCPAlterInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("order.updateCPAlterInfo", map);
	}

	@Override
	public void updateOrderRefundStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefundStatus", map);
	}

	@Override
	public void updateRefundInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("order.updateRefundInfo", map);
	}

	@Override
	public String queryPayChannel(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("order.queryPayChannel",order_id);
	}
}
