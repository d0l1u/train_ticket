package com.kuyou.train.dao;

import com.kuyou.train.entity.po.ChangeCpPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ChangeCpMapper
 *
 * @author taokai3
 * @date 2018/10/28
 */
public interface ChangeCpMapper {

    /**
     * 根据 changeId 查询乘客
     *
     * @param changeId
     * @return
     */
    List<ChangeCpPo> selectByChangeId(@Param("changeId") Integer changeId);

    /**
     * 根据 cpId 查询乘客
     *
     * @param cpId
     * @return
     */
    ChangeCpPo selectSequenceByNewCpId(@Param("newCpId") String cpId);

    /**
     * 根据 newCpId 查询乘客
     *
     *
     * @param changeId
     * @param newCpId
     * @return
     */
    ChangeCpPo selectByNewCpId(@Param("changeId") Integer changeId, @Param("newCpId") String newCpId);

    /**
     * 根据 orderId 查询乘客
     *
     * @param orderId
     * @return
     */
    List<ChangeCpPo> selectByOrderId(@Param("orderId") String orderId);

    /**
     * 根据 myOrderId 查询乘客
     *
     * @param myOrderId
     * @return
     */
    List<ChangeCpPo> selectByMyOrderId(@Param("myOrderId") String myOrderId);

    /**
     * 根据 changeId 修改乘客
     *
     * @param changeCpPo
     * @param cpId
     * @return
     */
    int updateByCpId(@Param("changeCpPo") ChangeCpPo changeCpPo, @Param("cpId") String cpId);


    /**
     * 根据 changeId 修改乘客
     *
     * @param changeCpPo
     * @param newCpId
     * @return
     */
    int updateByNewCpId(@Param("changeCpPo") ChangeCpPo changeCpPo, @Param("newCpId") String newCpId);

    /**
     * 根据 changeId 修改乘客
     *
     * @param changeCpPo
     * @param changeId
     * @param oldTicketNo
     * @return
     */
    int updateByChangeIdAndSeq(@Param("changeCpPo") ChangeCpPo changeCpPo, @Param("changeId") Integer changeId,
            @Param("oldTicketNo") String oldTicketNo);

    /**
     * 查询车票
     *
     * @param changeId
     * @return
     */
    List<String> selectSeqByChangeId(@Param("changeId") Integer changeId);
}