package com.kuyou.train.service.impl;

import com.kuyou.train.dao.HistoryMapper;
import com.kuyou.train.service.HistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * HistoryServiceImpl
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private HistoryMapper historyMapper;

    @Override
    public int insertBookLog(String orderId, String log) {
        return historyMapper.insertBookLog(orderId, "system", log);
    }

    @Override
    public int insertBookLog(String orderId, String opt, String log) {
        return historyMapper.insertBookLog(orderId, opt, log);
    }

    @Override
    public int insertChangeLog(String orderId, Integer changeId, String log) {
        return historyMapper.insertChangeLog(orderId, changeId, log);
    }

    @Override
    public int insertRefundLog(String orderId, String cpId, String log) {
        return historyMapper.insertRefundLog(orderId, cpId, log);
    }
}
