package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AcquireDao;
import com.l9e.transaction.dao.GtBookDao;
import com.l9e.transaction.service.GtBookService;

@Service("gtBookService")
public class GtBookServiceImpl implements GtBookService {
	
	@Resource
	private GtBookDao gtBookDao;
	@Resource 
	private AcquireDao acquireDao;
	
	@Override
	public void addGtUserAccount(Map<String, String> map) {
		gtBookDao.addGtUserAccount(map);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return gtBookDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryGtBookList(Map<String, Object> map) {
		return gtBookDao.queryGtBookList(map);
	}

	@Override
	public int queryGtBookListCount(Map<String, Object> map) {
		return gtBookDao.queryGtBookListCount(map);
	}

	@Override
	public Map<String, String> queryGtBookOrderInfo(String orderId) {
		return gtBookDao.queryGtBookOrderInfo(orderId).get(0);
	}

	@Override
	public List<Map<String, String>> queryGtBookOrderInfoBx(String orderId) {
		return gtBookDao.queryGtBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryGtBookOrderInfoCp(String orderId) {
		return gtBookDao.queryGtBookOrderInfoCp(orderId);
	}

	@Override
	public List<Map<String, Object>> queryGtHistroyByOrderId(String orderId) {
		return gtBookDao.queryGtHistroyByOrderId(orderId);
	}

	@Override
	public List<Map<String, Object>> queryGtOutTicketInfo(String orderId) {
		return gtBookDao.queryGtOutTicketInfo(orderId);
	}

	@Override
	public void updateGtSwitch_ignore(Map<String, String> map) {
		gtBookDao.updateGtSwitch_ignore(map);
	}

	@Override
	public void cpNoticeAgain(String order_id) {
		acquireDao.updateCp_Orderinfo_Notify(order_id);
		
	}

}
