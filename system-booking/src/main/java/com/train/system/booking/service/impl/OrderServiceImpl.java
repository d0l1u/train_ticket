package com.train.system.booking.service.impl;

import com.train.system.booking.dao.OrderMapper;
import com.train.system.booking.dao.PassengerMapper;
import com.train.system.booking.em.OrderStatus;
import com.train.system.booking.entity.Order;
import com.train.system.booking.entity.Passenger;
import com.train.system.booking.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private PassengerMapper passengerMapper;

    @Override
    public List<Order> queryWaitBookingOrder(int idPassMathResult) {
        return orderMapper.queryWaitBookingOrder(idPassMathResult);
    }

    @Override
    public int updateOrderStatus(String orderId, String oldStatus, String newStatus) {
        return orderMapper.updateOrderStatus(orderId, oldStatus, newStatus);
    }

    @Override
    public Order selectById(String orderId) {

        return orderMapper.selectById(orderId);
    }

    //    @Override
    //    public int updateFail(String orderId, String oldStatus, String newStatus, String failReason) {
    //        return orderMapper.updateFail(orderId, oldStatus, newStatus,failReason);
    //    }

    @Override
    public boolean updateOrder(Order order) {
        int updateResult = orderMapper.updateOrder(order);
        List<Passenger> passengers = order.getPassengers();
        if (updateResult > 0 && passengers != null && !passengers.isEmpty()) {
            //更新乘客信息
            for (Passenger passenger : passengers) {
                updateResult = passengerMapper.updateOtherByPassengerNo(passenger);
            }
        }
        return true;
    }

}
