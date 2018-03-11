package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.MeituanBookDao;
import com.l9e.transaction.vo.MeituanVo;

@Repository("meituanBookDAO")
public class MeituanBookDaoImpl extends BaseDao implements MeituanBookDao {

	@SuppressWarnings("unchecked")
	public List<MeituanVo> getAllBookList() {
		return this.getSqlMapClientTemplate().queryForList(
				"meituanBook.queryAllBookList");
	}

	@SuppressWarnings("unchecked")
	public List<MeituanVo> getBookList(HashMap<String, Object> paramMap) {
		List<MeituanVo> queryForList = this.getSqlMapClientTemplate()
				.queryForList("meituanBook.queryBookList", paramMap);
		return queryForList;
	}

	public int getBookListCount(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"meituanBook.queryBookListCount", paramMap);
	}

	// 获得某订单的详细信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getBookOrderInfo(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("meituanBook.queryBookOrderInfo", order_id);
	}

	// 获得某订单的车票信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getBookCpInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"meituanBook.queryBookCpInfo", order_id);
	}

	// 获得某订单的日志信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getBookLog(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"meituanBook.queryBookLog", order_id);
	}

	// 获得某订单号的通知信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getBookNotify(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("meituanBook.queryBookNotify", order_id);
	}


	//更新通知状态
	public int updateNotify_status(Map<String, String> paramMap){
		return (Integer)this.getSqlMapClientTemplate().update("meituanBook.updateNotify_status", paramMap);
	}
	
	//添加日志信息
	public void insertLog(Map<String, String> logMap){
		this.getSqlMapClientTemplate().insert("meituanBook.insertLog", logMap);
	}
	//查询退款订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map){
		return this.getSqlMapClientTemplate().queryForList("meituanBook.queryRefundTicket", map);
	}

	//重新通知
	public int updateNotify_Again(Map<String, String> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().update("meituanBook.updateNotify_Again", paramMap);
	}
	public int updateNotify_Again1(Map<String, String> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().update("meituanBook.updateNotify_Again1", paramMap);
	}

	@Override
	public String queryRefundTicketName(String cp_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("meituanBook.queryRefundTicketName",cp_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryQunarBookCp(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("meituanBook.queryQunarBookCp", map);
	}


}
