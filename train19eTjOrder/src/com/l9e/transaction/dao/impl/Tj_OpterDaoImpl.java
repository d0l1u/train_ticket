package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_OpterDao;
@Repository("tj_OpterDao")
public class Tj_OpterDaoImpl extends BaseDao implements Tj_OpterDao{

	public void addStatToTj_Opter(Map<String, Object> add_Map) {
		this.getSqlMapClientTemplate().insert("tj_Opter.addStatToTj_Opter", add_Map);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> queryAllDate(String table_name) {
		return this.getSqlMapClientTemplate().queryForList("tj_Opter.queryAllDate",table_name);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryAllOpter() {
		return this.getSqlMapClientTemplate().queryForList("tj_Opter.queryAllOpter");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrder_List(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("tj_Opter.queryOrder_List",query_Map);
	}

	public int queryRefund_total(Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefund_total",query_Map);
	}

	public int queryRefund_totalApp(Map<String, Object> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefund_totalApp",queryMap);
	}


	public int queryRefund_totalElong(Map<String, Object> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefund_totalElong",queryMap);
	}

	public int queryRefund_totalExt(Map<String, Object> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefund_totalExt",queryMap);
	}


	public int queryRefund_totalInner(Map<String, Object> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefund_totalInner",queryMap);
	}

	public int queryRefund_totalQunar(Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefund_totalQunar",query_Map);
	}

	public int queryRefund_totaltongcheng(Map<String, Object> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefund_totaltongcheng",queryMap);
	}
	
	public int queryRefundnew(Map<String, Object> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryRefundnew",queryMap);
	}

	public int queryTableTotal() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryTableTotal");
	}

	public void addToTjException(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("tj_Opter.addToTjException", paramMap);
	}

	public List<Map<String, String>> queryExceptionConfig() {
		return this.getSqlMapClientTemplate().queryForList("tj_Opter.queryExceptionConfig");
	}

	public int queryExceptionNum(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryExceptionNum", paramMap);
	}

	public int queryTjExceptionCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryTjExceptionCount", paramMap);
	}

	public void updateToTjException(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("tj_Opter.updateToTjException", paramMap);
	}

	public void insertTjMatch(Map<String, Object> addMap) {
		this.getSqlMapClientTemplate().insert("tj_Opter.insertTjMatch", addMap);
	}

	public List<Map<String, Object>> queryMatchList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("tj_Opter.queryMatchList", paramMap);
	}

	public Map<String, Object> queryStatusMatch(Map<String, Object> paramMap) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("tj_Opter.queryStatusMatch", paramMap);
	}
	
}
