package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongBookDao;
import com.l9e.transaction.vo.ElongVo;

@Repository("elongBookDAO")
public class ElongBookDaoImpl extends BaseDao implements ElongBookDao {

	@SuppressWarnings("unchecked")
	public List<ElongVo> getAllBookList() {
		return this.getSqlMapClientTemplate().queryForList(
				"elongBook.queryAllBookList");
	}

	@SuppressWarnings("unchecked")
	public List<ElongVo> getBookList(HashMap<String, Object> paramMap) {
		List<ElongVo> queryForList = this.getSqlMapClientTemplate()
				.queryForList("elongBook.queryBookList", paramMap);
		return queryForList;
	}

	@SuppressWarnings("unchecked")
	public List<ElongVo> getBookListCx(HashMap<String, Object> paramMap) {
		List<ElongVo> queryForList = this.getSqlMapClientTemplate()
				.queryForList("elongBook.queryBookListCx", paramMap);
		return queryForList;
	}
	
	@SuppressWarnings("unchecked")
	public int getBookListCount(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"elongBook.queryBookListCount", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public int getBookListCountCx(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"elongBook.queryBookListCountCx", paramMap);
	}

	// 获得某订单的详细信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getBookOrderInfo(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("elongBook.queryBookOrderInfo", order_id);
	}

	// 获得某订单的车票信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getBookCpInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"elongBook.queryBookCpInfo", order_id);
	}

	// 获得某订单的日志信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getBookLog(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"elongBook.queryBookLog", order_id);
	}

	// 获得某订单号的通知信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getBookNotify(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("elongBook.queryBookNotify", order_id);
	}

	// 获得某联程订单的联程信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> queryLianChengOrderInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"elongBook.queryLianChengOrderInfo", order_id);
	}

	// 获得联程总订单信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> queryLianChengTotalOrderInfo(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("elongBook.queryLianChengTotalOrderInfo",
						order_id);
	}

	// 获得联程订单付款信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> queryLianChengPayInfo(String trip_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("elongBook.queryLianChengPayInfo", trip_id);
	}
	
	//更新通知状态
	public int updateNotify_status(Map<String, String> paramMap){
		return (Integer)this.getSqlMapClientTemplate().update("elongBook.updateNotify_status", paramMap);
	}
	
	//添加日志信息
	public void insertLog(Map<String, String> logMap){
		this.getSqlMapClientTemplate().insert("elongBook.insertLog", logMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryQunarBook(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("elongBook.queryQunarBook", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryQunarBookCp(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("elongBook.queryQunarBookCp", map);
	}
	
	//查询联程预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryLianChengQunarBook(HashMap<String, Object> map){
		return this.getSqlMapClientTemplate().queryForList("elongBook.queryLianChengQunarBook", map);
	}
	
	//查询退款订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map){
		return this.getSqlMapClientTemplate().queryForList("elongBook.queryRefundTicket", map);
	}

	//重新通知
	public int updateNotify_Again(Map<String, String> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().update("elongBook.updateNotify_Again", paramMap);
	}
	public int updateNotify_Again1(Map<String, String> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().update("elongBook.updateNotify_Again1", paramMap);
	}

	@Override
	public String queryRefundTicketName(String cp_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("elongBook.queryRefundTicketName",cp_id);
	}

	@SuppressWarnings("unchecked")
	public String queryDbOrder_status(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("elongBook.queryDbOrder_status",order_id);
	}
	
	@SuppressWarnings("unchecked")
	public String queryDbNotify_status(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("elongBook.queryDbNotify_status",order_id);
	}
	

	@Override
	public void updateCheXiaoElong(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("elongBook.updateCheXiaoElong",updateMap);
	}
	@Override
	public void updateCheXiaoCp(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("elongBook.updateCheXiaoCp",updateMap);
	}
	
	public void insertCheXiaoNotify(Map<String, String> updateMap){
		this.getSqlMapClientTemplate().insert("elongBook.insertCheXiaoNotify", updateMap);
	}

	@Override
	public void updateGotoFailure(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("elongBook.updateGotoFailure",updateMap);
	}

	@Override
	public void updateGotoNormal(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("elongBook.updateGotoNormal",updateMap);
	}

	@Override
	public int queryQunarBookCpCount(HashMap<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"elongBook.queryQunarBookCpCount", map);
	}
	
	
}
