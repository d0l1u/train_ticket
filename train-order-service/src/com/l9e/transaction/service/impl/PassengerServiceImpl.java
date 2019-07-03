package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.PassengerDao;
import com.l9e.transaction.service.PassengerService;
import com.l9e.transaction.vo.Passenger;

@Service("passengerService")
public class PassengerServiceImpl implements PassengerService {
	
	@Resource
	private PassengerDao passengerDao;

	@Override
	public List<Passenger> findPassengerByOrder(String orderId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		return passengerDao.selectPassenger(params);
	}

}
