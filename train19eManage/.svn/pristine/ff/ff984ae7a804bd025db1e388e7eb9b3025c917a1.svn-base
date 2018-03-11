package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ExtBookDao;
import com.l9e.transaction.service.ExtBookService;

@Service("extBookService")
public class ExtBookServiceImpl implements ExtBookService {
	
	@Resource
	private ExtBookDao extBookDao;
	
	@Override
	public void addExtUserAccount(Map<String, String> map) {
		extBookDao.addExtUserAccount(map);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return extBookDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryExtBookList(Map<String, Object> map) {
		return extBookDao.queryExtBookList(map);
	}

	@Override
	public int queryExtBookListCount(Map<String, Object> map) {
		return extBookDao.queryExtBookListCount(map);
	}

	@Override
	public Map<String, String> queryExtBookOrderInfo(String orderId) {
		return extBookDao.queryExtBookOrderInfo(orderId).get(0);
	}

	@Override
	public List<Map<String, String>> queryExtBookOrderInfoBx(String orderId) {
		return extBookDao.queryExtBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryExtBookOrderInfoCp(String orderId) {
		return extBookDao.queryExtBookOrderInfoCp(orderId);
	}

	@Override
	public List<Map<String, Object>> queryExtHistroyByOrderId(String orderId) {
		return extBookDao.queryExtHistroyByOrderId(orderId);
	}

	@Override
	public List<Map<String, Object>> queryExtOutTicketInfo(String orderId) {
		return extBookDao.queryExtOutTicketInfo(orderId);
	}

	@Override
	public void updateExtSwitch_ignore(Map<String, String> map) {
		extBookDao.updateExtSwitch_ignore(map);
	}

}
