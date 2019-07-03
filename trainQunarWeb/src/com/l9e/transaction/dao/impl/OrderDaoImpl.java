package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoTrip;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDao implements OrderDao {

	@Override
	public int queryOrderCountByNo(String order_id) {
		return this.getTotalRows("order.queryOrderCountByNo", order_id);
	}

	@Override
	public int queryOrderCPCountByNo(String order_id) {
		return this.getTotalRows("order.queryOrderCPCountByNo", order_id);
	}

	
	@Override
	public void addQunarOrder(OrderInfo orderInfo) {
		this.getSqlMapClientTemplate().insert("order.addQunarOrder", orderInfo);
	}

	@Override
	public void addQunarOrderCp(OrderInfoCp cpInfo) {
		this.getSqlMapClientTemplate().insert("order.addQunarOrderCp", cpInfo);
	}

	@Override
	public void addQunarOrderNotify(String order_id, String order_type, String cp_notify_status) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("order_type", order_type);
		paramMap.put("cp_notify_status", cp_notify_status);
		this.getSqlMapClientTemplate().insert("order.addQunarOrderNotify", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTimedOutTicketList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedOutTicketList");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOrderCpList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderCpList", order_id);
	}

	@Override
	public void updateQunarOutNotifyBegin(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateQunarOutNotifyBegin", order_id);
	}

	@Override
	public void updateQunarOutNotifyEnd(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateQunarOutNotifyEnd", order_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTimedWaitingRefundList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedWaitingRefundList");
	}

	@Override
	public void updateQunarRefundNotifyBegin(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateQunarRefundNotifyBegin", map);;
	}

	@Override
	public void updateQunarRefundNotifyEnd(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateQunarRefundNotifyEnd", map);;
	}

	@Override
	public int queryRefundCountByNo(String order_id) {
		return this.getTotalRows("order.queryRefundCountByNo", order_id);
	}

	@Override
	public void addQunarRefund(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("order.addQunarRefund", map);
	}

	@Override
	public OrderInfo queryOrderInfoById(String order_id) {
		return (OrderInfo) this.getSqlMapClientTemplate().queryForObject("order.queryOrderInfoById", order_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTimedCpSysList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryTimedCpSysList");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryCpInfoList", order_id);
	}

	@Override
	public void updateCpSysNotifyBegin(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateCpSysNotifyBegin", order_id);
	}

	@Override
	public void updateCpSysOutNotifyEnd(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateCpSysOutNotifyEnd", order_id);
	}

	@Override
	public void updateCpOrderWithCpNotify(Map<String, String> cpMap) {
		this.getSqlMapClientTemplate().update("order.updateCpOrderWithCpNotify", cpMap);
	}

	@Override
	public void updateOrderWithCpNotify(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateOrderWithCpNotify", paramMap);
	}

	@Override
	public void addOrderInfoLog(Map<String, String> logMap) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfoLog", logMap);
	}

	@Override
	public void updateOutNotifyPrepare(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateOutNotifyPrepare", order_id);
	}

	@Override
	public String queryQunarSysSetting(String key) {
		return (String) this.getSqlMapClientTemplate().queryForObject("order.queryQunarSysSetting", key);
	}

	@Override
	public void addQunarOrderTrip(OrderInfoTrip trip) {
		this.getSqlMapClientTemplate().insert("order.addQunarOrderTrip", trip);
	}

	@Override
	public OrderInfoTrip queryTripOrderInfoById(String trip_id) {
		return (OrderInfoTrip) this.getSqlMapClientTemplate().queryForObject("order.queryTripOrderInfoById", trip_id);
	}

	@Override
	public void updateTripOrderWithCpNotify(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateTripOrderWithCpNotify", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTripListByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryTripListByOrderId", order_id);
	}

	@Override
	public int queryOutTicketNotifyCount(String order_id) {
		return this.getTotalRows("order.queryOutTicketNotifyCount", order_id);
	}

	@Override
	public void updateOrderNotifyFail(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateOrderNotifyFail", order_id);
	}

	@Override
	public List<Map<String,String>> queryPayMoneyByOrderId(String order_id) {
		return (List<Map<String,String>>) this.getSqlMapClientTemplate().queryForList("order.queryPayMoneyByOrderId", order_id);
	}

	@Override
	public int queryRefundCountByRefuse(String order_id) {
		return this.getTotalRows("order.queryRefundCountByRefuse", order_id);
	}

	@Override
	public Map<String, String> queryRefundStatusByNo(String order_id) {
		return  (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("order.queryRefundStatusByNo", order_id);
	}

	@Override
	public void updateQunarRefund(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateQunarRefund", order_id);
	}

	@Override
	public void updateQunarRefundStatus(String order_id) {
		this.getSqlMapClientTemplate().update("order.updateQunarRefundStatus", order_id);
	}

	@Override
	public void updateCPAlterInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateCPAlterInfo", map);
	}

	@Override
	public void updateCPRefundInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateCPRefundInfo", map);
	}

	@Override
	public void updateRefundInfoSingle(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateRefundInfoSingle", map);
	}

	@Override
	public void updateQunarCPRefundMoney(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("order.updateQunarCPRefundMoney", map);
	}

	@Override
	public Map<String, String> queryAccountOrderInfo(Map<String, String> param) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryAccountOrderInfo",param);
	}

	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		return (List<Map<String,String>>) this.getSqlMapClientTemplate().queryForList("order.queryCanRefundStreamList");
	}

	@Override
	public Map<String, String> queryRefundCpOrderInfo(Map<String, String> param) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryRefundCpOrderInfo",param);
	}

	@Override
	public void updateOrderRefundStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("order.updateOrderRefundStatus", map);
	}

	@Override
	public int queryOrderCpNoRefundNum(String order_id) {
		return this.getTotalRows("order.queryOrderCpNoRefundNum", order_id);
	}

	@Override
	public List<Map<String, String>> queryOrderCpRefundInfoList(String order_id) {
		return (List<Map<String,String>>) this.getSqlMapClientTemplate().queryForList("order.queryOrderCpRefundInfoList",order_id);
	}

	@Override
	public List<Map<String, String>> queryBookResultList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryBookResultList");
	}

	@Override
	public int queryBookTicketNotifyCount(String orderId) {
		return this.getTotalRows("order.queryBookTicketNotifyCount", orderId);
	}

	@Override
	public void updateOrderBookNotifyFail(String orderId) {
		this.getSqlMapClientTemplate().update("order.updateOrderBookNotifyFail", orderId);
	}

	@Override
	public void updateQunarBookNotifyEnd(String orderId) {
		this.getSqlMapClientTemplate().update("order.updateQunarBookNotifyEnd", orderId);
	}

	@Override
	public void updateQunarBookNotifyBegin(String orderId) {
		this.getSqlMapClientTemplate().update("order.updateQunarBookNotifyBegin", orderId);
	}

	
	
	
	
	@Override
	public int queryPayNotifyCount(String orderNo) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("order.queryPayNotifyCount", orderNo);
	}

	@Override
	public List<Map<String, String>> queryWaitPayOrderList() {
		return this.getSqlMapClientTemplate().queryForList("order.queryWaitPayOrderList");
	}

	@Override
	public void updateOrderPayNotifyFail(String orderNo) {
		this.getSqlMapClientTemplate().update("order.updateOrderPayNotifyFail", orderNo);
	}

	@Override
	public void updateQunarPayNotifyBegin(String orderNo) {
		this.getSqlMapClientTemplate().update("order.updateQunarPayNotifyBegin", orderNo);
	}

	@Override
	public void updateQunarPayNotifyEnd(String orderNo) {
		this.getSqlMapClientTemplate().update("order.updateQunarPayNotifyEnd", orderNo);
	}
	
	


	@Override
	public void addOrderInfoByBackup(String order_id) {
		this.getSqlMapClientTemplate().update("order.addOrderInfoByBackup", order_id);
	}
	
	@Override
	public void addOrderCpInfoByBackup(String order_id) {
		this.getSqlMapClientTemplate().update("order.addOrderCpInfoByBackup", order_id);
	}

	@Override
	public Map<String, String> queryAccountOrderBackupInfo(
			Map<String, String> param) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryAccountOrderBackupInfo",param);
	}

	@Override
	public Map<String, String> queryAccountInfo(String orderNo) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("order.queryAccountInfo", orderNo);
	}

	@Override
	public void updateBookNotifyPrepare(String orderNo) {
		this.getSqlMapClientTemplate().update("order.updateBookNotifyPrepare", orderNo);
	}

	@Override
	public List<Map<String, String>> queryOrderList(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("order.queryOrderList", param);
	}

	@Override
	public List<Map<String, String>> queryPassengerList(Map<String, String> map) {
		return this.getSqlMapClientTemplate().queryForList("order.queryPassengerList", map);
	}

	@Override
	public Integer queryOrderCount(Map<String, Object> param) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("order.queryOrderCount", param);
	}

	@Override
	public void addStudentInfo(DBStudentInfo s) {
		this.getSqlMapClientTemplate().insert("order.addStudentInfo", s);
	}
}
