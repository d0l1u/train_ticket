package com.kuyou.train.service;

import com.kuyou.train.entity.po.JdAccountPo;

import java.util.List;

/**
 * JdAccountService
 *
 * @author taokai3
 * @date 2018/12/5
 */
public interface JdAccountService {

    /**
     * 查询A类账号
     *
     * @param type
     * @return
     */
    int selectCountType(Integer type);


    /**
     * 批量插入
     *
     * @param jdAccountPos
     * @return
     */
    int insertBatch(List<JdAccountPo> jdAccountPos);

    /**
     * 更新
     *
     * @param jdAccountPo
     * @param accountId
     * @return
     */
    int updateById(JdAccountPo jdAccountPo, Long accountId);
}
