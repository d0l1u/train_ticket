package com.train.system.booking.service.impl;

import com.train.system.booking.dao.LogMapper;
import com.train.system.booking.entity.LogReturn;
import com.train.system.booking.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * LogServiceImpl
 *
 * @author taokai3
 * @date 2018/6/18
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public int insertBookingLog(String orderId, String message, String operator) {
        return logMapper.insertBookingLog(orderId, message, operator);
    }

    @Override
    public List<LogReturn> queryList() {
        return logMapper.queryList();
    }
}
