package com.train.system.center.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.train.system.center.dao.PassengerDao;
import com.train.system.center.entity.Passenger;
import com.train.system.center.service.PassengerService;

/**
 * PassengerServiceImpl
 *
 * @author taokai3
 * @date 2018/6/25
 */
@Service("passengerService")
public class PassengerServiceImpl implements PassengerService {

	@Resource
	private PassengerDao passengerDao;

	@Override
	public List<Passenger> queryListByOrderId(String orderId) {
		return passengerDao.queryListByOrderId(orderId);
	}

	@Override
	public int updatePassenger(Passenger passenger) {
		return passengerDao.updatePassenger(passenger);
	}

	@Override
	public Passenger queryByCpId(String cpId) {
		return passengerDao.queryByCpId(cpId);
	}

	@Override
	public List<Passenger> queryChangePassengers(Integer changeId) {
		return passengerDao.queryChangePassengers(changeId);
	}

	@Override
	public Passenger queryByChangeCpId(String cpId) {
		return passengerDao.queryByChangeCpId(cpId);
	}
}
