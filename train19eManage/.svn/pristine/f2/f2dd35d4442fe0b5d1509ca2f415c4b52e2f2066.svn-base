package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.InnerBookDao;

@Repository("innerBookDao")
public class InnerBookDaoImpl extends BaseDao implements InnerBookDao {

	// 查询预订订单列表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryInnerBookList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"innerBook.queryInnerBookList", map);
	}

	// 查询预订订单条数
	public int queryInnerBookListCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"innerBook.queryInnerBookListCount", map);
	}

	// 查询预订订单车票信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryInnerBookOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"innerBook.queryInnerBookOrderInfoCp", order_id);
	}

	// 查询预订订单保险信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryInnerBookOrderInfoBx(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"innerBook.queryInnerBookOrderInfoBx", order_id);
	}

	// 查询预订订单历史记录
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInnerHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"innerBook.queryInnerHistroyByOrderId", order_id);
	}

	// 查询预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryInnerBookOrderInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"innerBook.queryInnerBookOrderInfo", order_id);
	}

	// 查询退款明细
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInnerOutTicketInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"innerBook.queryInnerOutTicketInfo", order_id);
	}

	//切换无视截止时间
	public void updateInnerSwitch_ignore(Map<String, String> map){
		this.getSqlMapClientTemplate().update("innerBook.updateInnerSwitch_ignore",map);
	}
	

	//增加操作日志
	public void addInnerUserAccount(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("innerBook.addInnerUserAccount", map);
	}
}
