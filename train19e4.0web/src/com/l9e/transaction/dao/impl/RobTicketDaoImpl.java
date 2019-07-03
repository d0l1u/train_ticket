package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RobTicketDao;
import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicket_History;
import com.l9e.transaction.vo.RobTicket_Notify;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.transaction.vo.RobTicket_Refund;
import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;

/**
 * 19e后台-抢票DAO
 * 
 * @author yangwei01
 * 
 */
@SuppressWarnings("all")
@Repository("robTicketDao")
public class RobTicketDaoImpl extends BaseDao implements RobTicketDao {

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<Map<String, String>> queryRobList(Map<String,
	 * Object> paramMap) { return this.getSqlMapClientTemplate().queryForList(
	 * "robTicket.queryRobList", paramMap); }
	 * 
	 * @Override public int queryRobListCount(Map<String, Object> paramMap) {
	 * 
	 * return getTotalRows("robTicket.queryRobListCount", paramMap); }
	 * 
	 * @Override public int queryRobListCountCp(Map<String, Object> paramMap) {
	 * 
	 * return getTotalRows("robTicket.queryRobListCountCp", paramMap); }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<Map<String, String>> queryRobListCp(Map<String,
	 * Object> paramMap) {
	 * 
	 * return this.getSqlMapClientTemplate().queryForList(
	 * "robTicket.queryRobListCp", paramMap); }
	 */

	@Override
	public void deleteCP(RobTicket_CP cp) {
		this.getSqlMapClientTemplate().delete("robTicket.deleteCP", cp);
	}

	@Override
	public void deleteHistory(RobTicket_History history) {
		this.getSqlMapClientTemplate().delete("robTicket.deleteHistory",
				history);

	}

	@Override
	public void deleteNotify(RobTicket_Notify notify) {
		this.getSqlMapClientTemplate().delete("robTicket.deleteNotify", notify);
	}

	@Override
	public void deleteOrderInfo(RobTicket_OI oi) {
		this.getSqlMapClientTemplate().delete("robTicket.deleteOrderInfo", oi);

	}

	@Override
	public void insertCP(RobTicket_CP cp) {
		this.getSqlMapClientTemplate().insert("robTicket.insertCP", cp);

	}

	@Override
	public void insertHistory(RobTicket_History history) {
		this.getSqlMapClientTemplate().insert("robTicket.insertHistory",
				history);

	}

	@Override
	public void insertNotify(RobTicket_Notify notify) {
		this.getSqlMapClientTemplate().insert("robTicket.insertNotify", notify);

	}

