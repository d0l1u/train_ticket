package com.train.system.center.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.train.system.center.dao.RefundDao;
import com.train.system.center.entity.Refund;
import com.train.system.center.service.RefundService;

@Service("refundService")
public class RefundServiceImpl implements RefundService {

	@Resource
	private RefundDao refundDao;

	@Override
	public List<Refund> queryListWaitRefund(int limit) {
		return refundDao.queryListWaitRefund(limit);
	}

	@Override
	public int updateById(Refund refund, String orderStatus) {
		return refundDao.updateById(refund, orderStatus);
	}

	@Override
	public Refund queryByOrderIdAndSequence(String orderId, String subSequence) {
		return refundDao.queryByOrderIdAndSequence(orderId, subSequence);
	}

}
