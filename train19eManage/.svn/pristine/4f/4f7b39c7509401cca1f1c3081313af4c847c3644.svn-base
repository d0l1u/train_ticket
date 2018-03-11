package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TicketPriceDao;
import com.l9e.transaction.service.TicketPriceService;
@Service("ticketPriceService")
public class TicketPriceServiceImpl implements TicketPriceService {
	@Resource
	private TicketPriceDao ticketPriceDao;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return ticketPriceDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryTicketPriceList(
			Map<String, Object> paramMap) {
		return ticketPriceDao.queryTicketPriceList(paramMap);
	}

	public Map<String, String> queryTicketPriceModify(Map<String, String> updateMap) {
		return ticketPriceDao.queryTicketPriceModify(updateMap);
	}

	public void updateTicketPrice(Map<String, String> updateMap) {
		ticketPriceDao.updateTicketPrice(updateMap);
	}

	public int queryTicketPriceCheci(Map<String, Object> paramMap) {
		return ticketPriceDao.queryTicketPriceCheci(paramMap);
	}

	public void addTicketPrice(Map<String, String> addMap) {
		ticketPriceDao.addTicketPrice(addMap);
	}

	public void addTicletPriceLogs(Map<String, String> logMap) {
		ticketPriceDao.addTicletPriceLogs(logMap);
	}

	@Override
	public void deleteTicketPrice(Map<String, String> deleteMap) {
		ticketPriceDao.deleteTicketPrice(deleteMap);
	}

	@Override
	public int queryTicketPriceLogCount() {
		return ticketPriceDao.queryTicketPriceLogCount();
	}

	@Override
	public List<Map<String, Object>> queryTicketPriceLogList(
			Map<String, Object> paramMap) {
		return ticketPriceDao.queryTicketPriceLogList(paramMap);
	}
	
	
}
