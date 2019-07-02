package com.train.system.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.train.system.center.dao.OrderDao;
import com.train.system.center.dao.PassengerDao;
import com.train.system.center.em.OrderStatus;
import com.train.system.center.entity.Order;
import com.train.system.center.entity.Passenger;
import com.train.system.center.jedis.JedisClient;
import com.train.system.center.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * OrderServiceImpl
 *
 * @author taokai3
 * @date 2018/6/25
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Resource
	private OrderDao orderDao;

	@Resource
	private PassengerDao passengerDao;

	@Resource
	private JedisClient publicDataJedisClient;

	@Override
	public List<Order> queryListWaitBooking(int limit) {
		return orderDao.queryListWaitBooking(limit);
	}

	@Override
	public int updateOrder(Order order, String whereOrderStatus) {
		List<Passenger> passengers = order.getPassengers();
		for (int i = 0; passengers != null && i < passengers.size(); i++) {
			// 更新乘客数据
			int update = passengerDao.updatePassenger(passengers.get(i));
			logger.info("Update Passenger Result:{}", update);
		}
		// 更新订单数据
		return orderDao.updateOrder(order, whereOrderStatus);
	}

	@Override
	public Long updateToRedis(Order order, JSONObject requestJson, String queue) {
		String orderStatus = order.getOrderStatus();
		order.setOrderStatus(OrderStatus.BOOKING_ING);
		// 修改订单状态
		int updateResult = orderDao.updateOrder(order, orderStatus);
		logger.info("Update Order Result:{}", updateResult);
		if (updateResult != 0) {
			// 补充到redis
			return publicDataJedisClient.lpush(queue, requestJson.toJSONString());
		} else {
			return 0L;
		}
	}

	@Override
	public Order selectById(String orderId) {
		return orderDao.selectById(orderId);
	}

	@Override
	public List<Order> queryListWaitCancel(int limit) {
		return orderDao.queryListWaitCancel(limit);
	}

	@Override
	public Order selectByMyId(String orderId) {
		return orderDao.selectByMyId(orderId);
	}

	@Override
	public List<Order> queryListWaitPay(int limit) {
		return orderDao.queryListWaitPay(limit);
	}

	@Override
	public int insertTicketEntrance(String orderId, String trainCode, String fromStationName, String ticketEntrance) {
		return orderDao.insertTicketEntrance(orderId, trainCode, fromStationName, ticketEntrance);
	}
}
