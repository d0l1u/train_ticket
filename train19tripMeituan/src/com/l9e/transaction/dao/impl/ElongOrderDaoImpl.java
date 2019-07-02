package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongOrderDao;
import com.l9e.transaction.vo.ElongOrderInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.ElongPassengerInfo;
@Repository("elongOrderDao")
public class ElongOrderDaoImpl extends BaseDao implements ElongOrderDao{

	
	@Override
	public List<ElongOrderInfoCp> queryOrderCpInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("elongOrder.queryOrderCpInfo", order_id);
	}


	@Override
	public void insertElongOrder(Map<String, Object> orderInfo) {
		this.getSqlMapClientTemplate().insert("elongOrder.addOrder", orderInfo);
	}


	@Override
	public void insertElongOrderLogs(ElongOrderLogsVo log) {
		this.getSqlMapClientTemplate().insert("elongOrder.addLogs", log);		
	}

	@Override
	public int queryOrderCount(String order_id) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("elongOrder.queryOrderCount", order_id);
	}

	@Override
	public void updateOrderStatus(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("elongOrder.updateOrderStatus", map);
	}
	@Override
	public List<Map<String, Object>> querySendOrderCpsInfo(String order_id) {
		return  this.getSqlMapClientTemplate().queryForList("elongOrder.querySendOrderCpsInfo",order_id);
	}
	@Override
	public void updateCpOrderInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("elongOrder.updateCpOrderInfo",map);
	}
	@Override
	public void updateOrderInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("elongOrder.updateOrderInfo",map);
	}
	@Override
	public Map<String, Object> queryOrderInfo(String order_id) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("elongOrder.queryOrderInfo",order_id);
	}
	@Override
	public void updateRefundStream(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("elongOrder.updateRefundStream",map);
	}
	@Override
	public String queryRefundStatus(Map<String, String> paramMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("elongOrder.queryRefundStatus",paramMap);
	}
	@Override
	public void insertRefundOrder(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("elongOrder.insertRefundOrder",paramMap);
	}
	@Override
	public Map<String, Object> queryrefundInfo(Map<String, String> params) {
		return	(Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("elongOrder.queryrefundInfo",params); 
	}
	@Override
	public void addOrderInfo(ElongOrderInfo orderInfo) {
		this.getSqlMapClientTemplate().insert("elongOrder.addOrderInfo",orderInfo);
	}
	@Override
	public void addPassengerInfo(ElongPassengerInfo p) {
		this.getSqlMapClientTemplate().insert("elongOrder.addPassengerInfo",p);
	}
	@Override
	public void updateRefundStatus(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("elongOrder.updateRefundStatus",paramMap);
	}
	@Override
	public void updateOrderNoticeStatus(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("elongOrder.updateOrderNoticeStatus",param);
	}
	@Override
	public String queryCpPayMoney(Map<String, String> paramMap) {
		return	(String)this.getSqlMapClientTemplate().queryForObject("elongOrder.queryCpPayMoney",paramMap); 		
	}
	@Override
	public List<Map<String, Object>> getOfflineRefundRefundList() {
		return this.getSqlMapClientTemplate().queryForList("elongOrder.getOfflineRefundRefundList");
	}
	@Override
	public List<Map<String, Object>> getTcOfflineRefundRefundList() {
		return this.getSqlMapClientTemplate().queryForList("elongOrder.getTcOfflineRefundRefundList");
	}

	@Override
	public void updateOfflineNoticeStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("elongOrder.updateOfflineNoticeStatus",map);
	}


	@Override
	public void addOfflineRefund(Map<String, String> params) {
		this.getSqlMapClientTemplate().insert("elongOrder.addOfflineRefund",params);
	}


	@Override
	public String queryCpid(Map<String, String> pMap) {
		return (String) this.getSqlMapClientTemplate().queryForObject("elongOrder.queryCpid", pMap);
	}




	
}
