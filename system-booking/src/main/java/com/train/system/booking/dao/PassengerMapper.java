package com.train.system.booking.dao;

import com.train.system.booking.entity.Passenger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassengerMapper {

    List<Passenger> queryListByOrderId(@Param("orderId") String orderId);

    int updateOtherByPassengerNo(@Param("passenger")Passenger passenger);
}
