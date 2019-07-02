package com.kuyou.train.dao;

import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderStudentPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * OrderCpMapper
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface OrderCpMapper {

    /**
     * 根据cpid查询乘客信息
     *
     * @param cpId
     * @return
     */
    OrderCpPo selectByCpId(@Param("cpId") String cpId);

    /**
     * 查询票号
     *
     * @param orderId
     * @return
     */
    List<String> selectSeqByOrderId(@Param("orderId") String orderId);

    /**
     * 查询乘客
     *
     * @param orderId
     * @return
     */
    List<OrderCpPo> selectByOrderId(@Param("orderId") String orderId);

    /**
     * 查询乘客
     *
     * @param cpId
     * @return
     */
    OrderStudentPo selectStudent(@Param("cpId") String cpId);

    /**
     * 更新数据
     * @param cpId
     * @param cp
     * @return
     */
    int updateByCpid(@Param("cpId")String cpId, @Param("cp") OrderCpPo cp);
}
