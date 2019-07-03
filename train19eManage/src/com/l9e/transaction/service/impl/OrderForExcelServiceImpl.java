package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OrderForExcelDao;
import com.l9e.transaction.service.OrderForExcelService;

@Service("orderForExcelService")
public class OrderForExcelServiceImpl implements OrderForExcelService {

	@Resource
	private OrderForExcelDao orderForExcelDao;

	public List<Map<String, String>> getBalancAccountExcelList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getBalancAccountExcelList(paramMap);
	}


	public List<Map<String, String>> getOrderForExcelAllInsuranceList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getOrderForExcelAllInsuranceList(paramMap);
	}


	public List<Map<String, String>> getOrderForExcelAppRefundList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getOrderForExcelAppRefundList(paramMap);
	}


	public List<Map<String, String>> getOrderForExcelExtRefundList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getOrderForExcelExtRefundList(paramMap);
	}


	public List<Map<String, String>> getOrderForExcelInnerRefundList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getOrderForExcelInnerRefundList(paramMap);
	}


	public List<Map<String, String>> getOrderForExcelList(
			HashMap<String, Object> map) {
		return orderForExcelDao.getOrderForExcelList(map);
	}


	public List<Map<String, String>> getOrderForExcelOpterList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getOrderForExcelOpterList(paramMap);
	}


	public List<Map<String, String>> getOrderForExcelProvinceStatList(
			String createTime) {
		return orderForExcelDao.getOrderForExcelProvinceStatList(createTime);
	}


	public List<Map<String, String>> getOrderForExcelRefundList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getOrderForExcelRefundList(paramMap);
	}


	public List<Map<String, String>> queryAppBookList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryAppBookList(paramMap);
	}

	public List<Map<String, Object>> queryBxInfo(String orderId) {
		return orderForExcelDao.queryBxInfo(orderId);
	}


	public List<Map<String, String>> queryExtBookList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryExtBookList(paramMap);
	}

	public List<Map<String, String>> queryInnerBookList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryInnerBookList(paramMap);
	}


	public Map<String, String> queryProvinceTicketAndMoney(HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryProvinceTicketAndMoney(paramMap);
	}


	@Override
	public List<Map<String, String>> getElongAlterCpList(
			String change_id) {
		return orderForExcelDao.getElongAlterCpList(change_id);
	}


	@Override
	public List<Map<String, String>> getElongAlterExcelList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.getElongAlterExcelList(paramMap);
	}


	@Override
	public List<Map<String, String>> getCtripExcelList(
			Map<String, Object> paramMap) {
		return orderForExcelDao.getCtripExcelList(paramMap);
	}


	@Override
	public List<Map<String, String>> queryGtBookList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryGtBookList(paramMap);
	}


	@Override
	public List<Map<String, String>> queryGtRefundList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryGtRefundList(paramMap);
	}


	@Override
	public List<Map<String, String>> queryXcBookList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryXcBookList(paramMap);
	}

	@Override
	public List<Map<String, String>> queryXcRefundList(
			HashMap<String, Object> paramMap) {
		return orderForExcelDao.queryXcRefundList(paramMap);
	}


	@Override
	public List<Map<String, String>> queryTuniuBookExcelList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return orderForExcelDao.queryTuniuBookExcelList(paramMap);
	}



}
