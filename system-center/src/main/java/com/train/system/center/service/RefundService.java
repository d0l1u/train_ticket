package com.train.system.center.service;

import java.util.List;

import com.train.system.center.entity.Refund;

public interface RefundService {
	List<Refund> queryListWaitRefund(int limit);

	int updateById(Refund refund, String orderStatus);

	Refund queryByOrderIdAndSequence(String orderId, String subSequence);

}
