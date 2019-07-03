package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OldOrderDao;
import com.l9e.transaction.service.OldOrderService;

@Service("oldOrderService")
public class OldOrderServiceImpl implements OldOrderService{
	@Resource
	private OldOrderDao oldOrderDao;
	
	public List<Map<String, String>> queryOldOrderList(
			Map<String, Object> paramMap) {
		return oldOrderDao.queryOldOrderList(paramMap);
	}
	
	public List<Map<String, String>> queryOldOrderListCp(
			Map<String, Object> paramMap) {
		return oldOrderDao.queryOldOrderListCp(paramMap);
	}
	
	public List<Map<String, String>> queryOldOrderExcel(
			Map<String, Object> paramMap) {
		return oldOrderDao.queryOldOrderExcel(paramMap);
	}
	public int queryOldOrderCount(Map<String, Object> paramMap) {
		return oldOrderDao.queryOldOrderCount(paramMap);
	}
	
	public int queryOldOrderCountCp(Map<String, Object> paramMap) {
		return oldOrderDao.queryOldOrderCountCp(paramMap);
	}

	public Map<String, String> queryOldOrderInfo(String order_id) {
		return oldOrderDao.queryOldOrderInfo(order_id);
	}

	public List<Map<String, Object>> queryOldOrderInfoCp(String order_id) {
		return oldOrderDao.queryOldOrderInfoCp(order_id);
	}
}
