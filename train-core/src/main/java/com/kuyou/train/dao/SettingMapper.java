package com.kuyou.train.dao;

import com.kuyou.train.entity.po.SettingPo;
import org.apache.ibatis.annotations.Param;

/**
 * SettingMapper
 *
 * @author taokai3
 * @date 2018/12/26
 */
public interface SettingMapper {


    /**
     * 根据name查询
     *
     * @param name
     * @return
     */
    SettingPo selectByName(@Param("name") String name);
}
