package com.kuyou.train.service.impl;

import com.kuyou.train.dao.OrderCpMapper;
import com.kuyou.train.dao.OrderMapper;
import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.entity.po.ServicePo;
import com.kuyou.train.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrderServiceImpl
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderCpMapper orderCpMapper;

    @Override
    public OrderPo selectByMyOrderId(String myOrderId) {
        return orderMapper.selectByMyOrderId(myOrderId);
    }

    @Override
    public OrderPo selectByOrderId(String orderId) {
        return orderMapper.selectByOrderId(orderId);
    }


    @Override
    public int updateStatus(String orderId, String orderStatus) {
        int result = orderMapper.updateStatus(orderId, orderStatus);
        log.info("OrderService.updateStatus 更新结果:{}", result);
        return result;
    }

    @Override
    public int updateByOrderId(OrderPo orderPo, String orderId, String preStatus) {
        int result = orderMapper.updateByOrderId(orderPo, orderId, preStatus);
        log.info("OrderService.updateByOrderId 更新结果:{}", result);
        return result;
    }

    @Override
    public List<OrderPo> selectByStatus(ArrayList<String> statusList, Integer limit) {
        return orderMapper.selectByStatus(statusList, limit);
    }

    @Override
    public int updateStatusPre(String orderId, String preStatus, String status) {
        int result = orderMapper.updateStatusPre(orderId, preStatus, status);
        log.info("OrderService.updateStatusPre 更新结果:{}", result);
        return result;
    }

    @Override
    public List<ServicePo> selectWaitService(String bookStatus, String changeStatus, Integer limit) {
        return orderMapper.selectWaitService(bookStatus, changeStatus, limit);
    }

    @Override
    public List<ServicePo> selectStuckService(String bookStatus, String changeStatus, Date time) {
        return orderMapper.selectStuckService(bookStatus, changeStatus, time);
    }

    @Override
    public List<Integer> selectAccountId4Jd() {
        return orderMapper.selectAccountId4Jd();
    }

    @Override
    public int updateOrderAndCp(String orderId, String statusPre, OrderPo order, List<OrderCpPo> cps) {
        int update = orderMapper.updateByOrderId(order, orderId, statusPre);
        log.info("订单主体数据更新结果:{}", update);

        for (OrderCpPo cp : cps){
            String cpId = cp.getCpId();
            update = orderCpMapper.updateByCpid(cpId, cp);
            log.info("订单乘客数据:{} 更新结果:{}", cpId, update);
        }
        return update;
    }

    @Override
    public List<OrderPo> selectStuck(List<String> orderStatusList, Date stuckTime) {
        return orderMapper.selectStuck(orderStatusList, stuckTime);
    }
}
