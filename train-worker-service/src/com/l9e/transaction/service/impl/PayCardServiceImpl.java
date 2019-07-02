package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.PayCardDao;
import com.l9e.transaction.service.PayCardService;
import com.l9e.transaction.vo.PayCard;

@Service("payCardService")
public class PayCardServiceImpl implements PayCardService {
	
	@Resource
	private PayCardDao payCardDao;

	@Override
	public PayCard getCardByWorkerId(Integer workerId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workerId", workerId);
		params.put("limit", 1);
		return payCardDao.selectOnePayCard(params);
	}

	@Override
	public void updatePayCard(PayCard payCard) {
		payCardDao.updatePayCard(payCard);
	}

}
