package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RefundDao;
import com.l9e.transaction.service.RefundService;

@Service("refundService")
public class RefundServiceImpl implements RefundService{
	@Resource
	private RefundDao refundDao;

	

	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		return refundDao.queryCanRefundStreamList();
	}

	@Override
	public void updateCPAlterInfo(Map<String, String> map) {
		refundDao.updateCPAlterInfo(map);
	}

	@Override
	public void updateRefundInfo(Map<String, String> map) {
		refundDao.updateRefundInfo(map);
	}

	@Override
	public Map<String, String> queryAccountOrderInfo(Map<String, String> param) {
		return refundDao.queryAccountOrderInfo(param);
	}

	@Override
	public Map<String, String> queryRefundCpOrderInfo(Map<String, String> param) {
		return refundDao.queryRefundCpOrderInfo(param);
	}

	@Override
	public int updateOrderRefundStatus(Map<String, String> map) {
		return refundDao.updateOrderRefundStatus(map);
		
	}

	@Override
	public void updateCPRefundInfo(Map<String, String> map) {
		refundDao.updateCPRefundInfo(map);
	}

	@Override
	public Map<String, String> queryChangeRefundCpOrderInfo(
			Map<String, String> param) {
		return refundDao.queryChangeRefundCpOrderInfo(param);
	}
	
}
