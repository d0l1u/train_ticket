package com.train.system.center.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.train.system.center.entity.Order;
import com.train.system.center.entity.Passenger;

/**
 * OrderDao
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface OrderDao {

	List<Order> queryListWaitBooking(@Param("limit") Integer limit);

	int updateOrder(@Param("order") Order order, @Param("whereOrderStatus") String whereOrderStatus);

	Order selectById(@Param("orderId") String orderId);

	List<Order> queryListWaitCancel(@Param("limit") int limit);

	Order selectByMyId(@Param("orderId") String orderId);

	List<Order> queryListWaitPay(@Param("limit") int limit);

	int insertTicketEntrance(@Param("orderId") String orderId, @Param("trainCode") String trainCode,
			@Param("fromStationName") String fromStationName, @Param("ticketEntrance") String ticketEntrance);
}
