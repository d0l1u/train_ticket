package com.train.system.booking.dao;

import com.train.system.booking.entity.LogReturn;
import com.train.system.booking.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogMapper {

    int insertBookingLog(@Param("orderId") String orderId, @Param("message") String message,
            @Param("operator") String operator);

    List<LogReturn> queryList();
}
