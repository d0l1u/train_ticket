package com.train.system.booking.service;

import com.train.system.booking.entity.LogReturn;

import java.util.List;

/**
 * LogService
 *
 * @author taokai3
 * @date 2018/6/18
 */
public interface LogService {

    int insertBookingLog(String orderId, String message, String operator);

    List<LogReturn> queryList();


}
