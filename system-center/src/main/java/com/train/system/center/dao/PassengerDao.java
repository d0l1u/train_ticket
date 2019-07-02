package com.train.system.center.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.train.system.center.entity.Passenger;

/**
 * PassengerDao
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface PassengerDao {

	List<Passenger> queryListByOrderId(@Param("orderId") String orderId);

	int updatePassenger(@Param("passenger") Passenger passenger);

	Passenger queryByCpId(@Param("cpId") String cpId);

	List<Passenger> queryChangePassengers(@Param("changeId") Integer changeId);

	int updateChangePassenger(@Param("passenger") Passenger passenger);

	Passenger queryByChangeCpId(@Param("cpId") String cpId);
}
