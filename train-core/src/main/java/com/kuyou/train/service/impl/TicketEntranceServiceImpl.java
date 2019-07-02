package com.kuyou.train.service.impl;

import com.kuyou.train.dao.TicketEntranceMapper;
import com.kuyou.train.service.TicketEntranceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TicketEntranceServiceImpl
 *
 * @author taokai3
 * @date 2018/11/9
 */
@Slf4j
@Service
public class TicketEntranceServiceImpl implements TicketEntranceService {

    @Resource
    private TicketEntranceMapper ticketEntranceMapper;

    @Override
    public int insertBook(String orderId, String trainNo, String fromCity, String ticketEntrance) {
        int insertResult = ticketEntranceMapper.insertBook(orderId, trainNo, fromCity, ticketEntrance);
        log.info("检票口信息插入结果:{}", insertResult);
        return insertResult;
    }

    @Override
    public int insertChange(String orderId, Integer changeId, String trainNo, String fromCity, String ticketEntrance) {
        int insertResult = ticketEntranceMapper.insertChange(orderId, changeId, trainNo, fromCity, ticketEntrance);
        log.info("检票口信息插入结果:{}", insertResult);
        return insertResult;
    }
}
