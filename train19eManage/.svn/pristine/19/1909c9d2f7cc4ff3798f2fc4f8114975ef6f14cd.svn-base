package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AppBookDao;
@Repository("appBookDao")
public class AppBookDaoImpl extends BaseDao implements AppBookDao {

	// 查询预订订单列表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAppBookList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"appBook.queryAppBookList", map);
	}

	// 查询预订订单条数
	public int queryAppBookListCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"appBook.queryAppBookListCount", map);
	}

	// 查询预订订单车票信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAppBookOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"appBook.queryAppBookOrderInfoCp", order_id);
	}

	// 查询预订订单保险信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAppBookOrderInfoBx(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"appBook.queryAppBookOrderInfoBx", order_id);
	}

	// 查询预订订单历史记录
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryAppHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"appBook.queryAppHistroyByOrderId", order_id);
	}

	// 查询预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAppBookOrderInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"appBook.queryAppBookOrderInfo", order_id);
	}

	// 查询退款明细
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryAppOutTicketInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"appBook.queryAppOutTicketInfo", order_id);
	}

	//切换无视截止时间
	public void updateAppSwitch_ignore(Map<String, String> map){
		this.getSqlMapClientTemplate().update("appBook.updateAppSwitch_ignore",map);
	}
	

	//增加操作日志
	public void addAppUserAccount(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("appBook.addAppUserAccount", map);
	}
}
