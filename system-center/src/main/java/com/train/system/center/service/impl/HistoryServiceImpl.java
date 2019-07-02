package com.train.system.center.service.impl;

import com.train.system.center.dao.HistoryDao;
import com.train.system.center.service.HistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * HistoryServiceImpl
 *
 * @author taokai3
 * @date 2018/6/25
 */
@Service("historyService")
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private HistoryDao historyDao;

    @Override
    public int insertBookingHistory(String orderId, String message, String operator) {
        return historyDao.insertBookingHistory(orderId, message, operator);
    }

	@Override
	public int insertRefundHistory(String orderId, String cpId, String message, String operator) {
		return historyDao.insertRefundHistory(orderId, cpId, message, operator);
	}

	@Override
	public int insertChangeOrderHistory(String orderId, String cpId, String message, String operator) {
		return historyDao.insertChangeOrderHistory(orderId, cpId, message, operator);
	}
}
