package com.train.system.booking.service;

import com.train.system.booking.entity.Order;

import java.util.List;

public interface OrderService {

    /**
     * 根据 ID取模运算 结果，获取对应分区的数据
     *
     * @param idPassMathResult
     * @return
     */
    List<Order> queryWaitBookingOrder(int idPassMathResult);

    int updateOrderStatus(String orderId, String oldStatus, String newStatus);

    Order selectById(String orderId);

//    int updateFail(String orderId, String oldStatus, String newStatus, String failReason);

    boolean updateOrder(Order order);

//    boolean updateResend(Order order);
//    boolean updateManual(String orderId);
}
