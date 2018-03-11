package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.GtBookDao;
@Repository("gtBookDao")
public class GtBookDaoImpl extends BaseDao implements GtBookDao {

	// 查询预订订单列表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtBookList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"gtBook.queryGtBookList", map);
	}

	// 查询预订订单条数
	public int queryGtBookListCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"gtBook.queryGtBookListCount", map);
	}

	// 查询预订订单车票信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtBookOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"gtBook.queryGtBookOrderInfoCp", order_id);
	}

	// 查询预订订单保险信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtBookOrderInfoBx(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"gtBook.queryGtBookOrderInfoBx", order_id);
	}

	// 查询预订订单历史记录
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryGtHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"gtBook.queryGtHistroyByOrderId", order_id);
	}

	// 查询预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtBookOrderInfo(String order_id) {
		return  this.getSqlMapClientTemplate().queryForList("gtBook.queryGtBookOrderInfo", order_id);
	}

	// 查询退款明细
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryGtOutTicketInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"gtBook.queryGtOutTicketInfo", order_id);
	}

	//切换无视截止时间
	public void updateGtSwitch_ignore(Map<String, String> map){
		this.getSqlMapClientTemplate().update("gtBook.updateGtSwitch_ignore",map);
	}
	

	//增加操作日志
	public void addGtUserAccount(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("gtBook.addGtUserAccount", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryBookOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("gtBook.queryBookOrderInfo", order_id);
	}

}
