package com.kuyou.train.dao;

import com.kuyou.train.entity.po.TrainOptlog;
import org.apache.ibatis.annotations.Param;

/**
 * TrainOptlogMapper
 *
 * @author taokai3
 * @date 2018/12/26
 */
public interface TrainOptlogMapper {

    /**
     * 获取操作mapping
     * @param message
     * @return
     */
    TrainOptlog select4Book(@Param("message") String message);
}
