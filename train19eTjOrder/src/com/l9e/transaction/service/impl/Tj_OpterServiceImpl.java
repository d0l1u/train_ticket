package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_OpterDao;
import com.l9e.transaction.service.Tj_OpterService;

@Service("tj_OpterService")
public class Tj_OpterServiceImpl implements Tj_OpterService{
	@Resource
	private Tj_OpterDao tj_OpterDao;

	public int queryTableTotal() {
		return tj_OpterDao.queryTableTotal();
	}

	public List<String> queryAllDate(String table_name) {
		return tj_OpterDao.queryAllDate(table_name);
	}

	public List<String> queryAllOpter() {
		return tj_OpterDao.queryAllOpter();
	}


	public int queryRefund_total(Map<String, Object> query_Map) {
		return tj_OpterDao.queryRefund_total(query_Map);
	}

	public List<Map<String, String>> queryOrder_List(
			Map<String, Object> query_Map) {
		return tj_OpterDao.queryOrder_List(query_Map);
	}

	public int queryRefund_totalQunar(Map<String, Object> query_Map) {
		return tj_OpterDao.queryRefund_totalQunar(query_Map);
	}

	public void addStatToTj_Opter(Map<String, Object> add_Map) {
		tj_OpterDao.addStatToTj_Opter(add_Map);
	}

	public int queryRefund_totalApp(Map<String, Object> queryMap) {
		return (Integer)tj_OpterDao.queryRefund_totalApp(queryMap);
	}


	public int queryRefund_totalExt(Map<String, Object> queryMap) {
		return (Integer)tj_OpterDao.queryRefund_totalExt(queryMap);
	}


	public int queryRefund_totalElong(Map<String, Object> queryMap) {
		return (Integer)tj_OpterDao.queryRefund_totalElong(queryMap);
	}

	public int queryRefund_totalInner(Map<String, Object> queryMap) {
		return (Integer)tj_OpterDao.queryRefund_totalInner(queryMap);
	}

	public int queryRefund_totaltongcheng(Map<String, Object> queryMap) {
		return (Integer)tj_OpterDao.queryRefund_totaltongcheng(queryMap);
	}
	
	public int queryRefundnew(Map<String, Object> queryMap) {
		return (Integer)tj_OpterDao.queryRefundnew(queryMap);
	}

	public void addToTjException(Map<String, Object> paramMap) {
		tj_OpterDao.addToTjException(paramMap);
	}

	public List<Map<String, String>> queryExceptionConfig() {
		return tj_OpterDao.queryExceptionConfig();
	}

	public int queryExceptionNum(Map<String, Object> paramMap) {
		return tj_OpterDao.queryExceptionNum(paramMap);
	}

	public int queryTjExceptionCount(Map<String, Object> paramMap) {
		return tj_OpterDao.queryTjExceptionCount(paramMap);
	}

	public void updateToTjException(Map<String, Object> paramMap) {
		tj_OpterDao.updateToTjException(paramMap);
	}

	public void insertTjMatch(Map<String, Object> addMap) {
		tj_OpterDao.insertTjMatch(addMap);
	}

	public List<Map<String, Object>> queryMatchList(Map<String, Object> paramMap) {
		return tj_OpterDao.queryMatchList(paramMap);
	}

	public Map<String, Object> queryStatusMatch(Map<String, Object> paramMap) {
		return tj_OpterDao.queryStatusMatch(paramMap);
	}
}
