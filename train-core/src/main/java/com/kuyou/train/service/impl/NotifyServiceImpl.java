package com.kuyou.train.service.impl;

import com.kuyou.train.dao.NotifyMapper;
import com.kuyou.train.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * NotifyServiceImpl
 *
 * @author taokai3
 * @date 2018/11/9
 */
@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private NotifyMapper notifyMapper;

    @Override
    public int updateBookOrder(String orderId) {
        int result = notifyMapper.updateBookOrder(orderId);
        log.info("预订占位通知更新结果:{}", result);
        return result;
    }

    @Override
    public int updateBookCancel(String orderId) {
        int result = notifyMapper.updateBookCancel(orderId);
        log.info("预订取消通知更新结果:{}", result);
        return result;
    }

    @Override
    public int updateBookPay(String orderId) {
        int result = notifyMapper.updateBookPay(orderId);
        log.info("预订支付通知更新结果:{}", result);
        return result;
    }

    @Override
    public int updateRefund(String orderId, String cpId) {
        int result = notifyMapper.updateRefund(orderId, cpId);
        log.info("退票通知更新结果:{}", result);
        return result;
    }

    @Override
    public int updateChangeOrder(Integer changeId) {
        int result = notifyMapper.updateChangeOrder(changeId);
        log.info("改签占位通知更新结果:{}", result);
        return result;
    }

    @Override
    public int updateChangeCancel(Integer changeId) {
        int result = notifyMapper.updateChangeCancel(changeId);
        log.info("改签取消通知更新结果:{}", result);
        return result;
    }

    @Override
    public int updateChangePay(Integer changeId) {
        int result = notifyMapper.updateChangePay(changeId);
        log.info("改签支付通知更新结果:{}", result);
        return result;
    }
}
