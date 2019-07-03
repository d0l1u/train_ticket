package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.ElongOrderInfo;
import com.l9e.transaction.vo.ElongPassengerInfo;

@Repository("orderDao")
public class OrderDaoImpl  extends BaseDao  implements OrderDao{

	@Override
	public void addOrderInfo(DBOrderInfo orderInfo) {
		this.getSqlMapClientTemplate().insert("order.addOrderInfo",orderInfo);
	}
	@Override
	public void addPassengerInfo(DBPassengerInfo p) {
		this.getSqlMapClientTemplate().insert("order.addPassengerInfo",p);
	}
	@Override
	public int updateOrderStatus(Map<String, String> map) {
		return (Integer)this.getSqlMapClientTemplate().update("order.updateOrderStatus",map);
	}
	@Override
	public List<DBPassengerInfo> queryOrderCpsInfo(String order_id) {
		return (List<DBPassengerInfo>)this.getSqlMapClientTemplate().queryForList("order.queryOrderCpsInfo",order_id);
	}
	@Override
	public DBOrderInfo queryOrderInfo(String order_id) {
		return (DBOrderInfo)this.getSqlMapClientTemplate().queryForObject("order.queryOrderInfo",order_id);
	}
	@Override
	public void updateOrderNoticeStatus(Map<String, String> orderNotice) {
		this.getSqlMapClientTemplate().update("order.updateOrderNoticeStatus",orderNotice);
	}
	@Override
	public String queryOutTicketOrderStatusByOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("order.queryOutTicketOrderStatusByOrderId",order_id);
	}
	@Override
	public Map<String, Object> queryOutTicketOrderInfo(String order_id) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("order.queryOutTicketOrderInfo",order_id);
	}
	@Override
	public void updateOrderBookNoticeStatus(Map<String, String> orderNotice) {
		
	}
	@Override
	public String queryOrderStatusByOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("order.queryOrderStatusByOrderId",order_id);
	}
	@Override
	public void updateCpOrderInfo(Map<String, String> cpInfo) {
		this.getSqlMapClientTemplate().update("order.updateCpOrderInfo",cpInfo);
	}
	@Override
	public void updateOrderInfo(DBOrderInfo orderInfo) {
		this.getSqlMapClientTemplate().update("order.updateOrderInfo",orderInfo);
	}
	@Override
	public String queryRefundStatus(Map<String, String> paramMap) {
		return 	(String)this.getSqlMapClientTemplate().queryForObject("order.queryRefundStatus",paramMap);
	}
	@Override
	public void insertRefundOrder(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("order.insertRefundOrder",paramMap);		
	}
	@Override
	public Map<String, Object> queryOutTicketCpInfo(String cp_id) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("order.queryOutTicketCpInfo",cp_id);	
	}
	@Override
	public Map<String, Object> queryAllChannelNotify(String order_id) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("order.queryAllChannelNotify",order_id);	
	}
	@Override
	public void updateOrderPayMoney(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("order.updateOrderPayMoney",paramMap);
	}
	@Override
	public void addStudentInfo(DBStudentInfo s) {
		this.getSqlMapClientTemplate().insert("order.addStudentInfo",s);
	}
	@Override
	public String querySeatNo(Map<String, String> cpInfo) {
		return (String)this.getSqlMapClientTemplate().queryForObject("order.querySeatNo",cpInfo);	
	}
	@Override
	public List<String> queryChangeIdsByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("order.queryChangeIdsByOrderId",order_id);
	}
	@Override
	public Map<String, String> queryUsedAccountInfo(Integer id) {
		return (Map<String, String>) getSqlMapClientTemplate().queryForObject("order.selectAccountById", id);
	}
	
	/**
	 * 查询订单中出发与到达车站的三字码 
	 * @param map
	 * @return 出发与到达车站三字码列表
	 */
	@Override
	public List<DBOrderInfo> queryCity3CList(Map<String, String> map) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("order.queryCity3CList", map);
	}
}
