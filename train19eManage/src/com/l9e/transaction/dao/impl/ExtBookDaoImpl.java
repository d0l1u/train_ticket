package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ExtBookDao;
@Repository("extBookDao")
public class ExtBookDaoImpl extends BaseDao implements ExtBookDao {

	// 查询预订订单列表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryExtBookList(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"extBook.queryExtBookList", map);
	}

	// 查询预订订单条数
	public int queryExtBookListCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"extBook.queryExtBookListCount", map);
	}

	// 查询预订订单车票信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryExtBookOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"extBook.queryExtBookOrderInfoCp", order_id);
	}

	// 查询预订订单保险信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryExtBookOrderInfoBx(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"extBook.queryExtBookOrderInfoBx", order_id);
	}

	// 查询预订订单历史记录
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryExtHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"extBook.queryExtHistroyByOrderId", order_id);
	}

	// 查询预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryExtBookOrderInfo(String order_id) {
		return  this.getSqlMapClientTemplate().queryForList("extBook.queryExtBookOrderInfo", order_id);
	}

	// 查询退款明细
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryExtOutTicketInfo(String order_id) {
		return this.getSqlMapClientTemplate().queryForList(
				"extBook.queryExtOutTicketInfo", order_id);
	}

	//切换无视截止时间
	public void updateExtSwitch_ignore(Map<String, String> map){
		this.getSqlMapClientTemplate().update("extBook.updateExtSwitch_ignore",map);
	}
	

	//增加操作日志
	public void addExtUserAccount(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("extBook.addExtUserAccount", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryBookOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("extBook.queryBookOrderInfo", order_id);
	}

}
