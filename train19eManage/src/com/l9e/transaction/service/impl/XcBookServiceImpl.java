package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.XcBookDao;
import com.l9e.transaction.service.XcBookService;

@Service("xcBookService")
public class XcBookServiceImpl implements XcBookService {
	
	@Resource
	private XcBookDao xcBookDao;
	
	@Override
	public void addXcUserAccount(Map<String, String> map) {
		xcBookDao.addXcUserAccount(map);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return xcBookDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryXcBookList(Map<String, Object> map) {
		return xcBookDao.queryXcBookList(map);
	}

	@Override
	public int queryXcBookListCount(Map<String, Object> map) {
		return xcBookDao.queryXcBookListCount(map);
	}

	@Override
	public Map<String, String> queryXcBookOrderInfo(String orderId) {
		return xcBookDao.queryXcBookOrderInfo(orderId).get(0);
	}

	@Override
	public List<Map<String, String>> queryXcBookOrderInfoBx(String orderId) {
		return xcBookDao.queryXcBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryXcBookOrderInfoCp(String orderId) {
		return xcBookDao.queryXcBookOrderInfoCp(orderId);
	}

	@Override
	public List<Map<String, Object>> queryXcHistroyByOrderId(String orderId) {
		return xcBookDao.queryXcHistroyByOrderId(orderId);
	}

	@Override
	public List<Map<String, Object>> queryXcOutTicketInfo(String orderId) {
		return xcBookDao.queryXcOutTicketInfo(orderId);
	}

	@Override
	public void updateXcSwitch_ignore(Map<String, String> map) {
		xcBookDao.updateXcSwitch_ignore(map);
	}

}
