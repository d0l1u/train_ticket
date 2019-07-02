package com.kuyou.train.dao;


import com.kuyou.train.entity.po.JdAccountPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * JdAccountMapper
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface JdAccountMapper {

    /**
     * 批量插入
     * @param jdAccountPos
     * @param jdAccountPo
     * @return
     */
    int insertBatch(@Param("jdAccountPos") List<JdAccountPo> jdAccountPos,
            @Param("jdAccountPo") JdAccountPo jdAccountPo);

    /**
     * 更新
     * @param jdAccountPo
     * @param accountId
     * @return
     */
    int updateById(@Param("jdAccountPo") JdAccountPo jdAccountPo, @Param("accountId")Long accountId);

    /**
     * 查询
     *
     * @param type
     * @return
     */
    int selectCountType(@Param("type") Integer type);


}
