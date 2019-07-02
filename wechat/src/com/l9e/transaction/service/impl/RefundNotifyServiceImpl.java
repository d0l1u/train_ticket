package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RefundNotifyDao;
import com.l9e.transaction.service.RefundNotifyService;

@Service("refundNotifyService")
public class RefundNotifyServiceImpl implements RefundNotifyService {

	@Resource
	private RefundNotifyDao refundNotifyDao;

	@Override
	public void insertIntoNotify(Map<String, Object> map) {
		refundNotifyDao.insertIntoNotify(map);
	}

}
