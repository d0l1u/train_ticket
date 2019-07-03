package com.train.system.booking.service;

import com.train.system.booking.entity.Passenger;

import java.util.List;

/**
 * PassengerService
 *
 * @author taokai3
 * @date 2018/6/18
 */
public interface PassengerService {

    public List<Passenger> queryListByOrderId(String orderId);

}
