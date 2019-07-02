package com.kuyou.train.service;

import com.google.common.collect.Lists;
import com.kuyou.train.common.enums.OrderStatusEnum;
import com.kuyou.train.entity.dto.NoCompleteOrderDto;
import com.kuyou.train.entity.req.CancelReq;
import com.kuyou.train.entity.resp.CancelResp;
import com.kuyou.train.kyfw.impl.LoginApiRobot;
import com.kuyou.train.kyfw.impl.OrderApiRobot;
import com.kuyou.train.kyfw.impl.QueryOrderApiRobot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CancelService
 *
 * @author taokai3
 * @date 2018/11/27
 */
@Slf4j
@Service
public class CancelService {

    @Resource
    private LoginApiRobot loginApiRobot;

    @Resource
    private QueryOrderApiRobot queryOrderApiRobot;

    @Resource
    private OrderApiRobot orderApiRobot;

    public CancelResp cancel(CancelReq cancelReq) throws IOException {
        String orderId = cancelReq.getOrderId();
        Integer changeId = cancelReq.getChangeId();
        boolean bookFlag = cancelReq.isBookFlag();
        String sequence = cancelReq.getSequence();
        String username = cancelReq.getUsername();
        String password = cancelReq.getPassword();
        List<String> subSequences = cancelReq.getSubSequences().stream().sorted().collect(Collectors.toList());
        log.info("取消订单orderId:{}, changeId:{}, sequence:{}", orderId, changeId, sequence);
        log.info("取消订单subSequences:{}", subSequences);
        List<OrderStatusEnum> statusEnums = Lists.newArrayList(OrderStatusEnum.WAIT_PAY);
        if (!bookFlag) {
            statusEnums = Lists.newArrayList(OrderStatusEnum.CHANGE_WAIT_PAY, OrderStatusEnum.ALTER_WAIT_PAY);
        }

        //登录
        loginApiRobot.checkLoginStatus(username, password);

        //查询未完成订单
        NoCompleteOrderDto orderDto = queryOrderApiRobot.queryNoComplete(sequence, "", subSequences, statusEnums);
        if (orderDto == null) {
            //取消成功
            return CancelResp.builder().success(true).orderId(orderId).changeId(changeId).bookFlag(bookFlag).build();
        }

        if (OrderStatusEnum.WAIT_ING.equals(orderDto.getStatusEnum())) {
            //取消排队订单
            orderApiRobot.cancelQueueOrder(bookFlag);
        } else {
            //取消普通订单
            orderApiRobot.cancelOrder(sequence);
        }
        return CancelResp.builder().success(true).orderId(orderId).changeId(changeId).bookFlag(bookFlag).build();
    }
}
