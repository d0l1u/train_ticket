package com.kuyou.train.service;

import com.kuyou.train.entity.po.ChangePo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ChangeService
 *
 * @author taokai3
 * @date 2018/11/2
 */
public interface ChangeService {

    /**
     * 根据changeId查询改签数据
     *
     * @param changeId
     * @return
     */
    ChangePo selectByChangeId(Integer changeId);

    /**
     * 根据订单ID查询改签数据
     *
     * @param orderId
     * @return
     */
    List<ChangePo> selectByOrderId(String orderId);

    /**
     * 根据酷游订单ID查询改签数据
     *
     * @param myOrderId
     * @return
     */
    List<ChangePo> selectByMyOrderId(String myOrderId);

    /**
     * 根据状态查询
     *
     * @param status
     * @param limit
     * @return
     */
    List<ChangePo> selectByStatus(String status, Integer limit);

    /**
     * 根据状态查询
     *
     * @param statusList
     * @param limit
     * @return
     */
    List<ChangePo> selectByStatus(List<String> statusList, Integer limit);

    /**
     * 修改状态，通过前置状态
     *
     * @param changeId
     * @param preStatus
     * @param targetStatus
     * @return
     */
    int updateStatusPre(Integer changeId, String preStatus, String targetStatus);

    /**
     * 修改状态
     *
     * @param changeId
     * @param status
     * @param reason
     * @return
     */
    int updateStatus(Integer changeId, String status, String reason);

    /**
     * 查询卡状态的订单
     *
     * @param supplierList
     * @param stuckMinutes
     * @param status
     * @return
     */
    List<ChangePo> selectStuck(String status, ArrayList<String> supplierList, Date stuckMinutes);

    /**
     * 更新订单状态
     *
     * @param changeId
     * @param status
     * @return
     */
    int updateStatusById(Integer changeId, String status);

    /**
     * 根据 changeId 查询订单
     *
     * @param changePo
     * @param changeId
     * @return
     */
    int updateByChangeId(ChangePo changePo, Integer changeId);


}
