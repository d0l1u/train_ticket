package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookStuInfo;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDao implements OrderDao {
	@Override
	public void addOrderInfo(OrderInfo orderInfo) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfo", orderInfo);
	}
	@Override
	public void addOrderInfoBx(OrderInfoBx orderInfoBx) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoBx", orderInfoBx);
	}
	@Override
	public void addOrderInfoCp(OrderInfoCp orderInfoCp) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoCp", orderInfoCp);
	}
	@Override
	public void addOrderInfoPs(OrderInfoPs orderInfoPs) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoPs", orderInfoPs);
	}
	@Override
	public void addBookStuInfo(BookStuInfo bookStuInfo) {
		this.getSqlMapClientTemplate().update("order.addBookStuInfo", bookStuInfo);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderDetailList", order_id);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderContactInfo(String orderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderContactInfo", orderId);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCpContactInfo(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("order.queryCpContactInfo", orderId);
	}
	@Override
	public OrderInfo queryOrderInfo(String order_id) {
		return (OrderInfo) this.getSqlMapClientTemplate().queryForObject("order.queryOrderInfo", order_id);
	}
	@Override
	public OrderInfoPs queryOrderInfoPs(String order_id) {
		return (OrderInfoPs) this.getSqlMapClientTemplate().queryForObject("order.queryOrderInfoPs", order_id);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<OrderInfo> queryOrderList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderList", paramMap);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<BookDetailInfo> queryOrderCpList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderCpList", order_id);
	}
	@Override
	public int queryOrderListCount(Map<String, Object> paramMap) {
		return this.getTotalRows("order.queryOrderListCount", paramMap);
	}
	@Override
	public void updateOrder(String orderid) {
		this.getSqlMapClientTemplate().update("order.updateOrderForRefunding", orderid);
	}
	@Override
	public void updateOrderStatus(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("order.updateOrderStatus", eopInfo);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedRefundList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedRefundList");
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryCpInfoList", order_id);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> queryNotifyCpOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryNotifyCpOrderInfo", order_id);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedSendList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedSendList");
	}
	@Override
	public void updateOrderRefund(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefund", eopInfo);
	}
	@Override
	public void updateBxStatusNotSend(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateBxStatusNotSend", order_id);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderDiffer(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderDiffer", order_id);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> queryOrderForRefund(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderForRefund", order_id);
	}
	@Override
	public void updateOrderRefundTotal(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefundTotal", paramMap);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundStreamList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryRefundStreamList", order_id);
	}
	@Override
	public void addOrderOptLog(Map<String, String> logMap) {
		this.getSqlMapClientTemplate().insert("order.addOrderOptLog", logMap);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.querySpecTimeBeforeFrom", order_id);
	}
	@Override
	public void addOrderInfoBxfp(Map<String, String> bxfpMap) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoBxfp", bxfpMap);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<String> queryCp_idList(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("order.queryCp_idList", orderId);
	}
	@SuppressWarnings("unchecked")
	public List<String> queryCp_idByIds(Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("order.queryCp_idByIds", paramMap);
	}

	@Override
	public String queryOrderStatusById(Map<String, String> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("order.queryOrderStatusById", paramMap);
	}
	
	@Override
	public String queryCpOrderinfoStatusById(Map<String, String> paramMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("order.queryCpOrderinfoStatusById", paramMap);
	}

	@Override
	public void updateOrderStatusById(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateOrderStatusById", paramMap);
	}
	@Override
	public int queryOrderBxCount(Map<String, String> paramMap) {
		return this.getTotalRows("order.queryOrderBxCount", paramMap);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryOrderBxInfo(Map<String, String> paramMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderBxInfo", paramMap);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryOrderInfoByMap(Map<String, String> paramMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryOrderInfoByMap", paramMap);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryMerchantInfoByOrderId(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryMerchantInfoByOrderId", order_id);
	}
	@Override
	public void updateOrderEopInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderEopInfo", map);
	}
	@Override
	public void updateOrderPayFailReason(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderPayFailReason", map);
	}
	@Override
	public void deleteOrderInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("order.deleteOrderInfo", order_id);
	}
	@Override
	public String queryMsgChannel(String order_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("order.queryMsgChannel", order_id);
	}
	@SuppressWarnings("unchecked")
	public List<String> queryRefund_cp_list(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("order.queryRefund_cp_list",orderId);
	}
	@SuppressWarnings("unchecked")
	public List<OrderInfoBx> queryBxInfosById(Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("order.queryBxInfosById",paramMap);
	}
	@Override
	public int updateRefuseOrderStatus(Map<String, String> param) {
		return this.getSqlMapClientTemplate().update("order.updateRefuseOrderStatus",param);
	}
	@Override
	public void insertOrderLogs(ExternalLogsVo logs) {
		this.getSqlMapClientTemplate().insert("order.insertOrderLogs",logs);
	}
	@Override
	public void addAlterOrderinfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("order.addAlterOrderinfo",map);
	}
	@Override
	public int queryAlterOrderifoNum(Map<String, String> param) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("order.queryAlterOrderifoNum",param);
	}
	@Override
	public void addAlterOrderNotify(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("order.addAlterOrderNotify",param);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryFeeModel(String merchant_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryFeeModel",merchant_id);
	}
	@Override
	public void updateMerchantTicketNum(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateMerchantTicketNum",order_id);
	}
	@Override
	public String queryOrderIdByEop(String eop_order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("order.queryOrderIdByEop",eop_order_id);
	}
	@Override
	public void updateRefundOrderRepeatNotify(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("order.updateRefundOrderRepeatNotify",param);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryCpSizeAndPrice(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryCpSizeAndPrice",order_id);
	}
	@Override
	public void addOrderBookNotifyInfo(Map<String,Object> map) {
		this.getSqlMapClientTemplate().insert("order.addOrderBookNotifyInfo",map);
	}
	@Override
	public void updateOrderWaitPayMoney(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderWaitPayMoney",map);
	}
	@Override
	public void deleteOrderCpInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("order.deleteOrderCpInfo",order_id);
	}
	
	@Override
	public void deleteOrderBxInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("order.deleteOrderBxInfo",order_id);
	}
	@Override
	public void updateBookSuccessTime(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateBookSuccessTime",order_id);
	}
	@Override
	public String queryRefundStreamSeq(Map<String, String> param) {
		return (String)this.getSqlMapClientTemplate().queryForObject("order.queryRefundStreamSeq",param);
	}
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryAccountOrderInfo(Map<String, String> param) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("order.queryAccountOrderInfo", param);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryCanRefundStreamList");
	}
	@SuppressWarnings("unchecked")
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
	public void updateCPOrderInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("order.updateCPOrderInfo", map);
	}
	@Override
	public int queryRefundCount(String refundSeq) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("order.queryRefundCount", refundSeq);
	}
	@Override
	public void updateExtRefundNotifyStatus(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("order.updateExtRefundNotifyStatus", map);
	}
	@Override
	public int queryHistoryByOrderId(String orderId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("order.queryHistoryByOrderId", orderId);
	}
	@Override
	public OrderInfoCp queryCpInfoByCpId(String cpId) {
		return (OrderInfoCp)this.getSqlMapClientTemplate().queryForObject("order.queryCpInfoByCpId", cpId);
	}
	@Override
	public String selectOrderLog(Map<String, Object> paramMap) {
		return (String)getSqlMapClientTemplate().queryForObject("order.selectOrderLog", paramMap);

	}
	
	
}
