package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.XcBookDao;
@Repository("xcBookDao")
public class XcBookDaoImpl extends BaseDao implements XcBookDao {

	// 查询预订订单列表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcBookList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"xcBook.queryXcBookList", map);
	}

	// 查询预订订单条数
	public int queryXcBookListCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"xcBook.queryXcBookListCount", map);
	}

	// 查询预订订单车票信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcBookOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"xcBook.queryXcBookOrderInfoCp", order_id);
	}

	// 查询预订订单保险信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcBookOrderInfoBx(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"xcBook.queryXcBookOrderInfoBx", order_id);
	}

	// 查询预订订单历史记录
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryXcHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"xcBook.queryXcHistroyByOrderId", order_id);
	}

	// 查询预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcBookOrderInfo(String order_id) {
		return  this.getSqlMapClientTemplate().queryForList("xcBook.queryXcBookOrderInfo", order_id);
	}

	// 查询退款明细
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryXcOutTicketInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"xcBook.queryXcOutTicketInfo", order_id);
	}

	//切换无视截止时间
	public void updateXcSwitch_ignore(Map<String, String> map){
		this.getSqlMapClientTemplate().update("xcBook.updateXcSwitch_ignore",map);
	}
	

	//增加操作日志
	public void addXcUserAccount(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("xcBook.addXcUserAccount", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryBookOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("xcBook.queryBookOrderInfo", order_id);
	}

}
