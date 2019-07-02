package com.kuyou.train.service;

import com.kuyou.train.entity.po.PassengerPo;

import java.util.List;

/**
 * PassengerService
 *
 * @author taokai3
 * @date 2018/12/29
 */
public interface PassengerService {

    /**
     * 删除不存在的乘客
     *
     * @param accountId
     * @param cardNoList
     * @return
     */
    int deleteNotExist(Integer accountId, List<String> cardNoList);

    /**
     * 插入或者更新
     *
     * @param passengers
     * @return
     */
    int insertOrUpdate(List<PassengerPo> passengers);
}
