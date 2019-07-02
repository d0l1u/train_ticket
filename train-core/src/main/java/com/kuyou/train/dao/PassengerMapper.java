package com.kuyou.train.dao;

import com.kuyou.train.entity.po.PassengerPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PassengerMapper
 *
 * @author taokai3
 * @date 2018/12/29
 */
public interface PassengerMapper {

    /**
     * 删除不存在的乘客
     *
     * @param accountId
     * @param cardNoList
     * @return
     */
    int deleteNotExist(@Param("accountId") Integer accountId, @Param("cardNoList") List<String> cardNoList);

    /**
     * 更新白名单
     *
     * @param passengers
     * @return
     */
    int insertOrUpdate(@Param("passengers")List<PassengerPo> passengers);

}
