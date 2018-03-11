package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TuniuBookDao;
import com.l9e.transaction.service.TuniuBookService;

@Service("tuniuBookService")
public class TuniuBookServiceImpl implements TuniuBookService {
	
	@Resource
	private TuniuBookDao tuniuBookDao;
	
	@Override
	public void addTuniuUserAccount(Map<String, String> map) {
		tuniuBookDao.addTuniuUserAccount(map);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return tuniuBookDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryTuniuBookList(Map<String, Object> map) {
		return tuniuBookDao.queryTuniuBookList(map);
	}

	@Override
	public int queryTuniuBookListCount(Map<String, Object> map) {
		return tuniuBookDao.queryTuniuBookListCount(map);
	}

	@Override
	public Map<String, String> queryTuniuBookOrderInfo(String orderId) {
		return tuniuBookDao.queryTuniuBookOrderInfo(orderId).get(0);
	}

	@Override
	public List<Map<String, String>> queryTuniuBookOrderInfoBx(String orderId) {
		return tuniuBookDao.queryTuniuBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryTuniuBookOrderInfoCp(String orderId) {
		return tuniuBookDao.queryTuniuBookOrderInfoCp(orderId);
	}

	@Override
	public List<Map<String, Object>> queryTuniuHistroyByOrderId(String orderId) {
		return tuniuBookDao.queryTuniuHistroyByOrderId(orderId);
	}

	@Override
	public List<Map<String, Object>> queryTuniuOutTicketInfo(String orderId) {
		return tuniuBookDao.queryTuniuOutTicketInfo(orderId);
	}

	@Override
	public void updateTuniuSwitch_ignore(Map<String, String> map) {
		tuniuBookDao.updateTuniuSwitch_ignore(map);
	}

}
