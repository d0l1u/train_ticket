package com.kuyou.train.service;

import com.kuyou.train.entity.po.ChangeCpPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ChangeCpService
 *
 * @author taokai3
 * @date 2018/11/2
 */
public interface ChangeCpService {


    /**
     * 根据 changeId 查询乘客
     *
     * @param changeId
     * @return
     */
    List<ChangeCpPo> selectByChangeId(Integer changeId);

    /**
     * 根据 cpId 查询乘客票号
     *
     * @param cpId
     * @return
     */
    ChangeCpPo selectSequenceByNewCpId(String cpId);

    /**
     * 根据 newCpId 查询乘客：高铁的newCpid 不是随机生成的
     *
     * @param changeId
     * @param newCpId
     * @return
     */
    ChangeCpPo selectByNewCpId(Integer changeId, String newCpId);



    /**
     * 根据 orderId 查询乘客
     *
     * @param orderId
     * @return
     */
    List<ChangeCpPo> selectByOrderId(String orderId);

    /**
     * 根据 myOrderId 查询乘客
     *
     * @param myOrderId
     * @return
     */
    List<ChangeCpPo> selectByMyOrderId(String myOrderId);

    /**
     * 根据 changeId 修改乘客
     *
     * @param changeCpPo
     * @param cpId
     * @return
     */
    int updateByCpId(ChangeCpPo changeCpPo, String cpId);


    /**
     * 根据 changeId 修改乘客
     *
     * @param changeCpPo
     * @param newCpId
     * @return
     */
    int updateByNewCpId(ChangeCpPo changeCpPo, @Param("newCpId") String newCpId);

    /**
     * 根据 changeId 修改乘客
     *
     * @param changeCpPo
     * @param changeId
     * @param oldTicketNo
     * @return
     */
    int updateByChangeIdAndSeq(ChangeCpPo changeCpPo, Integer changeId, String oldTicketNo);

    /**
     * 查询车票
     *
     * @param integer
     * @return
     */
    List<String> selectSeqByChangeId(Integer integer);
}
