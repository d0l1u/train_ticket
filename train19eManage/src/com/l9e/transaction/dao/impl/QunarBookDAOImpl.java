package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QunarBookDAO;
import com.l9e.transaction.vo.QunarBookVo;

@Repository("qunarBookDAO")
public class QunarBookDAOImpl extends BaseDao implements QunarBookDAO {

	@SuppressWarnings("unchecked")
	public List<QunarBookVo> getAllBookList() {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarbook.queryAllBookList");
	}

	@SuppressWarnings("unchecked")
	public List<QunarBookVo> getBookList(HashMap<String, Object> paramMap) {
		List<QunarBookVo> queryForList = this.getSqlMapClientTemplate()
				.queryForList("qunarbook.queryBookList", paramMap);
		return queryForList;
	}
	
	@SuppressWarnings("unchecked")
	public List<QunarBookVo> getBookList1(HashMap<String, Object> paramMap) {
		List<QunarBookVo> queryForList1 = this.getSqlMapClientTemplate()
				.queryForList("qunarbook.queryBookList1", paramMap);
		return queryForList1;
	}

	@SuppressWarnings("unchecked")
	public int getBookListCount(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarbook.queryBookListCount", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public int getBook1ListCount(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"qunarbook.queryBook1ListCount", paramMap);
	}

	// 获得某订单的详细信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getBookOrderInfo(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("qunarbook.queryBookOrderInfo", order_id);
	}

	// 获得某订单的车票信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getBookCpInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarbook.queryBookCpInfo", order_id);
	}

	// 获得某订单的日志信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getBookLog(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarbook.queryBookLog", order_id);
	}

	// 获得某订单号的通知信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getBookNotify(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("qunarbook.queryBookNotify", order_id);
	}

	// 获得某联程订单的联程信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> queryLianChengOrderInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"qunarbook.queryLianChengOrderInfo", order_id);
	}

	// 获得联程总订单信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> queryLianChengTotalOrderInfo(String order_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("qunarbook.queryLianChengTotalOrderInfo",
						order_id);
	}

	// 获得联程订单付款信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> queryLianChengPayInfo(String trip_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("qunarbook.queryLianChengPayInfo", trip_id);
	}
	
	//更新通知状态
	public int updateNotify_status(Map<String, String> paramMap){
		return (Integer)this.getSqlMapClientTemplate().update("qunarbook.updateNotify_status", paramMap);
	}
	
	//添加日志信息
	public void insertLog(Map<String, String> logMap){
		this.getSqlMapClientTemplate().insert("qunarbook.insertLog", logMap);
	}
}
