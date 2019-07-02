package com.kuyou.train.dao;

import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.entity.po.ServicePo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * OrderCpMapper
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface OrderMapper {

    /**
     * 根据myOrderId查询订单
     *
     * @param myOrderId
     * @return
     */
    OrderPo selectByMyOrderId(@Param("myOrderId") String myOrderId);

    /**
     * 根据 orderId 查询订单
     *
     * @param orderId
     * @return
     */
    OrderPo selectByOrderId(@Param("orderId") String orderId);

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param orderStatus
     * @return
     */
    int updateStatus(@Param("orderId") String orderId, @Param("status") String orderStatus);

    /**
     * 更新订单数据
     *
     * @param orderPo
     * @param orderId
     * @param preStatus
     * @return
     */
    int updateByOrderId(@Param("orderPo") OrderPo orderPo, @Param("orderId") String orderId,
            @Param("preStatus") String preStatus);

    /**
     * 根据状态查询订单
     *
     * @param statusList
     * @param limit
     * @return
     */
    List<OrderPo> selectByStatus(@Param("statusList") List<String> statusList, @Param("limit") Integer limit);

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param preStatus
     * @param status
     * @return
     */
    int updateStatusPre(@Param("orderId") String orderId, @Param("preStatus") String preStatus,
            @Param("status") String status);

    /**
     * 查询订单处理的订单
     *
     * @param bookStatus
     * @param changeStatus
     * @param limit
     * @return
     */
    List<ServicePo> selectWaitService(@Param("bookStatus") String bookStatus,
            @Param("changeStatus") String changeStatus,
            @Param("limit") Integer limit);

    /**
     * 查询卡单
     *
     * @param bookStatus
     * @param changeStatus
     * @param time
     * @return
     */
    List<ServicePo> selectStuckService(@Param("bookStatus") String bookStatus,
            @Param("changeStatus") String changeStatus, @Param("time") Date time);

    /**
     * 查询
     *
     * @return
     */
    List<Integer> selectAccountId4Jd();

    /**
     * 卡单
     * @param orderStatusList：状态集合
     * @param stuckTime：卡单时间
     * @return
     */
    List<OrderPo> selectStuck(@Param("orderStatusList")List<String> orderStatusList, @Param("stuckTime")Date stuckTime);
}