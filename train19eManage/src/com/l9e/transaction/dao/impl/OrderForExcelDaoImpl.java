package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderForExcelDao;
@Repository("orderForExcelDao")
public class OrderForExcelDaoImpl extends BaseDao implements OrderForExcelDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getBalancAccountExcelList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getBalancAccountExcelList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelAllInsuranceList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelAllInsuranceList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelAppRefundList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelAppRefundList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelExtRefundList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelExtRefundList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelInnerRefundList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelInnerRefundList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelList(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelList", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelOpterList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelOpterList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelProvinceStatList(
			String createTime) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelProvinceStatList", createTime);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderForExcelRefundList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getOrderForExcelRefundList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAppBookList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryAppBookList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryExtBookList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryExtBookList",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryBxInfo(String string) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryBxInfo", string);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryInnerBookList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryInnerBookList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryProvinceTicketAndMoney(HashMap<String, Object> paramMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("orderForExcel.queryProvinceTicketAndMoney", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getElongAlterCpList(String change_id) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getElongAlterCpList", change_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getElongAlterExcelList(HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getElongAlterExcelList", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getCtripExcelList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.getCtripExcelList", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryGtBookList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryGtBookList", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryGtRefundList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryGtRefundList", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryXcBookList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryXcBookList", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryXcRefundList(
			HashMap<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryXcRefundList", paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryTuniuBookExcelList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("orderForExcel.queryTuniuBookExcelList", paramMap);
	}

	


}