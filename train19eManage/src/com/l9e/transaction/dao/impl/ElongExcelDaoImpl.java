package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongExcelDao;
@Repository("elongExcelDao")
public class ElongExcelDaoImpl extends BaseDao implements ElongExcelDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getExcelList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.queryExcelList",paramMap);
	}

	@Override
	public int getExcelListCount(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("elongExcel.queryExcelListCount",paramMap);
	}
	
	@Override
	public void insertIntoExcel(HashMap<String, Object> paramMap) {
		  this.getSqlMapClientTemplate().insert("elongExcel.insertIntoExcel",paramMap);
	}
	
	@Override
	public int deleteExcelById(String excel_id) {
		return (Integer)this.getSqlMapClientTemplate().delete("elongExcel.deleteExcelById",excel_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getElongAlterCpList(String id) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.getElongAlterCpList",id);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTcAlterList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.queryTcAlterList",map);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTcBookList(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.queryTcBookList",map);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTcRefundList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.queryTcRefundList",map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAllBook(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.queryAllBook",map);
	}

	@Override
	public void tcAddCheck(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("elongExcel.tcAddCheck",paramMap);
	}

	@Override
	public int getTcCheckCount(HashMap<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("elongExcel.getTcCheckCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTcCheckList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.getTcCheckList",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFileNameById(Map<String, String> excel) {
		return this.getSqlMapClientTemplate().queryForList("elongExcel.getFileNameById",excel);
	}

	@Override
	public void addFileNameLogs(Map<String, String> excel) {
		this.getSqlMapClientTemplate().insert("elongExcel.addFileNameLogs",excel);
	}
	



}
