package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuBookDao;
@Repository("tuniuBookDao")
public class TuniuBookDaoImpl extends BaseDao implements TuniuBookDao {

	// 查询预订订单列表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuBookList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"tuniuBook.queryTuniuBookList", map);
	}

	// 查询预订订单条数
	public int queryTuniuBookListCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"tuniuBook.queryTuniuBookListCount", map);
	}

	// 查询预订订单车票信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuBookOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"tuniuBook.queryTuniuBookOrderInfoCp", order_id);
	}

	// 查询预订订单保险信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuBookOrderInfoBx(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"tuniuBook.queryTuniuBookOrderInfoBx", order_id);
	}

	// 查询预订订单历史记录
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryTuniuHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"tuniuBook.queryTuniuHistroyByOrderId", order_id);
	}

	// 查询预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuBookOrderInfo(String order_id) {
		return  this.getSqlMapClientTemplate().queryForList("tuniuBook.queryTuniuBookOrderInfo", order_id);
	}

	// 查询退款明细
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryTuniuOutTicketInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"tuniuBook.queryTuniuOutTicketInfo", order_id);
	}

	//切换无视截止时间
	public void updateTuniuSwitch_ignore(Map<String, String> map){
		this.getSqlMapClientTemplate().update("tuniuBook.updateTuniuSwitch_ignore",map);
	}
	

	//增加操作日志
	public void addTuniuUserAccount(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("tuniuBook.addTuniuUserAccount", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryBookOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("tuniuBook.queryBookOrderInfo", order_id);
	}

}
