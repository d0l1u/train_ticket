package com.train.system.center.service;

import java.util.List;

import com.train.system.center.entity.Passenger;

/**
 * PassengerService
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface PassengerService {

	List<Passenger> queryListByOrderId(String orderId);

	int updatePassenger(Passenger passenger);

	Passenger queryByCpId(String cpId);

	List<Passenger> queryChangePassengers(Integer changeId);

	Passenger queryByChangeCpId(String cpId);

}
