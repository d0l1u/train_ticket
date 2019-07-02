package com.kuyou.train.service;

import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderStudentPo;

import java.util.List;

/**
 * OrderCpService
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface OrderCpService {

    /**
     * 根据cpid查询乘客信息
     *
     * @param cpId
     * @return
     */
    OrderCpPo selectByCpId(String cpId);

    /**
     * 查询票号
     *
     * @param orderId
     * @return
     */
    List<String> selectSeqByOrderId(String orderId);

    /**
     * 查询乘客
     *
     * @param orderId
     * @return
     */
    List<OrderCpPo> selectByOrderId(String orderId);

    /**
     * 查询乘客
     *
     * @param cpId
     * @return
     */
    OrderStudentPo selectStudent(String cpId);
}
