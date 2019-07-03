package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.UploadExcelDao;
@Repository("uploadExcelDao")
public class UploadExcelDaoImpl extends BaseDao implements UploadExcelDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExcelList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryExcelList",paramMap);
	}

	
	public int getExcelListCount(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("uploadExcel.queryExcelListCount",paramMap);
	}
	
	
	public void insertIntoExcel(HashMap<String, Object> paramMap) {
		  this.getSqlMapClientTemplate().insert("uploadExcel.insertIntoExcel",paramMap);
	}
	
	
	public int deleteExcelById(String excel_id) {
		return (Integer)this.getSqlMapClientTemplate().delete("uploadExcel.deleteExcelById",excel_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getElongAlterCpList(String id) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.getElongAlterCpList",id);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTcAlterList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTcAlterList",map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAlterList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryAlterList",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTcBookList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTcBookList",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTcRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTcRefundList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllBook(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryAllBook",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryElongBookCp(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryElongBookCp",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryElongRefundTicket(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryElongRefundTicket",map);
	}


	public String queryElongRefundTicketName(String string) {
		return (String) this.getSqlMapClientTemplate().queryForObject("uploadExcel.queryElongRefundTicketName",string);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAppRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryAppRefundList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryElongRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryElongRefundList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryExtRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryExtRefundList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryInnerRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryInnerRefundList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryQunarRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryQunarRefundList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryl9eRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryl9eRefundList",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTestList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTestList",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTestMap(String string) {
		return  this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTestMap",string);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtRefundList(
			HashMap<String, Object> map) {
		return  this.getSqlMapClientTemplate().queryForList("uploadExcel.queryGtRefundList",map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtgjAlterList(
			HashMap<String, Object> map) {
		return  this.getSqlMapClientTemplate().queryForList("uploadExcel.queryGtgjAlterList",map);
	}

	

	public void tcAddTcCheck(Map<String, String> addMap) {
		this.getSqlMapClientTemplate().insert("uploadExcel.tcAddTcCheck",addMap);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryMtRefundList(
			HashMap<String, Object> map) {
		return  this.getSqlMapClientTemplate().queryForList("uploadExcel.queryMtRefundList",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryXcRefundList",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryMtBookList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryMtBookList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtBookList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryGtBookList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryGtRefundNotifyList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryGtRefundNotifyList",map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcBookList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryXcBookList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryXcRefundNotifyList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryXcRefundNotifyList",map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuRefundList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTuniuRefundList",map);
	}


	public void addCheckOrderInfo(Map<String, String> m) {
		this.getSqlMapClientTemplate().insert("uploadExcel.addCheckOrderInfo",m);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuBookList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTuniuBookList",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTuniuRefundTicket(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryTuniuRefundTicket",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCheckTcAlter(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("uploadExcel.queryCheckTcAlter",map);
	}




}
