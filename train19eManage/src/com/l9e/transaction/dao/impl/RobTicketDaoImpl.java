package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RobTicketDao;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_History;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_Notify;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_OI;

/**
 * 19e后台-抢票DAO
 * 
 * @author yangwei01
 * 
 */
@Repository("robTicketDao")
public class RobTicketDaoImpl extends BaseDao implements RobTicketDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryRobList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"robTicket.queryRobList", paramMap);
	}

	@Override
	public int queryRobListCount(Map<String, Object> paramMap) {

		return getTotalRows("robTicket.queryRobListCount", paramMap);
	}

	@Override
	public int queryRobListCountCp(Map<String, Object> paramMap) {

		return getTotalRows("robTicket.queryRobListCountCp", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryRobListCp(Map<String, Object> paramMap) {

		return this.getSqlMapClientTemplate().queryForList(
				"robTicket.queryRobListCp", paramMap);
	}

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

		return (List<Map<String, String>>)this.getSqlMapClientTemplate().queryForList("robTicket.queryCP",
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
	public void updateFrontBackCP_Refund(HashMap<String, String> map) {
		this.getSqlMapClientTemplate().update("robTicket.updateFrontBackCP_Refund",map);
		
	}

	@Override
	public void insertJLHistory(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("robTicket.insertJlOrderHistory", map);
		
	}

	@Override
	public List<Map<String, String>> queryRobListForExcel(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"robTicket.queryRobListForExcel", paramMap);
	}

}
