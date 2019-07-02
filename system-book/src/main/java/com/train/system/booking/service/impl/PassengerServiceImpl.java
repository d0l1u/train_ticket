package com.train.system.booking.service.impl;

import com.train.system.booking.dao.PassengerMapper;
import com.train.system.booking.entity.Passenger;
import com.train.system.booking.service.PassengerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PassengerServiceImpl
 *
 * @author taokai3
 * @date 2018/6/18
 */
@Service("passengerService")
public class PassengerServiceImpl implements PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    @Override
    public List<Passenger> queryListByOrderId(String orderId) {
        return passengerMapper.queryListByOrderId(orderId);
    }
}
