package com.kuyou.train.service;

import com.kuyou.train.entity.po.RefundPo;
import com.kuyou.train.entity.po.UnderRefundPo;

import java.util.Date;
import java.util.List;

/**
 * RefundService
 *
 * @author taokai3
 * @date 2018/11/9
 */
public interface RefundService {


    /**
     * 插入线下退票
     *
     * @param refundList
     * @return
     */
    int insertUnderRefund(List<UnderRefundPo> refundList);

    /**
     * 根据orderId， 和 车票号 查询退票信息
     *
     * @param orderId
     * @param subSequence
     * @return
     */
    RefundPo selectRefundByOrderIdAndSeq(String orderId, String subSequence);

    /**
     * 根据orderId， 和 车票号 查询退票信息
     *
     * @param orderId
     * @param subSequence
     * @return
     */
    RefundPo selectRefundByMyOrderIdAndSeq(String orderId, String subSequence);

    /**
     * 修改退票订单的状态
     *
     * @param orderId
     * @param cpId
     * @param status
     */
    int updateStatus(String orderId, String cpId, String status);

    /**
     * 修改退票订单
     * @param refundPo
     * @param orderId
     * @param cpId
     * @return
     */
    int updateRefund(RefundPo refundPo, String orderId, String cpId);


    /**
     * 查询等待退票的订单
     *
     * @param orderStatus
     * @param limit
     * @return
     */
    List<RefundPo> selectWaitRefund(String orderStatus, int limit);

    /**
     * 乐观锁更新
     *
     * @param orderId
     * @param cpId
     * @param status
     * @param preStatus
     * @return
     */
    int updateStatusPre(String orderId, String cpId, String status, String preStatus);

    /**
     * 查询卡状态订单
     *
     * @param status
     * @param time
     * @return
     */
    List<RefundPo> selectStuck(String status, Date time);
}
