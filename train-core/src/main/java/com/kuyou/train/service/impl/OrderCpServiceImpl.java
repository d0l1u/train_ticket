package com.kuyou.train.service.impl;

import com.kuyou.train.dao.OrderCpMapper;
import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderStudentPo;
import com.kuyou.train.service.OrderCpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * OrderCpServiceImpl
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Service
public class OrderCpServiceImpl implements OrderCpService {

    @Resource
    private OrderCpMapper orderCpMapper;


    @Override
    public OrderCpPo selectByCpId(String cpId) {
        return orderCpMapper.selectByCpId(cpId);
    }

    @Override
    public List<String> selectSeqByOrderId(String orderId) {
        return orderCpMapper.selectSeqByOrderId(orderId);
    }

    @Override
    public List<OrderCpPo> selectByOrderId(String orderId) {
        return orderCpMapper.selectByOrderId(orderId);
    }

    @Override
    public OrderStudentPo selectStudent(String cpId) {
        return orderCpMapper.selectStudent(cpId);
    }
}
