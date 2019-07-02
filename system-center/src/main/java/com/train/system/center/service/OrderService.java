package com.train.system.center.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.train.system.center.entity.Order;
import com.train.system.center.entity.Passenger;

/**
 * OrderService
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface OrderService {

	List<Order> queryListWaitBooking(int limit);

	int updateOrder(Order order, String whereOrderStatus);

	Long updateToRedis(Order order, JSONObject requestJson, String queue);

	Order selectById(String orderId);

	List<Order> queryListWaitCancel(int limit);

	Order selectByMyId(String orderId);

	List<Order> queryListWaitPay(int i);

	int insertTicketEntrance(String orderId, String trainCode, String fromStationName, String ticketEntrance);
}
