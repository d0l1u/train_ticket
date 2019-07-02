package com.kuyou.train.dao;

import com.kuyou.train.entity.po.ChangePo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ChangeMapper
 *
 * @author taokai3
 * @date 2018/10/28
 */
public interface ChangeMapper {

    /**
     * g根据状态查询订单
     *
     * @param statusList
     * @param limit
     * @return
     */
    List<ChangePo> selectByStatus(@Param("statusList") List<String> statusList, @Param("limit") Integer limit);

    /**
     * 根据changeId查询订单
     *
     * @param changeId
     * @return
     */
    ChangePo selectByChangeId(@Param("changeId") Integer changeId);

    /**
     * 根据 orderId 查询订单
     *
     * @param orderId
     * @return
     */
    List<ChangePo> selectByOrderId(@Param("orderId") String orderId);

    /**
     * 根据 myOrderId 查询订单
     *
     * @param myOrderId
     * @return
     */
    List<ChangePo> selectByMyOrderId(@Param("myOrderId") String myOrderId);

    /**
     * 根据 changeId 查询订单
     *
     * @param changePo
     * @param changeId
     * @return
     */
    int updateByChangeId(@Param("changePo") ChangePo changePo, @Param("changeId") Integer changeId);

    /**
     * 更新状态
     *
     * @param changeId
     * @param preStatus
     * @param targetStatus
     * @return
     */
    int updateStatusPre(@Param("changeId") Integer changeId, @Param("preStatus") String preStatus,
            @Param("targetStatus") String targetStatus);

    /**
     * 更新状态
     *
     * @param changeId
     * @param status
     * @param reason
     * @return
     */
    int updateStatus(@Param("changeId") Integer changeId, @Param("status") String status,
            @Param("reason") String reason);

    /**
     * 查询卡单的订单
     *
     *
     * @param status
     * @param supplierList
     * @param stuckMinutes
     * @return
     */
    List<ChangePo> selectStuck(@Param("status") String status, @Param("supplierList") ArrayList<String> supplierList,
            @Param("stuckMinutes") Date stuckMinutes);

    /**
     * 修改订单状态
     *
     * @param changeId
     * @param status
     * @return
     */
    int updateStatusById(@Param("changeId") Integer changeId, @Param("status") String status);


}