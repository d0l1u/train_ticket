package com.kuyou.train.dao;

import com.kuyou.train.entity.po.RefundPo;
import com.kuyou.train.entity.po.UnderRefundPo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * RefundMapper
 *
 * @author taokai3
 * @date 2018/11/9
 */
public interface RefundMapper {

    /**
     * 插入线下退票
     *
     * @param refundList
     * @return
     */
    int insertUnderRefund(@Param("refundList") List<UnderRefundPo> refundList);

    /**
     * 查询退票
     *
     * @param orderId
     * @param subSequence
     * @return
     */
    RefundPo selectRefundByOrderIdAndSeq(@Param("orderId") String orderId, @Param("subSequence") String subSequence);

    /**
     * 查询退票
     *
     * @param orderId
     * @param subSequence
     * @return
     */
    RefundPo selectRefundByMyOrderIdAndSeq(@Param("orderId") String orderId, @Param("subSequence") String subSequence);

    /**
     * 修改状态
     *
     * @param orderId
     * @param cpId
     * @param status
     * @return
     */
    int updateStatus(@Param("orderId") String orderId, @Param("cpId") String cpId, @Param("status") String status);

    /**
     * 修改退票数据
     *
     * @param refund
     * @param orderId
     * @param cpId
     * @return
     */
    int updateRefund(@Param("refundPo") RefundPo refund, @Param("orderId") String orderId, @Param("cpId") String cpId);

    /**
     * 查询等待退票的订单
     *
     * @param orderStatus
     * @param limit
     * @return
     */
    List<RefundPo> selectWaitRefund(@Param("orderStatus") String orderStatus, @Param("limit") int limit);

    /**
     * 乐观锁
     *
     * @param orderId
     * @param cpId
     * @param status
     * @param preStatus
     * @return
     */
    int updateStatusPre(@Param("orderId") String orderId, @Param("cpId") String cpId, @Param("status") String status,
            @Param("preStatus") String preStatus);

    /**
     * 查询卡状态订单
     *
     * @param status
     * @param time
     * @return
     */
    List<RefundPo> selectStuck(@Param("status") String status, @Param("time") Date time);
}
