package com.kuyou.train.service;

import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.entity.po.ServicePo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrderService
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface OrderService {

    /**
     * 根据myOrderId查询订单
     *
     * @param myOrderId
     * @return
     */
    OrderPo selectByMyOrderId(String myOrderId);

    /**
     * 根据 orderId 查询订单
     *
     * @param orderId
     * @return
     */
    OrderPo selectByOrderId(String orderId);

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param orderStatus
     * @return
     */
    int updateStatus(String orderId, String orderStatus);


    /**
     * 更新订单数据
     *
     * @param orderPo
     * @param orderId
     * @param preStatus
     * @return
     */
    int updateByOrderId(OrderPo orderPo, String orderId, String preStatus);

    /**
     * 根据状态查询订单
     *
     * @param statusList
     * @param limit
     * @return
     */
    List<OrderPo> selectByStatus(ArrayList<String> statusList, Integer limit);

    /**
     * 乐观锁，更新状态
     *
     * @param orderId
     * @param preStatus
     * @param status
     * @return
     */
    int updateStatusPre(String orderId, String preStatus, String status);

    /**
     * 查询等待取消的订单
     *
     * @param bookStatus
     * @param changeStatus
     * @param limit
     * @return
     */
    List<ServicePo> selectWaitService(String bookStatus, String changeStatus, Integer limit);

    /**
     * 查询卡单
     *
     * @param bookStatus
     * @param changeStatus
     * @param time
     * @return
     */
    List<ServicePo> selectStuckService(String bookStatus, String changeStatus, Date time);

    /**
     * 查询账号
     *
     * @return
     */
    List<Integer> selectAccountId4Jd();

    /**
     * 更新数据
     * @param orderId
     * @param statusPre
     * @param order
     * @param cps
     * @return
     */
    int updateOrderAndCp(String orderId, String statusPre, OrderPo order, List<OrderCpPo> cps);


    /**
     * 卡单
     * @param orderStatusList：状态集合
     * @param stuckTime：卡单时间
     * @return
     */
    List<OrderPo> selectStuck(List<String> orderStatusList, Date stuckTime);
}
