package com.train.system.center.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.train.system.center.dao.NotifyDao;
import com.train.system.center.service.NotifyService;

/**
 * NotifyServiceImpl
 *
 * @author taokai3
 * @date 2018/6/25
 */
@Service("notifyService")
public class NotifyServiceImpl implements NotifyService {

	@Resource
	private NotifyDao notifyDao;

	@Override
	public int updateBeginByOrderId(String orderId) {
		return notifyDao.updateBeginByOrderId(orderId);
	}

	@Override
	public int updateRefund(String orderId, String cpId) {
		return updateRefund(orderId, cpId);
	}
}