	@Override
	public void insertOrderInfo(RobTicket_OI oi) {
		this.getSqlMapClientTemplate().insert("robTicket.insertOrderInfo", oi);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryCP(Map<String, Object> paramMap) {

		return this.getSqlMapClientTemplate().queryForList("robTicket.queryCP",
				paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryHistory(Map<String, Object> paramMap) {

		return this.getSqlMapClientTemplate().queryForList(
				"robTicket.queryHistory", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryNotify(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"robTicket.queryNotify", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryOrderInfo(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"robTicket.queryOrderInfo", paramMap);
	}

	@Override
	public void updateCP(RobTicket_CP cp) {
		this.getSqlMapClientTemplate().update("robTicket.updateCP", cp);
	}

	@Override
	public void updateHistory(RobTicket_History history) {
		this.getSqlMapClientTemplate().update("robTicket.updateHistory",
				history);
	}

	@Override
	public void updateNotify(RobTicket_Notify notify) {
		this.getSqlMapClientTemplate().update("robTicket.updateNotify", notify);
	}

	@Override
	public void updateOrderInfo(RobTicket_OI oi) {
		this.getSqlMapClientTemplate().update("robTicket.updateOrderInfo", oi);
	}

	@Override
	public RobTicket_CP selectCPByPrimaryKey(RobTicket_CP cp) {
		return (RobTicket_CP) this.getSqlMapClientTemplate().queryForObject(
				"robTicket.selectCPByPrimaryKey");
	}

	@Override
	public RobTicket_History selectHistoryByPrimaryKey(RobTicket_History history) {
		return (RobTicket_History) this.getSqlMapClientTemplate()
				.queryForObject("robTicket.selectHistoryByPrimaryKey");
	}

	@Override
	public RobTicket_Notify selectNotifyByPrimaryKey(RobTicket_Notify notify) {
		return (RobTicket_Notify) this.getSqlMapClientTemplate()
				.queryForObject("robTicket.selectNotifyByPrimaryKey");
	}

	@Override
	public RobTicket_OI selectOrderInfoByPrimaryKey(RobTicket_OI oi) {
		return (RobTicket_OI) this.getSqlMapClientTemplate().queryForObject(
				"robTicket.selectOrderInfoByPrimaryKey",oi);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RobTicket_CP> selectCPsByorderId(RobTicket_OI oi) {
		return this.getSqlMapClientTemplate().queryForList("robTicket.selectCPsByorderId", oi);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RobTicket_OI> selectOrderInfoByConditions(
			Map<String, Object> conditions) {
		return this.getSqlMapClientTemplate().queryForList("robTicket.selectOrderInfoByConditions", conditions);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> selectOrderStatusNum(HashMap<String, String> paraMap) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("robTicket.selectOrderStatusNum", paraMap);
	}

	@Override
	public void deleteCPByOrderInfo(RobTicket_OI oi) {
		this.getSqlMapClientTemplate().delete("robTicket.deleteCPByOrderInfo", oi);
		
	}

	@Override
	public Map<String, String> queryEOPByEopId(String eopOrderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("robTicket.queryEOPByEopId", eopOrderId);
	}

	@Override
	public Map<String, String> queryOrderIdByCtripId(String cripOrderId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("robTicket.queryOrderIdByCtripId", cripOrderId);
	}

	@Override
	public void insertRefund(RobTicket_Refund refund) {
		this.getSqlMapClientTemplate().insert("robTicket.insertRefund", refund);
		
	}

	@Override
	public void updateRefund(RobTicket_Refund refund) {
		this.getSqlMapClientTemplate().update("robTicket.updateRefund", refund);
		
	}

	@Override
	public void updateJLOrderInfo(RobTicket_OI oi) {
		this.getSqlMapClientTemplate().update("robTicket.updateJLOrderInfo", oi);
		
	}

	@Override
	public void insertJLHistory(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("robTicket.insertJlOrderHistory", map);
		
	}

	@Override
	public void updateCPRobSucc(final List<HashMap<String, String>> adultCPS) {
		SqlMapClientTemplate template = this.getSqlMapClientTemplate();
		template.execute(new SqlMapClientCallback<T>() {
			@Override
			public T doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (HashMap<String, String> hashMap : adultCPS) {
					executor.update("robTicket.updateCPRobSucc", hashMap);
				}
				executor.executeBatch();
				return null;
			}
		});
		
	}

	@Override
	public int selectOrderInfoByConditionsCount(Map<String, String> conditions) {
		
		return getTotalRows("robTicket.selectOrderInfoByConditionsCount", conditions);
	}

	@Override
	public void updateFrontBackCP_Refund(String[] cpids,String status) {
		final List<Map<String, String>> list =new ArrayList<Map<String,String>>();
		for (int i = 0; i < cpids.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("cp_id", cpids[i]);
			map.put("status", status);
			list.add(map);
			
		}
		SqlMapClientTemplate template = this.getSqlMapClientTemplate();
		template.execute(new SqlMapClientCallback<T>() {
			@Override
			public T doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (Map<String, String> hashMap : list){
					executor.update("robTicket.updateFrontBackCP_Refund_Front", hashMap);
					executor.update("robTicket.updateFrontBackCP_Refund_Back", hashMap);
				}
				executor.executeBatch();
				return null;
			}
		});
		
	}

	@Override
	public RobTicket_CP queryRefundCp(HashMap<String, Object> map) {
		List<RobTicket_CP> queryForList = this.getSqlMapClientTemplate().queryForList("robTicket.queryRefundCp", map);
		if (queryForList.isEmpty()) {
			return null ; 
		}
		return queryForList.get(0);
	}


	@Override
	public Map<String, String> querySMSRobSucc(String ctripOrderId) {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("robTicket.querySMSRobSucc", ctripOrderId);
	}

	@Override
	public void insertRobRefundFromCtrip(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("robTicket.insertRobRefundFromCtrip", map);
	}
	
	

}
