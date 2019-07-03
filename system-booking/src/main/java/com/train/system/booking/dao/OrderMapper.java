package com.train.system.booking.dao;

import com.train.system.booking.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    List<Order> queryWaitBookingOrder(@Param("idPassMathResult") int idPassMathResult);

    int updateOrderStatus(@Param("orderId") String orderId, @Param("oldStatus") String oldStatus,
            @Param("newStatus") String newStatus);

    Order selectById(@Param("orderId") String orderId);

    int updateFail(@Param("orderId") String orderId, @Param("oldStatus") String oldStatus,
            @Param("newStatus") String newStatus, @Param("failReason") String failReason);

    int updateOrder(@Param("order") Order order);
}
